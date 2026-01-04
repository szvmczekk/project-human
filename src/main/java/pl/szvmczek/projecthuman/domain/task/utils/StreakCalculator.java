package pl.szvmczek.projecthuman.domain.task.utils;

import pl.szvmczek.projecthuman.domain.task.Task;
import pl.szvmczek.projecthuman.domain.task.TaskCompletion;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class StreakCalculator {
    public static int getTaskStreak(Task task){
        if(task.getCompletions().isEmpty())
            return 0;

        Set<LocalDate> dates = task.getCompletions().stream()
                .map(TaskCompletion::getDate)
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now();
        LocalDate last = dates.stream()
                .max(LocalDate::compareTo)
                .get();

        if(last.isBefore(today.minusDays(1))) return 0;

        LocalDate day = last;
        int streak = 0;

        while(dates.contains(day)){
            streak++;
            day = day.minusDays(1);
        }

        return streak;
    }
}
