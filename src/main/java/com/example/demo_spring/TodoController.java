package com.example.demo_spring;

import com.example.demo_spring.Models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService todoservice;

//    @GetMapping("/{id}")
//    String printId(@PathVariable long id){
//        return "welcome to product "+id;
//    }

    @PostMapping("/create")
    ResponseEntity<Todo> createUser(@RequestBody Todo todo){

        return new ResponseEntity<>(todoservice.createTodo(todo), HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<Todo>> getTodos(){
        return new ResponseEntity<>(todoservice.getTodos(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    ResponseEntity<Todo> getTodoById(@PathVariable long id){
        try{
            Todo createdTodo = todoservice.getTodoById(id);
            return new ResponseEntity<>(createdTodo,HttpStatus.OK);
        }catch(RuntimeException exception) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    ResponseEntity<Todo> updateTodoById(@RequestBody Todo  todo){
        return new ResponseEntity<>(todoservice.updateTodo(todo),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteId(@PathVariable long id){
        todoservice.deleteTodoById(id);
    }
}
