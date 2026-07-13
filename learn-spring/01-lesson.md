### 1. Introduction

**What is Spring**  
**Spring** is a lightweight, open-source framework for building Java applications. It provides a set of tools and libraries that simplify common backend tasks: dependency management, configuration, transaction handling, data access, and more.

**Why was it created**  
Spring was created to solve complexity and rigidity in enterprise Java development that arose from early Java EE (then J2EE) patterns. It aimed to make applications easier to write, test, and maintain by promoting loose coupling and modular design.

**What problem does it solve**  
- Reduces boilerplate code for wiring components.  
- Replaces heavy, XML-centric configuration with simpler, programmatic or annotation-based configuration.  
- Makes testing easier by enabling dependency injection and inversion of control.  
- Provides consistent abstractions for transactions, data access, and web layers.

**Why companies use it**  
Companies use Spring because it speeds development, improves maintainability, supports large-scale systems, and integrates with many technologies (JPA, JDBC, messaging, security, cloud). It’s battle-tested and widely adopted.

**Why you should learn it**  
Learning Spring gives you the practical skills to build professional Java backends, understand modern enterprise patterns, and be internship-ready for backend roles.

---

### 2. Real Life Analogy

**Analogy: A Library System**  
- **Library building** = Application container (Spring)  
- **Librarian** = Spring IoC container (manages and provides books/components)  
- **Books** = Beans (objects managed by Spring)  
- **Readers** = Parts of your application that request beans (controllers, services)  
Spring is like a library where the librarian (IoC) gives the right book (bean) to whoever asks, instead of each reader searching the shelves and creating books themselves.

---

### 3. Visual Explanation

```
Client (Browser / Mobile)
        ↓
   HTTP Request
        ↓
  Web Layer (Spring MVC)
        ↓
 Controller (uses Services)
        ↓
 Service Layer (business logic)
        ↓
 Repository Layer (data access)
        ↓
     Database
```

And for core Spring runtime:

```
Application Startup
↓
Classpath Scanning
↓
Bean Definitions Created
↓
ApplicationContext (IoC Container) initialized
↓
Beans instantiated and injected
↓
Application ready to accept requests
```

---

### 4. Internal Working Under the Hood

This section explains what Spring does internally when you build and run an application.

**Startup and Bean Discovery**
- **Classpath scanning**: Spring scans packages (based on configuration or `@ComponentScan`) to find classes annotated with stereotypes like `@Component`, `@Service`, `@Repository`, `@Controller`.
- **Bean definition creation**: For each discovered class, Spring creates a *bean definition* (metadata describing how to create the bean).
- **ApplicationContext initialization**: The `ApplicationContext` (the IoC container) is created. It reads bean definitions and prepares to instantiate beans.
- **Bean instantiation and dependency injection**:
  - Spring resolves dependencies declared in constructors, fields, or setters.
  - It creates bean instances in the correct order (respecting dependencies).
  - It injects dependencies (constructor injection preferred).
- **Lifecycle callbacks**: If beans implement lifecycle interfaces (`InitializingBean`, `DisposableBean`) or have `@PostConstruct`/`@PreDestroy`, Spring calls those at appropriate times.

**Which objects get created**
- `BeanDefinition` objects (metadata)
- Bean instances (your classes)
- `ApplicationContext` (e.g., `AnnotationConfigApplicationContext` or `SpringApplication`’s context)
- Infrastructure beans (e.g., `Environment`, `ResourceLoader`, `BeanFactoryPostProcessor`)

**Which methods/annotations are processed**
- Annotations like `@Component`, `@Configuration`, `@Bean`, `@Autowired`, `@Controller` are processed by Spring’s annotation processors during startup.
- `@Configuration` classes are proxied to ensure `@Bean` methods behave as singletons.

**How dependencies are managed**
- Spring uses the IoC principle: objects do not create their dependencies; the container injects them.
- Injection can be by constructor, setter, or field (constructor injection is recommended).

