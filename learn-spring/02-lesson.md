### 1. Introduction

**What this lesson covers**  
- **Problems Java developers faced before Spring**: tight coupling, heavy boilerplate, hard-to-test code, XML explosion, and complex transaction and resource management.  
- **What IoC (Inversion of Control) and DI (Dependency Injection) are**: core ideas Spring introduced to solve those problems.  
- **Why these concepts matter**: they make code modular, testable, maintainable, and easier to evolve in real projects.

**Why this lesson exists**  
Understanding the pain points that led to Spring gives context for *why* IoC/DI are designed the way they are and *how* they change the way you structure applications.

---

### 2. Real Life Analogy

**Analogy: Restaurant Kitchen vs Home Cooking**  
- **Before Spring (Home Cooking)**: Every chef must fetch ingredients, clean, cook, and serve. Each chef repeats the same setup code.  
- **With Spring (Restaurant Kitchen)**: A kitchen manager prepares ingredients and tools, and chefs request what they need. Chefs focus on cooking (business logic) while the kitchen manager (IoC container) provides dependencies.

This shows how IoC moves responsibility for wiring and provisioning away from individual classes to a central manager.

---

### 3. Visual Explanation

**Problem before Spring (tight coupling)**

```
Client
 ↓
Controller (creates Service)
    new ServiceImpl(new RepositoryImpl())
 ↓
Service (creates Repository)
    new RepositoryImpl()
 ↓
Repository (creates DB connection)
    new Connection(...)
 ↓
Database
```

**With IoC / DI (loose coupling)**

```
Client
 ↓
Controller (depends on Service interface)
 ↓
Service (depends on Repository interface)
 ↓
Repository (depends on DataSource)
 ↓
IoC Container (creates and injects concrete implementations)
 ↓
Database
```

---

### 4. Internal Working (Under the Hood)

**Problems before Spring — what actually happened internally in old-style apps**
- **Manual object creation**: Classes used `new` to create dependencies. This scattered construction logic across the codebase.  
- **Hard-coded implementations**: Code depended on concrete classes rather than interfaces, making swapping implementations difficult.  
- **Configuration sprawl**: XML files grew large and were hard to maintain.  
- **Testing difficulty**: Unit tests had to create real dependencies (databases, network clients) or complex test scaffolding.  
- **Lifecycle and resource management**: Developers manually opened/closed connections and managed transactions, leading to bugs and leaks.

**What IoC/DI change internally**
- **IoC container (ApplicationContext)** becomes the *creator and manager* of objects (beans).  
- **Bean definitions** describe how to create objects; the container reads these definitions at startup.  
- **Dependency graph** is built by the container: it inspects constructors, fields, and setters to determine what each bean needs.  
- **Instantiation order** is resolved by the container to satisfy dependencies.  
- **Injection** happens via reflection: the container calls constructors or sets fields to provide dependencies.  
- **Lifecycle callbacks** are invoked by the container (`@PostConstruct`, `@PreDestroy`, `BeanPostProcessor` hooks).  
- **At runtime**, when a request arrives, controllers and services are already wired; the container does not create them per request (unless scope says otherwise).

**Which objects and components are involved**
- **`ApplicationContext` / `BeanFactory`**: holds bean definitions and instances.  
- **`BeanDefinition`**: metadata describing a bean.  
- **`ClassPathBeanDefinitionScanner`**: finds annotated classes.  
- **`AutowiredAnnotationBeanPostProcessor`**: processes `@Autowired` and performs injection.  
- **`ConstructorResolver`**: chooses which constructor to call for instantiation.  
- **`BeanPostProcessor`**: allows custom logic before/after initialization.  

**What happens during startup**
1. Classpath scanning finds candidate classes.  
2. Bean definitions are registered.  
3. `ApplicationContext` resolves dependencies and instantiates singleton beans.  
4. Post-processors run and lifecycle callbacks are invoked.  
5. Application is ready to serve requests.

**What happens during runtime**
- Beans are reused according to scope.  
- When a request arrives, the web layer calls into already-initialized beans.  
- Transactional proxies and AOP interceptors may wrap beans to add behavior like transactions or security.

---

### 5. Syntax (Core DI Mechanisms in Spring)

**Common ways to declare dependencies**

