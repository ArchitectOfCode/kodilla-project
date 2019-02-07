package com.crud.tasks.repository;

import com.crud.tasks.domain.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {
    @Override
    List<Task> findAll();   //currently findAllById



    List<Task> findAllById();



    @Override
    Task save(Task task);   //currently saveAll

    Optional<Task> findById(Long id);

    void deleteById(Long Id);
}