**What happens when a request comes**
1. Embedded server (Tomcat/Jetty) receives HTTP request.
2. `DispatcherServlet` (Spring MVC front controller) receives request.
3. `HandlerMapping` finds the controller method mapped to the URL.
4. `HandlerAdapter` invokes the controller method.
5. Controller calls service layer (beans already injected).
6. Service calls repository (data access).
7. Response is returned to `DispatcherServlet`, converted to JSON by `HttpMessageConverters` (Jackson), and sent back to client.

---

### 5. Syntax (Core Concepts and Common Annotations)

**Key annotations and what they mean**

- `@Component` — marks a class as a Spring-managed component. Package: `org.springframework.stereotype`.  
- `@Service` — specialization of `@Component` for service-layer classes. Package: `org.springframework.stereotype`.  
- `@Repository` — specialization for data access layer; translates persistence exceptions. Package: `org.springframework.stereotype`.  
- `@Controller` — marks a web controller for MVC. Package: `org.springframework.stereotype`.  
- `@RestController` — shorthand for `@Controller` + `@ResponseBody` (returns JSON). Package: `org.springframework.web.bind.annotation`.  
- `@Autowired` — tells Spring to inject a dependency. Package: `org.springframework.beans.factory.annotation`.  
- `@Configuration` — marks a class that declares `@Bean` methods. Package: `org.springframework.context.annotation`.  
- `@Bean` — declares a bean method inside `@Configuration`. Package: `org.springframework.context.annotation`.  
- `@ComponentScan` — tells Spring which packages to scan for components. Package: `org.springframework.context.annotation`.  

**Why these exist**  
Annotations provide declarative metadata so Spring can discover and manage your classes automatically, reducing manual wiring.

---

### 6. Code Example

**Small example: a simple service and controller**

```java
// package com.example.demo.service;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
```

```java
// package com.example.demo.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final GreetingService greetingService;

    // Constructor injection: preferred for immutability and testability
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "Student") String name) {
        return greetingService.greet(name);
    }
}
```

**Explain every important line**
- `@Service` — registers `GreetingService` as a bean in the ApplicationContext.
- `public String greet(String name)` — business logic method.
- `@RestController` — registers controller and ensures return values are written to HTTP response body.
- `private final GreetingService greetingService;` — dependency reference.
- Constructor — Spring will inject `GreetingService` automatically because it’s a bean.
- `@GetMapping("/greet")` — maps HTTP GET `/greet` to this method.
- `@RequestParam` — binds query parameter `name` to method parameter.

**How Spring wires this**
- At startup, Spring scans packages, finds `GreetingService` and `GreetingController`, creates bean definitions, instantiates `GreetingService`, then instantiates `GreetingController` injecting the `GreetingService` instance into its constructor.

---

### 7. Request Flow (Detailed Lifecycle for the Example)

1. **Client** sends `GET /greet?name=Kabi`.
2. **Embedded Tomcat** receives request and forwards to Spring.
3. **DispatcherServlet** receives request.
4. **HandlerMapping** finds `GreetingController#greet`.
5. **HandlerAdapter** invokes the method, resolving `@RequestParam`.
6. `GreetingController` calls `GreetingService.greet("Kabi")`.
7. `GreetingService` returns `"Hello, Kabi!"`.
8. `DispatcherServlet` uses `HttpMessageConverter` (StringHttpMessageConverter) to write response.
9. **Client** receives HTTP 200 with body `Hello, Kabi!`.

---

### 8. Under the Hood Code Execution

**What happens when `@RestController` is used**
- `@RestController` is meta-annotated with `@Controller` and `@ResponseBody`.  
- During component scanning, Spring finds the class and registers a bean definition.  
- `DispatcherServlet` uses `RequestMappingHandlerMapping` to detect `@GetMapping` and other request mappings and registers them in handler mappings.  
- When a request matches, `RequestMappingHandlerAdapter` invokes the method and uses `HandlerMethodArgumentResolver`s to resolve parameters (e.g., `@RequestParam`) and `HttpMessageConverters` to convert return values to HTTP responses.

**How dependency injection happens**
- Spring builds a dependency graph from bean definitions.
- For constructor injection, Spring finds the constructor and resolves required beans by type (and qualifier if needed).
- Spring instantiates beans in an order that satisfies dependencies, using reflection to call constructors.

