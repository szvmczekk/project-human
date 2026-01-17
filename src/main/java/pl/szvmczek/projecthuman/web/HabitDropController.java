package pl.szvmczek.projecthuman.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.szvmczek.projecthuman.domain.habitdrop.HabitDropService;
import pl.szvmczek.projecthuman.domain.habitdrop.dto.HabitDropCreateDto;
import pl.szvmczek.projecthuman.domain.habitdrop.dto.HabitDropViewDto;
import pl.szvmczek.projecthuman.domain.habitdrop.dto.HabitDropUpdateDto;
import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.List;

@Controller
@RequestMapping("/habit-drops")
public class HabitDropController {
    private final HabitDropService habitDropService;

    public HabitDropController(HabitDropService habitDropService) {
        this.habitDropService = habitDropService;
    }

    @GetMapping()
    public String habitDrops(@AuthenticationPrincipal UserCredentialsDto user, Model model){
        List<HabitDropViewDto> habits = habitDropService.getAll(user.getId());
        model.addAttribute("habits", habits);
        return "habit-drops";
    }

    @GetMapping("/add")
    public String viewAddForm(Model model){
        model.addAttribute("habit",new HabitDropCreateDto());
        return "habit-drop-add-form";
    }

    @PostMapping("/add")
    public String addHabitDrop(@ModelAttribute HabitDropCreateDto dto, @AuthenticationPrincipal UserCredentialsDto user){
        habitDropService.create(dto,user.getId());
        return "redirect:/habit-drops";
    }

    @GetMapping("/{id}/edit")
    public String editHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user, Model model){
        HabitDropUpdateDto habitForEdit = habitDropService.getHabitForEdit(habitId, user.getId());
        model.addAttribute("habit", habitForEdit);
        return "habit-drop-edit-form";
    }

    @PostMapping("/edit")
    public String editHabitDrop(@ModelAttribute HabitDropUpdateDto habit, @AuthenticationPrincipal UserCredentialsDto user){
        habitDropService.update(habit, user.getId());
        return "redirect:/habit-drops";
    }

    @PostMapping("/{id}/start")
    public String startHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        habitDropService.start(habitId, user.getId());
        return "redirect:/habit-drops";
    }

    @PostMapping("/{id}/delete")
    public String deleteHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        habitDropService.delete(habitId, user.getId());
        return "redirect:/habit-drops";
    }

    @PostMapping("/{id}/reset")
    public String resetHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        habitDropService.reset(habitId, user.getId());
        return "redirect:/habit-drops";
    }
}
