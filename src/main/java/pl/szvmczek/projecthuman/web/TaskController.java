package pl.szvmczek.projecthuman.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szvmczek.projecthuman.domain.task.Task;
import pl.szvmczek.projecthuman.domain.task.TaskService;
import pl.szvmczek.projecthuman.domain.task.dto.TaskEditDto;
import pl.szvmczek.projecthuman.domain.user.User;
import pl.szvmczek.projecthuman.domain.user.UserService;
import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService,UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String viewTasks(Model model, @AuthenticationPrincipal UserCredentialsDto user){
        if(user == null){
            return "redirect:/login";
        }
        List<Task> userTasks = taskService.findAllTasksFromUserId(user.getId());
        model.addAttribute("tasks",userTasks);
        return "task-main-page";

    }

    @GetMapping("/add")
    public String viewAddForm(Model model){
        model.addAttribute("task",new Task());
        return "add-form";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task, @AuthenticationPrincipal UserCredentialsDto user){
        Optional<User> authUser = userService.findUserByEmail(user.getEmail());
        authUser.ifPresent(task::setUser);
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/complete")
    public String completeTask(@RequestParam Long id){
        Optional<Task> task = taskService.findTaskById(id);
        task.ifPresent(t -> taskService.changeStatus(id));
        return "redirect:/tasks";
    }

    @PostMapping("/delete")
    public String deleteTask(@RequestParam Long id){
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/edit")
    public String viewEditForm(@RequestParam Long id, Model model, @AuthenticationPrincipal UserCredentialsDto user){
        Optional<Task> task = taskService.findTaskById(id);
        if(task.isEmpty())
            return "redirect:/tasks";
        if(!task.get().getUser().getId().equals(user.getId()))
            return "redirect:/tasks";
        model.addAttribute("task",task.get());
        return "edit-form";
    }

    @PostMapping("/edit")
    public String  editTask(@ModelAttribute TaskEditDto task, @AuthenticationPrincipal UserCredentialsDto user){
        System.out.println();
        taskService.updateTask(task,user.getId());
        return "redirect:/tasks";
    }
}
