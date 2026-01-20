package pl.szvmczek.projecthuman.web;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.szvmczek.projecthuman.domain.category.Category;
import pl.szvmczek.projecthuman.domain.category.CategoryService;
import pl.szvmczek.projecthuman.domain.habit.HabitService;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitCreateDto;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitUpdateDto;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitViewDto;
import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.List;

@Controller
@RequestMapping("/habits")
public class HabitController {
    private final HabitService habitService;
    private final CategoryService categoryService;

    public HabitController(HabitService habitService, CategoryService categoryService) {
        this.habitService = habitService;
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String viewHabits(Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        List<HabitViewDto> userTasks = habitService.getHabitsForUser(user.getId());
        model.addAttribute("habits", userTasks);
        return "habit";
    }

    @GetMapping("/add")
    public String viewAddForm(Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        List<Category> userCategories = categoryService.getAllCategoriesByUser(user.getId());
        model.addAttribute("habit", new HabitCreateDto());
        model.addAttribute("categories", userCategories);
        return "habit-add";
    }

    @PostMapping("/add")
    public String addHabit(@ModelAttribute("habit") @Valid HabitCreateDto habit,
                           BindingResult exceptions,
                           @AuthenticationPrincipal UserCredentialsDto user) {
        if (exceptions.hasErrors()) {
            return "habit-add";
        }
        habitService.saveHabit(habit, user.getId());
        return "redirect:/habits";
    }

    @PostMapping("/complete")
    public String completeHabit(@RequestParam("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user) {
        habitService.toggleCompletion(habitId, user.getId());
        return "redirect:/habits";
    }

    @PostMapping("/{id}/delete")
    public String deleteHabit(@PathVariable Long id, @AuthenticationPrincipal UserCredentialsDto user) {
        habitService.deleteHabit(id,user.getId());
        return "redirect:/habits";
    }

    @GetMapping("/{id}/edit")
    public String viewEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        HabitUpdateDto habitForEdit = habitService.getHabitForEdit(id, user.getId());
        List<Category> userCategories = categoryService.getAllCategoriesByUser(user.getId());
        model.addAttribute("habit", habitForEdit);
        model.addAttribute("categories", userCategories);
        return "habit-edit";
    }

    @PostMapping("/edit")
    public String editHabit(@ModelAttribute HabitUpdateDto habit, @AuthenticationPrincipal UserCredentialsDto user) {
        habitService.updateHabitForUser(habit, user.getId());
        return "redirect:/habits";
    }
}
