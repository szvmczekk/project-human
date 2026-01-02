package pl.szvmczek.projecthuman.domain.task;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szvmczek.projecthuman.domain.task.dto.TaskAddDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskEditDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskViewDto;
import pl.szvmczek.projecthuman.domain.user.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskCompletionRepository taskCompletionRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, TaskCompletionRepository taskCompletionRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskCompletionRepository = taskCompletionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void changeStatus(Long taskId,Long userId){
        Task task = getTaskOrThrow(taskId, userId);
        LocalDate today = LocalDate.now();
        if(taskCompletionRepository.existsByTask_IdAndDate(taskId,today)) {
            taskCompletionRepository.deleteByTask_IdAndDate(taskId, today);
        }else{
            task.getCompletions().add(new TaskCompletion(task,today));
        }
    }

    public List<TaskViewDto> findAllTasksFromUserId(Long userId) {
        List<Task> tasksByUser = taskRepository.findAllByUserId(userId);
        return tasksByUser.stream()
                .map(task -> TaskDtoMapper.map(task, taskCompletionRepository.existsByTask_IdAndDate(task.getId(), LocalDate.now())))
                .toList();
    }

    @Transactional
    public void updateTask(TaskEditDto dto, Long userId){
        Task originalTask = taskRepository.findByIdAndUserId(dto.getId(), userId)
                .orElseThrow(() -> new AccessDeniedException("Task not found or wrong authentication!"));
        originalTask.setTitle(dto.getTitle());
        originalTask.setDescription(dto.getDescription());
    }

    public void saveTask(TaskAddDto task, Long userId){
        Task taskToSave = TaskDtoMapper.map(task);
        userRepository.findById(userId).ifPresent(user -> {
            taskToSave.setUser(user);
            taskRepository.save(taskToSave);
        });
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    public Optional<Task> findTaskById(Long id){
        return taskRepository.findById(id);
    }

    private Task getTaskOrThrow(Long taskId, Long userId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found!"));
        if(!task.getUser().getId().equals(userId))
            throw new AccessDeniedException("No permission");
        return task;
    }
}
