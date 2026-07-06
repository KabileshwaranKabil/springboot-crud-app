package com.example.demo_spring.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue
    Long id;
    String title;
    String description;
    Boolean isCompleted;
}
