package com.example.demo_spring.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/hello")
    String sayHello(){
        return "I am Kabileshwaran";
    }
}
