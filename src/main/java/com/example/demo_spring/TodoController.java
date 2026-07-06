package com.example.demo_spring;

import com.example.demo_spring.Models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService todoservice;

    @GetMapping("/get")
    public String getTodo(){
        return "todo";
    }

//    @GetMapping("/{id}")
//    String printId(@PathVariable long id){
//        return "welcome to product "+id;
//    }

    @GetMapping("")
    String printIdPara(@RequestParam String username, @RequestParam String password){
        return "welcome "+username+" password: "+password;
    }

    @PostMapping("/create")
    ResponseEntity<Todo> createUser(@RequestBody Todo todo){
        return new ResponseEntity<>(todoservice.createTodo(todo), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<Todo> getTodoById(@PathVariable long id){
        return new ResponseEntity<>(todoservice.getTodoById(id),HttpStatus.OK);
    }


    @PutMapping("/{id}")
    String updateId(@PathVariable long id){
        return "updating id "+id;
    }

    @DeleteMapping("/{id}")
    String deleteId(@PathVariable long id){
        return "deleting id "+id;
    }
}