1. **Constructor injection (recommended)**

```java
@Service
public class OrderService {
    private final PaymentGateway paymentGateway;

    public OrderService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
```

- **Why**: dependencies are explicit and final; easier to test.

2. **Setter injection**

```java
@Component
public class NotificationService {
    private EmailClient emailClient;

    @Autowired
    public void setEmailClient(EmailClient emailClient) {
        this.emailClient = emailClient;
    }
}
```

- **Why**: useful for optional dependencies.

3. **Field injection (not recommended)**

```java
@Component
public class BadExample {
    @Autowired
    private SomeDependency dep;
}
```

- **Why not**: hides dependencies, harder to test.

**Key annotations and meaning**

- `@Component` — marks a class as a bean candidate.  
- `@Service`, `@Repository`, `@Controller` — semantic specializations of `@Component`.  
- `@Autowired` — instructs Spring to inject a dependency by type.  
- `@Qualifier("name")` — disambiguates when multiple beans of same type exist.  
- `@Primary` — marks a bean as the default when multiple candidates exist.  
- `@Configuration` + `@Bean` — programmatic bean definitions.

---

### 6. Code Example — Before vs After

**Before Spring (manual wiring)**

```java
public class UserController {
    private final UserService userService;

    public UserController() {
        UserRepository repo = new UserRepository(new JdbcConnection(...));
        this.userService = new UserService(repo);
    }

    public List<User> listUsers() {
        return userService.findAll();
    }
}
```

**Problems**: `UserController` creates `UserService` and `UserRepository`, tightly coupling layers and making testing hard.

**With Spring (IoC / DI)**

```java
// UserRepository.java
@Repository
public class UserRepository {
    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<User> findAll() {
        // use dataSource to query DB
    }
}

// UserService.java
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}

// UserController.java
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> listUsers() {
        return userService.findAll();
    }
}
```

**Line-by-line explanation highlights**
- `@Repository`, `@Service`, `@RestController` register beans.  
- Constructors declare dependencies; Spring injects concrete instances automatically.  
- No `new` in controllers or services; wiring is centralized in the container.

---

### 7. Request Flow (with DI in place)

```
Client
 ↓
Embedded Server (Tomcat)
 ↓
DispatcherServlet
 ↓
HandlerMapping finds controller bean (already created by IoC)
 ↓
Controller method invoked
 ↓
Controller calls Service (injected)
 ↓
Service calls Repository (injected)
 ↓
Repository uses DataSource (injected)
 ↓
Database
```

**Key point**: the IoC container has already created and wired the controller, service, repository, and data source before any request arrives.

---

### 8. Under the Hood Code Execution (Detailed)

**How Spring finds and injects dependencies**
- **Component scanning**: `ClassPathBeanDefinitionScanner` inspects classes for stereotype annotations.  
- **BeanDefinition creation**: For each candidate, Spring creates a `BeanDefinition` describing the bean class, scope, and dependencies.  
- **Dependency resolution**: When instantiating a bean, Spring examines constructors and parameters. If a constructor has parameters, Spring resolves each parameter by type and qualifiers.  
- **Constructor selection**: `ConstructorResolver` chooses the best constructor (e.g., the one annotated with `@Autowired` or the single constructor if only one exists).  
- **Instantiation**: Spring uses reflection to call the chosen constructor and create the instance.  
- **Injection**: For setter or field injection, `AutowiredAnnotationBeanPostProcessor` sets fields or calls setters after instantiation.  
- **Proxying for AOP**: If a bean requires transactional behavior, Spring creates a proxy (JDK dynamic proxy or CGLIB) that wraps the bean and intercepts method calls to apply advice.  
- **Lifecycle hooks**: `BeanPostProcessor` implementations run `postProcessBeforeInitialization` and `postProcessAfterInitialization`. `@PostConstruct` methods are invoked after initialization.

**What happens when multiple beans of same type exist**
- Spring tries to match by **type** first. If multiple candidates exist, it looks for **@Primary** or **@Qualifier**. If ambiguity remains, startup fails with `NoUniqueBeanDefinitionException`.

---

### 9. Common Mistakes

- **Using field injection**  
  *Why it fails:* hides dependencies and makes unit testing harder.  
  *Fix:* use constructor injection.

