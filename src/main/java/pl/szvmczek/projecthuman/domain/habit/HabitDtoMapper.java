package pl.szvmczek.projecthuman.domain.habit;

import pl.szvmczek.projecthuman.domain.habit.dto.HabitAddDto;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitViewDto;

import java.time.LocalDate;

public class HabitDtoMapper {
    static HabitViewDto map(Habit habit){
        return new HabitViewDto(
                habit.getId(),
                habit.getTitle(),
                habit.getDescription(),
                checkIfTaskIsCompleted(habit),
                getTaskStreak(habit),
                getTaskCategory(habit));
    }

    static Habit map(HabitAddDto habitAddDto){
        return new Habit(habitAddDto.getTitle(), habitAddDto.getDescription());
    }

    private static boolean checkIfTaskIsCompleted(Habit habit){
        if(habit.getLastCompletionDate() == null) return false;
        return habit.getLastCompletionDate().isEqual(LocalDate.now());
    }

    private static int getTaskStreak(Habit habit){
        if(habit.getLastCompletionDate() == null) return 0;
        if(habit.getLastCompletionDate().isBefore(LocalDate.now().minusDays(1))){
            return 0;
        }
        return habit.getCurrentStreak();
    }

    private static String getTaskCategory(Habit habit){
        if(habit.getCategory() == null) return "Uncategorized";
        return habit.getCategory().getName();
    }
}
