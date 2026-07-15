package com.example.demo_spring.repository;

import com.example.demo_spring.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD
public interface TodoRepository extends JpaRepository<Todo, Long> {

}
