package pl.szvmczek.projecthuman.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.szvmczek.projecthuman.domain.category.Category;
import pl.szvmczek.projecthuman.domain.category.CategoryService;
import pl.szvmczek.projecthuman.domain.task.TaskService;
import pl.szvmczek.projecthuman.domain.task.dto.TaskAddDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskEditDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskViewDto;
import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final CategoryService categoryService;

    public TaskController(TaskService taskService, CategoryService categoryService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String viewTasks(Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        List<TaskViewDto> userTasks = taskService.getTasksForUser(user.getId());
//        List<Category> userCategories = categoryService.getAllCategoriesByUser(user.getId());
        model.addAttribute("tasks", userTasks);
//        model.addAttribute("categories", userCategories);
        return "task-main-page";
    }

    @GetMapping("/add")
    public String viewAddForm(Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        List<Category> userCategories = categoryService.getAllCategoriesByUser(user.getId());
        model.addAttribute("task", new TaskAddDto());
        model.addAttribute("categories", userCategories);
        return "task-add-form";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute TaskAddDto task, @AuthenticationPrincipal UserCredentialsDto user) {
        taskService.saveTask(task, user.getId());
        return "redirect:/tasks";
    }

    @PostMapping("/complete")
    public String completeTask(@RequestParam("id") Long taskId, @AuthenticationPrincipal UserCredentialsDto user) {
        taskService.changeStatus(taskId, user.getId());
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserCredentialsDto user) {
        taskService.deleteTask(id,user.getId());
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String viewEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        TaskEditDto taskForEdit = taskService.getTaskForEdit(id, user.getId());
        List<Category> userCategories = categoryService.getAllCategoriesByUser(user.getId());
        model.addAttribute("task", taskForEdit);
        model.addAttribute("categories", userCategories);
        return "task-edit-form";
    }

    @PostMapping("/edit")
    public String editTask(@ModelAttribute TaskEditDto task, @AuthenticationPrincipal UserCredentialsDto user) {
        taskService.updateTaskForUser(task, user.getId());
        return "redirect:/tasks";
    }
}
