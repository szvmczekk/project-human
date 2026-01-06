package pl.szvmczek.projecthuman.domain.task;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szvmczek.projecthuman.domain.category.Category;
import pl.szvmczek.projecthuman.domain.category.CategoryService;
import pl.szvmczek.projecthuman.domain.task.dto.TaskAddDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskEditDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskViewDto;
import pl.szvmczek.projecthuman.domain.task.utils.StreakCalculator;
import pl.szvmczek.projecthuman.domain.user.User;
import pl.szvmczek.projecthuman.domain.user.UserService;

import java.time.LocalDate;
import java.util.List;

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

    public List<TaskViewDto> getTasksForUser(Long userId) {
        List<Task> tasksByUser = taskRepository.findAllByUserId(userId);
        return tasksByUser.stream()
                .map(task -> TaskDtoMapper.map(
                        task,
                        taskCompletionRepository.existsByTask_IdAndDate(task.getId(), LocalDate.now()),
                        StreakCalculator.getTaskStreak(task)
                ))
                .toList();
    }

    public void saveTask(TaskAddDto taskAddDto, Long userId){
        Task taskToSave = TaskDtoMapper.map(taskAddDto);
        User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Category category = categoryService.getCategoryByIdAndUserId(taskAddDto.getCategoryId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        taskToSave.setUser(user);
        taskToSave.setCategory(category);
        taskRepository.save(taskToSave);
    }

    @Transactional
    public void changeStatus(Long taskId,Long userId){
        Task task = getTaskOrThrow(taskId, userId);
        LocalDate today = LocalDate.now();
        if(taskCompletionRepository.existsByTask_IdAndDate(task.getId(),today)) {
            taskCompletionRepository.deleteByTask_IdAndDate(task.getId(), today);
        }else{
            task.getCompletions().add(new TaskCompletion(task,today));
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
}
