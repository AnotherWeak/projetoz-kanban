package com.projeto.service;

import com.example.projetos.model.Task;
import com.example.projetos.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.projetos.model.Task.Status.IN_PROGRESS;
import static com.example.projetos.model.Task.Status.TO_DO;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        task.setStatus(TO_DO);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByStatus(Task.Status status) {
        return taskRepository.findAllByStatus(status);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setPriority(updatedTask.getPriority());
            task.setDeadline(updatedTask.getDeadline());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task moveTask(Long id) {
        return taskRepository.acharPorID(id).map(task -> {
            switch (task.getStatus()) {
                case TO_DO -> task.setStatus(IN_PROGRESS);
                case IN_PROGRESS -> task.setStatus(Task.Status.DONE);
                default -> throw new IllegalStateException("Tarefa já completada");
            }
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }
}
