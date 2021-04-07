package com.msk.taskmanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.msk.taskmanager.model.Task;
import com.msk.taskmanager.model.User;
import com.msk.taskmanager.service.TaskService;
import com.msk.taskmanager.service.UserService;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String listTasks(Model model) {

        model.addAttribute("tasks", taskService.findAll());
        return "views/tasksList";
    }

    @GetMapping("task/create")
    public String showTaskForm(Model model) {

        User user = userService.getUserByEmail("admin@mail.com");
        Task task = new Task();
        task.setCreatorName(user.getName());
        model.addAttribute("task", task);
        return "views/emptyTaskForm";
    }

    @PostMapping("task/create")
    public String createTask(@Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "views/emptyTaskForm";
        }
        taskService.createTask(task);

        return "redirect:/tasks";
    }

    @GetMapping("task/edit/{id}")
    public String showFilledTaskForm(@PathVariable Long id,  Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "views/filledTaskForm";
    }

    @PostMapping("task/edit/{id}")
    public String updateTask(@Valid Task task, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "views/filledTaskForm";
        }
        taskService.updateTask(id, task);
        return "redirect:/tasks";
    }

    @GetMapping("task/markDone/{id}")
    public String setTaskCompleted(@PathVariable Long id) {
        taskService.setTaskCompleted(id);
        return "redirect:/tasks";
    }

    @GetMapping("task/markUndone/{id}")
    public String setTaskNotCompleted(@PathVariable Long id) {
        taskService.setTaskNotCompleted(id);
        return "redirect:/tasks";
    }

}
