package pl.szvmczek.projecthuman.domain.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void changeStatus(Long id){
        Task task = taskRepository.findById(id).get();
        task.setDone(!task.isDone());
    }

    public List<Task> getAllTasks(){
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public void saveTask(Task task){
        taskRepository.save(task);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    public Optional<Task> findTaskById(Long id){
        return taskRepository.findById(id);
    }
}
