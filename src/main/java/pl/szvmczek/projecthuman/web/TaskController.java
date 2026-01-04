package pl.szvmczek.projecthuman.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szvmczek.projecthuman.domain.task.TaskService;
import pl.szvmczek.projecthuman.domain.task.dto.TaskAddDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskEditDto;
import pl.szvmczek.projecthuman.domain.task.dto.TaskViewDto;
import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String viewTasks(Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        List<TaskViewDto> userTasks = taskService.getTasksForUser(user.getId());
        model.addAttribute("tasks", userTasks);
        return "task-main-page";
    }

    @GetMapping("/add")
    public String viewAddForm(Model model) {
        model.addAttribute("task", new TaskAddDto());
        return "add-form";
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

    @PostMapping("/delete")
    public String deleteTask(@RequestParam Long id, @AuthenticationPrincipal UserCredentialsDto user) {
        taskService.deleteTask(id,user.getId());
        return "redirect:/tasks";
    }

    @GetMapping("/edit")
    public String viewEditForm(@RequestParam Long id, Model model, @AuthenticationPrincipal UserCredentialsDto user) {
        TaskEditDto taskForEdit = taskService.getTaskForEdit(id, user.getId());
        model.addAttribute("task", taskForEdit);
        return "edit-form";
    }

    @PostMapping("/edit")
    public String editTask(@ModelAttribute TaskEditDto task, @AuthenticationPrincipal UserCredentialsDto user) {
        taskService.updateTaskForUser(task, user.getId());
        return "redirect:/tasks";
    }
}
