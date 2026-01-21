package pl.szvmczek.projecthuman.web;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.szvmczek.projecthuman.domain.badhabit.BadHabitService;
import pl.szvmczek.projecthuman.domain.badhabit.dto.BadHabitCreateDto;
import pl.szvmczek.projecthuman.domain.badhabit.dto.BadHabitViewDto;
import pl.szvmczek.projecthuman.domain.badhabit.dto.BadHabitUpdateDto;
import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.List;

@Controller
@RequestMapping("/bad-habits")
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
    public String addHabitDrop(@ModelAttribute("habit") @Valid BadHabitCreateDto dto,
                               BindingResult exceptions,
                               @AuthenticationPrincipal UserCredentialsDto user){
        if(exceptions.hasErrors())
            return "bad_habit-add";
        badHabitService.create(dto,user.getId());
        return "redirect:/bad-habits";
    }

    @GetMapping("/{id}/edit")
    public String editHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user, Model model){
        BadHabitUpdateDto habitForEdit = badHabitService.getHabitForEdit(habitId, user.getId());
        model.addAttribute("habit", habitForEdit);
        return "bad_habit-edit";
    }

    @PostMapping("/edit")
    public String editHabitDrop(@ModelAttribute("habit") @Valid BadHabitUpdateDto habit,
                                BindingResult exceptions,
                                @AuthenticationPrincipal UserCredentialsDto user){
        if(exceptions.hasErrors())
            return "bad_habit-edit";
        badHabitService.update(habit, user.getId());
        return "redirect:/bad-habits";
    }

    @PostMapping("/{id}/start")
    public String startHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        badHabitService.start(habitId, user.getId());
        return "redirect:/bad-habits";
    }

    @PostMapping("/{id}/delete")
    public String deleteHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        badHabitService.delete(habitId, user.getId());
        return "redirect:/bad-habits";
    }

    @PostMapping("/{id}/reset")
    public String resetHabitDrop(@PathVariable("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user){
        badHabitService.reset(habitId, user.getId());
        return "redirect:/bad-habits";
    }
}
