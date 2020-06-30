package com.crud.service;

import com.crud.domain.Task;
import com.crud.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class DbService {
    @Autowired
    private TaskRepository repository;

    public List<Task> getAllTasks(){
        return repository.findAll();
    }

    public Optional<Task> getTaskById(Long id){
        return repository.findById(id);
    }

    public Task saveTask(final Task task){
        return repository.save(task);
    }

    public void deleteTaskByID(Long taskId) {
        repository.deleteById(taskId);
    }
}
