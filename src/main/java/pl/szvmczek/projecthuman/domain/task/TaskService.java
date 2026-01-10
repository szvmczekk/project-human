package pl.szvmczek.projecthuman.domain.task;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szvmczek.projecthuman.domain.category.Category;
import pl.szvmczek.projecthuman.domain.category.CategoryService;
import pl.szvmczek.projecthuman.domain.task.dto.TaskAddDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskEditDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskViewDto;
import pl.szvmczek.projecthuman.domain.user.User;
import pl.szvmczek.projecthuman.domain.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskCompletionRepository taskCompletionRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public TaskService(TaskRepository taskRepository,
                       TaskCompletionRepository taskCompletionRepository,
                       UserService userService,
                       CategoryService categoryService) {
        this.taskRepository = taskRepository;
        this.taskCompletionRepository = taskCompletionRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    public List<TaskViewDto> getTasksForUser(Long userId) {
        List<Task> tasksByUser = taskRepository.findAllByUserId(userId);
        return tasksByUser.stream()
                .map(TaskDtoMapper::map)
                .toList();
    }

    @Transactional
    public void saveTask(TaskAddDto taskAddDto, Long userId){
        Task taskToSave = TaskDtoMapper.map(taskAddDto);
        User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(taskAddDto.getCategoryId() != null) {
            Category category = categoryService.getCategoryByIdAndUserId(taskAddDto.getCategoryId(), userId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            taskToSave.setCategory(category);
        }
        taskToSave.setUser(user);
        taskRepository.save(taskToSave);
    }

    @Transactional
    public void changeStatus(Long taskId, Long userId) {
        Task task = getTaskOrThrow(taskId, userId);
        LocalDate today = LocalDate.now();
        boolean doneToday = taskCompletionRepository.existsByTask_IdAndDate(task.getId(), today);

        if (doneToday) {
            taskCompletionRepository.deleteByTask_IdAndDate(task.getId(), today);
            Optional<TaskCompletion> prev =
                    taskCompletionRepository.findTopByTask_IdAndDateLessThanOrderByDateDesc(task.getId(), today);
            if (prev.isEmpty()) {
                task.setLastCompletionDate(null);
                task.setCurrentStreak(0);
            } else {
                LocalDate prevDate = prev.get().getDate();
                task.setLastCompletionDate(prevDate);
                task.setCurrentStreak(recalculateStreakEndingAt(task.getId(), prevDate));
            }
        } else {
            taskCompletionRepository.save(new TaskCompletion(task, today));
            LocalDate last = task.getLastCompletionDate();
            if (last != null && last.isEqual(today.minusDays(1))) {
                task.setCurrentStreak(task.getCurrentStreak() + 1);
            } else {
                task.setCurrentStreak(1);
            }
            task.setLastCompletionDate(today);
        }
    }


    public void deleteTask(Long taskId,Long userId){
        Task task = getTaskOrThrow(taskId, userId);
        taskRepository.delete(task);
    }

    public TaskEditDto getTaskForEdit(Long taskId, Long userId){
        Task task = getTaskOrThrow(taskId, userId);
        return new TaskEditDto(task.getId(),task.getTitle(),task.getDescription());
    }

    @Transactional
    public void updateTaskForUser(TaskEditDto dto, Long userId){
        Task originalTask = getTaskOrThrow(dto.getId(), userId);
        originalTask.setTitle(dto.getTitle());
        originalTask.setDescription(dto.getDescription());
        categoryService.getCategoryByIdAndUserId(dto.getCategoryId(),userId).ifPresent(originalTask::setCategory);
    }

    private Task getTaskOrThrow(Long taskId, Long userId){
        return taskRepository.findByIdAndUserId(taskId,userId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found!"));
    }

    private int recalculateStreakEndingAt(Long taskId, LocalDate endDate) {
        List<LocalDate> dates = taskCompletionRepository.findDatesUpToDesc(taskId, endDate);
        if (dates.isEmpty()) return 0;

        int streak = 0;
        LocalDate expected = endDate;

        for (LocalDate d : dates) {
            if (!d.equals(expected)) break;
            streak++;
            expected = expected.minusDays(1);
        }
        return streak;
    }

}
