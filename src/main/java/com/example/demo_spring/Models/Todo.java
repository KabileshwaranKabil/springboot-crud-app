package com.example.demo_spring.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.NotFound;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue
    Long id;
    @Size(min=10,max=29)
    @NotBlank
    @NotNull
    String title;

    @NotBlank
    @NotNull
    String description;
    Boolean isCompleted;

    @Email
    String email;

    @Pattern(regexp = "^[0-9]{10}$")
    String mobile;
}
