package pl.szvmczek.projecthuman.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.szvmczek.projecthuman.domain.category.Category;
import pl.szvmczek.projecthuman.domain.category.CategoryService;
import pl.szvmczek.projecthuman.domain.habit.HabitService;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitAddDto;
import pl.szvmczek.projecthuman.domain.habit.dto.HabitEditDto;
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
        return "task-main-page";
    }

    @GetMapping("/add")
    public String viewAddForm(Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        List<Category> userCategories = categoryService.getAllCategoriesByUser(user.getId());
        model.addAttribute("habit", new HabitAddDto());
        model.addAttribute("categories", userCategories);
        return "task-add-form";
    }

    @PostMapping("/add")
    public String addHabit(@ModelAttribute HabitAddDto habit, @AuthenticationPrincipal UserCredentialsDto user) {
        habitService.saveHabit(habit, user.getId());
        return "redirect:/habits";
    }

    @PostMapping("/complete")
    public String completeHabit(@RequestParam("id") Long habitId, @AuthenticationPrincipal UserCredentialsDto user) {
        habitService.toggleCompletion(habitId, user.getId());
        return "redirect:/habits";
    }

    @GetMapping("/{id}/delete")
    public String deleteHabit(@PathVariable Long id, @AuthenticationPrincipal UserCredentialsDto user) {
        habitService.deleteHabit(id,user.getId());
        return "redirect:/habits";
    }

    @GetMapping("/{id}/edit")
    public String viewEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        HabitEditDto habitForEdit = habitService.getHabitForEdit(id, user.getId());
        List<Category> userCategories = categoryService.getAllCategoriesByUser(user.getId());
        model.addAttribute("habit", habitForEdit);
        model.addAttribute("categories", userCategories);
        return "task-edit-form";
    }

    @PostMapping("/edit")
    public String editHabit(@ModelAttribute HabitEditDto habit, @AuthenticationPrincipal UserCredentialsDto user) {
        habitService.updateHabitForUser(habit, user.getId());
        return "redirect:/habits";
    }
}
