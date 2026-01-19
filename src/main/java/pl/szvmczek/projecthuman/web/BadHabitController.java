package pl.szvmczek.projecthuman.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.szvmczek.projecthuman.domain.habitdrop.BadHabitService;
import pl.szvmczek.projecthuman.domain.habitdrop.dto.BadHabitCreateDto;
import pl.szvmczek.projecthuman.domain.habitdrop.dto.BadHabitViewDto;
import pl.szvmczek.projecthuman.domain.habitdrop.dto.BadHabitUpdateDto;
import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.List;

@Controller
@RequestMapping("/habit-drops")
public class BadHabitController {
    private final BadHabitService badHabitService;

    public BadHabitController(BadHabitService badHabitService) {
        this.badHabitService = badHabitService;
    }

    @GetMapping()
    public String habitDrops(@AuthenticationPrincipal UserCredentialsDto user, Model model){
        List<BadHabitViewDto> habits = badHabitService.getAll(user.getId());
        model.addAttribute("habits", habits);
        return "bad_habit";
    }

    @GetMapping("/add")
    public String viewAddForm(Model model){
        model.addAttribute("habit",new BadHabitCreateDto());
        return "bad_habit-add";
    }

    @PostMapping("/add")
    public String addHabitDrop(@ModelAttribute BadHabitCreateDto dto, @AuthenticationPrincipal UserCredentialsDto user){
        badHabitService.create(dto,user.getId());
        return "redirect:/habit-drops";
    }

    @GetMapping("/{id}/edit")
    public String editHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user, Model model){
        BadHabitUpdateDto habitForEdit = badHabitService.getHabitForEdit(habitId, user.getId());
        model.addAttribute("habit", habitForEdit);
        return "bad_habit-edit";
    }

    @PostMapping("/edit")
    public String editHabitDrop(@ModelAttribute BadHabitUpdateDto habit, @AuthenticationPrincipal UserCredentialsDto user){
        badHabitService.update(habit, user.getId());
        return "redirect:/habit-drops";
    }

    @PostMapping("/{id}/start")
    public String startHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        badHabitService.start(habitId, user.getId());
        return "redirect:/habit-drops";
    }

    @PostMapping("/{id}/delete")
    public String deleteHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        badHabitService.delete(habitId, user.getId());
        return "redirect:/habit-drops";
    }

    @PostMapping("/{id}/reset")
    public String resetHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        badHabitService.reset(habitId, user.getId());
        return "redirect:/habit-drops";
    }
}
