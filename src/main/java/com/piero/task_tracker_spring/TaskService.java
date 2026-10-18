package com.piero.task_tracker_spring;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository tasks;
    private final UserRepository users;

    public TaskService(TaskRepository tasks, UserRepository users) {
        this.tasks = tasks;
        this.users = users;
    }

    public List<Task> findAll(Long userId) {
        return tasks.findByOwnerIdOrderByIdAsc(userId);
    }

    public Optional<Task> findById(Long id, Long userId) {
        return tasks.findByIdAndOwnerId(id, userId);
    }

    public Task create(TaskRequest req, Long userId) {
        Task task = new Task(req.title().trim(), Boolean.TRUE.equals(req.completed()));
        task.setOwner(users.getReferenceById(userId));
        return tasks.save(task);
    }

    @Transactional
    public Optional<Task> update(Long id, TaskRequest req, Long userId) {
        return tasks.findByIdAndOwnerId(id, userId).map(task -> {
            task.setTitle(req.title().trim());
            if (req.completed() != null) {
                task.setCompleted(req.completed());
            }
            return task;
        });
    }

    @Transactional
    public boolean delete(Long id, Long userId) {
        return tasks.findByIdAndOwnerId(id, userId)
                .map(task -> { tasks.delete(task); return true; })
                .orElse(false);
    }
}