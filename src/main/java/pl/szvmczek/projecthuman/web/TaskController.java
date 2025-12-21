package pl.szvmczek.projecthuman.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szvmczek.projecthuman.domain.task.Task;
import pl.szvmczek.projecthuman.domain.task.TaskService;

import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String home(Model model){
        List<Task> allTasks = taskService.getAllTasks();
        model.addAttribute("tasks",allTasks);
        return "index";
    }

    @GetMapping("/add")
    public String viewAddForm(Model model){
        model.addAttribute("task",new Task());
        return "add-form";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task){
        taskService.saveTask(task);
        return "redirect:/";
    }

    @PostMapping("/complete")
    public String completeTask(@RequestParam Long id){
        Optional<Task> task = taskService.findTaskById(id);
        task.ifPresent(t -> taskService.changeStatus(id));
        return "redirect:/";
    }
}