- **Relying on default package scanning location**  
  *Why it fails:* main application class placed in subpackage may not scan all components.  
  *Fix:* place main class at root package or use `@ComponentScan`.

- **Injecting concrete classes instead of interfaces**  
  *Why it fails:* reduces flexibility and makes swapping implementations harder.  
  *Fix:* depend on interfaces.

- **Expecting `@Autowired` to work on private constructors without proper configuration**  
  *Why it fails:* Spring needs to be able to call the constructor; prefer public constructors or single-constructor classes.

- **Placing `@Transactional` on private methods**  
  *Why it fails:* Spring AOP proxies only intercept public (or at least non-private) methods called from outside the bean.  
  *Fix:* put `@Transactional` on public service methods.

---

### 10. Best Practices

- **Prefer constructor injection** for required dependencies.  
- **Program to interfaces** not implementations.  
- **Use `@Configuration` + `@Bean`** for wiring third-party libraries.  
- **Keep beans stateless** where possible; avoid mutable shared state.  
- **Use `@Qualifier` or `@Primary`** to resolve multiple bean candidates explicitly.  
- **Limit scope**: use singleton scope for stateless services; use request or prototype only when necessary.  
- **Write unit tests** by instantiating classes with test doubles (mocks) passed into constructors.

---

### 11. Exercises

**Easy (5)**  
1. Define IoC in one sentence.  
2. List three problems that manual `new`-based wiring causes.  
3. Convert a class that uses field injection to constructor injection.  
4. Explain what `@Component` does.  
5. Name the Spring component responsible for scanning classes.

**Medium (3)**  
1. Create `PaymentService` and `PaymentController` using constructor injection; explain how Spring wires them.  
2. Explain how Spring chooses which constructor to use when multiple constructors exist.  
3. Describe what happens if two beans of the same type exist and no `@Primary` or `@Qualifier` is provided.

**Challenge (1)**  
- Build a small app with two implementations of `NotificationSender` (`EmailSender`, `SmsSender`). Configure Spring so `EmailSender` is injected by default but allow switching to `SmsSender` using `@Qualifier`. Explain the bean selection process and show startup logs that indicate which bean was chosen.

---

### 12. Mini Project (Refine the Student Registry)

**Goal**: Apply IoC/DI to the Student Registry from Lesson 1.  
**Steps**  
1. Create interfaces: `StudentRepository`, `StudentService`.  
2. Provide an in-memory implementation `InMemoryStudentRepository` annotated with `@Repository`.  
3. Implement `StudentServiceImpl` annotated with `@Service` and inject repository via constructor.  
4. Implement `StudentController` annotated with `@RestController` and inject service via constructor.  
5. Add a `DataLoader` bean (`@Component`) that populates initial students at startup using `@PostConstruct`.

**Why this helps**: you will see how the container creates beans, injects dependencies, and how startup order and lifecycle hooks work.

---

### 13. Interview Questions

1. **What problem does IoC solve**  
   *Answer:* IoC moves responsibility for creating and wiring objects from application code to a container, reducing coupling and improving testability.

2. **What is Dependency Injection**  
   *Answer:* DI is a pattern where an object's dependencies are provided by an external entity rather than the object creating them itself.

3. **Constructor vs Field Injection**  
   *Answer:* Constructor injection makes dependencies explicit and supports immutability; field injection hides dependencies and complicates testing.

4. **How does Spring resolve which bean to inject when multiple candidates exist**  
   *Answer:* Spring matches by type, then looks for `@Primary`, `@Qualifier`, or bean name; if ambiguity remains, it throws `NoUniqueBeanDefinitionException`.

5. **What is a BeanPostProcessor**  
   *Answer:* A `BeanPostProcessor` allows custom modification of new bean instances before and after initialization.

---

### 14. Summary

- **Before Spring**, Java apps suffered from tight coupling, manual wiring, XML complexity, and testing pain.  
- **IoC and DI** shift object creation and wiring to the container, producing loosely coupled, testable, and maintainable code.  
- **Spring’s container** builds a dependency graph at startup, instantiates beans, injects dependencies, and manages lifecycle callbacks.  
- **Practical rule**: prefer constructor injection, program to interfaces, and keep beans stateless.
