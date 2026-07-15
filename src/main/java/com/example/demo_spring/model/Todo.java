package com.example.demo_spring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue
    Long id;
    @Size(min=10,max=29)
    @NotBlank
    @NotNull

    @Schema(name ="title", example = "complete spring boot")
    String title;
    Boolean isCompleted;
//
//    @Email
//    String email;
//
//    @Pattern(regexp = "^[0-9]{10}$")
//    String mobile;
}
