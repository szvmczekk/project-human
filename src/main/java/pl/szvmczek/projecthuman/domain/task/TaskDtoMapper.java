package pl.szvmczek.projecthuman.domain.task;

import pl.szvmczek.projecthuman.domain.task.dto.TaskAddDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskViewDto;

public class TaskDtoMapper {
    static TaskViewDto map(Task task, boolean completed){
        return new TaskViewDto(task.getId(), task.getTitle(), task.getDescription(), completed);
    }

    static Task map(TaskAddDto taskAddDto){
        return new Task(taskAddDto.getTitle(), taskAddDto.getDescription());
    }
}