**Which Spring components are responsible**
- `AnnotationConfigApplicationContext` or `SpringApplication` bootstrapper — starts the context.
- `ClassPathBeanDefinitionScanner` — scans for annotated classes.
- `BeanFactoryPostProcessor` and `BeanPostProcessor` — allow customization of bean definitions and instances.
- `DispatcherServlet`, `HandlerMapping`, `HandlerAdapter` — handle web requests.

---

### 9. Common Mistakes

- **Using field injection (`@Autowired` on fields)**  
  *Why it’s bad:* Harder to test, hides dependencies, breaks immutability.  
  *Avoid by:* Using constructor injection.

- **Not specifying component scan base package**  
  *Why it happens:* Default scan may not include your packages.  
  *Avoid by:* Placing main application class at root package or using `@ComponentScan`.

- **Mixing configuration styles carelessly**  
  *Why it happens:* Using XML and annotations together can confuse startup order.  
  *Avoid by:* Prefer annotation-based configuration for new projects.

- **Overusing `@Transactional` at wrong layer**  
  *Why it happens:* Putting transactions on controllers or private methods won’t work as expected.  
  *Avoid by:* Apply `@Transactional` on public service methods.

---

### 10. Best Practices

- **Prefer constructor injection** for required dependencies.  
- **Keep controllers thin** — controllers should orchestrate, not contain business logic.  
- **Put business logic in services** and data access in repositories.  
- **Use `@Repository` for DAOs** to get exception translation.  
- **Use `@Configuration` + `@Bean` for third-party library wiring** when you can’t annotate classes.  
- **Organize packages by feature** (e.g., `student`, `course`) rather than by layer only, for larger projects.  
- **Write unit tests** for services using mocks; write integration tests for repositories.

---

### 11. Exercises

**Easy (5)**  
1. Explain in one sentence what IoC means.  
2. What does `@Component` do?  
3. Convert a simple class into a Spring bean using an annotation.  
4. Why is constructor injection preferred over field injection?  
5. Name the Spring component that receives HTTP requests.

**Medium (3)**  
1. Create a `UserService` bean and a `UserController` that returns a list of users (in-memory). Explain each line.  
2. Explain what `ApplicationContext` is and how you would retrieve a bean programmatically.  
3. Describe what happens if two beans of the same type exist and Spring needs to inject one by type.

**Challenge (1)**  
- Build a small Spring application (no database) with a `Book` entity, `BookService`, and `BookController`. Implement endpoints to add a book and list all books. Use constructor injection and explain the startup logs that show bean creation.

---

### 12. Mini Project Idea

**Mini Project: Simple Student Registry (no DB yet)**  
- **Goal:** Build a REST API to add and list students stored in an in-memory list.  
- **Layers:** `StudentController`, `StudentService`, `StudentRepository` (in-memory).  
- **Features:** Add student, list students, basic validation (non-empty name).  
- **Why:** This lets you practice controllers, services, dependency injection, and request mapping before adding persistence.

---

### 13. Interview Questions

1. **What is Spring and why use it?**  
   *Answer:* Spring is a framework for building Java applications that provides IoC, DI, and many modules to simplify enterprise development.

2. **Explain IoC and DI.**  
   *Answer:* IoC (Inversion of Control) means the framework controls object creation and wiring. DI (Dependency Injection) is a pattern where dependencies are provided to objects rather than created by them.

3. **What is `ApplicationContext`?**  
   *Answer:* It’s the Spring IoC container that holds bean definitions and manages bean lifecycle.

4. **Why prefer constructor injection?**  
   *Answer:* It makes dependencies explicit, supports immutability, and is easier to test.

5. **What is `@RestController` vs `@Controller`?**  
   *Answer:* `@RestController` combines `@Controller` and `@ResponseBody` to return data (usually JSON) directly; `@Controller` is used for MVC views.

---

### 14. Summary

- **Spring** is a powerful framework that simplifies Java backend development by providing IoC, DI, and many abstractions for web, data, and transactions.  
- **Key idea:** Let the container manage object creation and wiring so your code focuses on business logic.  
- **Next step:** In the next lesson we will cover **Problems before Spring** and **IoC and Dependency Injection** in depth, with code-level internals and hands-on exercises.
