package com.piero.task_tracker_spring;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> list(@AuthenticationPrincipal Long userId) {
        return service.findAll(userId);
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        return service.findById(id, userId).orElseThrow(TaskController::notFound);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody TaskRequest req, @AuthenticationPrincipal Long userId) {
        return service.create(req, userId);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody TaskRequest req,
                       @AuthenticationPrincipal Long userId) {
        return service.update(id, req, userId).orElseThrow(TaskController::notFound);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        if (!service.delete(id, userId)) {
            throw notFound();
        }
    }

    private static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }
}