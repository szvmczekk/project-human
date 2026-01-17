package pl.szvmczek.projecthuman.domain.habitdrop;

import pl.szvmczek.projecthuman.domain.habitdrop.dto.HabitDropCreateDto;
import pl.szvmczek.projecthuman.domain.habitdrop.dto.HabitDropViewDto;
import pl.szvmczek.projecthuman.domain.habitdrop.dto.HabitDropUpdateDto;

public class HabitDropDtoMapper {

    static HabitDrop map(HabitDropCreateDto habitDropDto){
        return new HabitDrop(habitDropDto.getTitle(), habitDropDto.getDescription());
    }

    static HabitDropUpdateDto mapToEdit(HabitDrop habit){
        return new HabitDropUpdateDto(habit.getId(), habit.getTitle(), habit.getDescription());
    }

    static HabitDropViewDto map(HabitDrop habitDrop){
        return new HabitDropViewDto(
                habitDrop.getId(),
                habitDrop.getTitle(),
                habitDrop.getDescription(),
                habitDrop.isActive(),
                habitDrop.getStartDateTime(),
                habitDrop.getResetCount());
    }
}
