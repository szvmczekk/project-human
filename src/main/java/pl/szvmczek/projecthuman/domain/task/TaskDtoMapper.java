package pl.szvmczek.projecthuman.domain.task;

import pl.szvmczek.projecthuman.domain.task.dto.TaskAddDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskViewDto;

import java.time.LocalDate;

public class TaskDtoMapper {
    static TaskViewDto map(Task task){
        return new TaskViewDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                checkIfTaskIsCompleted(task),
                getTaskStreak(task),
                getTaskCategory(task));
    }

    static Task map(TaskAddDto taskAddDto){
        return new Task(taskAddDto.getTitle(), taskAddDto.getDescription());
    }

    private static boolean checkIfTaskIsCompleted(Task task){
        if(task.getLastCompletionDate() == null) return false;
        return task.getLastCompletionDate().isEqual(LocalDate.now());
    }

    private static int getTaskStreak(Task task){
        if(task.getLastCompletionDate() == null) return 0;
        if(task.getLastCompletionDate().isBefore(LocalDate.now().minusDays(1))){
            return 0;
        }
        return task.getCurrentStreak();
    }

    private static String getTaskCategory(Task task){
        if(task.getCategory() == null) return "Uncategorized";
        return task.getCategory().getName();
    }
}
