package com.example.demo_spring;

import com.example.demo_spring.Models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

// CRUD
public interface TodoRepository extends JpaRepository<Todo, Long> {

}
