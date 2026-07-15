# What is Spring Security?
- Think of **Spring Security** as the **security guard** of your application.  
- Just like a guard checks IDs before letting people into a building, Spring Security checks **who is trying to access your app** and **what they’re allowed to do**.  
- It’s not just about login forms — it also protects against attacks like **CSRF** (fake requests), **session hijacking**, and **brute force login attempts**.  
- Works seamlessly with **Spring Boot**: once you add the dependency, it automatically locks down your app.

**Code Setup:**
```xml
<!-- Add in pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

# Authentication vs Authorization
- **Authentication** = “Who are you?”  
  - Example: Showing your **passport** at the airport.  
  - In Spring Security: username + password, or token (JWT/OAuth2).  
- **Authorization** = “What can you do?”  
  - Example: Even if you’re inside the airport, you can’t enter the **pilot’s cockpit** unless you’re authorized.  
  - In Spring Security: roles like `ROLE_USER`, `ROLE_ADMIN`.

**Code Example:**
```java
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/user/**").hasRole("USER")
    .anyRequest().authenticated()
);
```

---

# How Spring Security Works Internally
- Imagine a **security checkpoint** with multiple gates:
  1. **Filter Chain**: Every request passes through filters (like guards checking different things).
     - Example: `UsernamePasswordAuthenticationFilter` checks login credentials.
     - `CsrfFilter` checks if requests are safe.
  2. **AuthenticationManager**: The **chief guard** who decides if credentials are valid.
  3. **AuthenticationProvider**: Specialized guards (DB, LDAP, JWT) that know how to validate.
  4. **UserDetailsService**: The **database clerk** who fetches user info.
  5. **PasswordEncoder**: Ensures passwords are stored securely (like hashing with BCrypt).

**Code Example:**
```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails user = User.withUsername("kabilesh")
                           .password(passwordEncoder().encode("12345"))
                           .roles("USER")
                           .build();
    return new InMemoryUserDetailsManager(user);
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

# Default Security
- When you add Spring Security:
  - All endpoints are locked.
  - Default login form appears.
  - Default user = `user` with a random password (shown in console logs).
- This is like a **new building with a guard at every door** — you can’t enter until you show credentials.

**Code Example (Custom Security):**
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // disable CSRF for APIs
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**").permitAll()
            .anyRequest().authenticated())
        .httpBasic(); // simple login with username/password
    return http.build();
}
```

---

📌 **Analogy Recap:**
- Spring Security = Security guard.  
- Authentication = Showing ID.  
- Authorization = Accessing restricted areas.  
- Filter Chain = Multiple checkpoints.  
- Default Security = Locked building until configured.

--- 
