# Object Relationships & UML Connections

In Object-Oriented Design, relationships between classes and objects describe how they interact, depend, and connect with each other. Understanding these relationships is critical for creating clean UML diagrams and designing scalable low-level architectures.

There are **six primary relationships** in Object-Oriented Programming:

---

## 1. Association (General Relationship - "Knows-A")
Association is a structural relationship where two objects are connected but have **completely independent lifecycles**. One object just "knows" about the other.
-   **UML Notation**: Solid line with an open arrow pointing to the associated class (`───>`).
-   **Example**: A **Teacher** and a **Student**. 
-   A teacher can exist without students, and a student can exist without a specific teacher.
-   **Code representation**: Represented as a simple reference field.

## 2. Aggregation (Weak Association - "Has-A")
Aggregation is a special, one-way form of association representing a **whole-part** relationship, where the parts can exist independently of the whole.
-   **UML Notation**: Line with a hollow diamond at the owner/whole class (`──◇`).
-   **Example**: A **Department** and a **Professor**. 
-   If the department is shut down, the professor still exists and can join another department.
-   **Key**: "Whole" and "Part" have separate lifecycles.

## 3. Composition (Strong Association - "Part-of")
Composition is a strong "has-a" relationship where the **part cannot exist without the whole**. It represents a whole-part relationship with ownership and matching lifecycles.
-   **UML Notation**: Line with a filled (black) diamond at the owner/whole class (`──◆`).
-   **Example**: A **House** and a **Room**.
-   If the house is destroyed, the rooms are automatically destroyed.
-   **Key**: The lifecycle of the "Part" is strictly managed by the "Whole".

## 4. Generalization (Inheritance - "Is-A")
Generalization is the relationship between a class and its parent class (inheritance). The subclass inherits all behaviors and properties of the superclass.
-   **UML Notation**: Solid line with a hollow triangle/arrowhead pointing to the parent/superclass (`──▷`).
-   **Example**: A **Car** is a specialized type of **Vehicle**.
-   **Key**: Code reuse and subclassing. Represented via `extends` in Java.

## 5. Realization (Implementation - "Realizes/Implements")
Realization is a semantic relationship where a client class implements or **realizes** the contract (behavioral signature) defined by an interface.
-   **UML Notation**: Dashed line with a hollow triangle/arrowhead pointing to the interface (`- - ▷`).
-   **Example**: A **LaserPrinter** realizes (implements) the **Printer** interface.
-   **Key**: Abstract contracts. The interface specifies *what* must be done, and the class defines *how* it's done. Represented via `implements` in Java.

## 6. Dependency (Transient Relationship - "Uses-A")
Dependency is the weakest relationship where a class relies on another class temporarily (e.g., as a parameter in a method, a local variable, or a static method call).
-   **UML Notation**: Dashed line with an open arrow pointing to the dependency (`- - >`).
-   **Example**: An **OrderProcessor** depends on a **DatabaseConnection** to save an order.
-   **Key**: A change in the dependency class may force changes in the dependent class.

---

## Comparison Table

| Relationship | Type | Lifecycle | Strength | UML Symbol | Java Keyword / Mechanism |
|--------------|------|-----------|----------|------------|---------------------------|
| **Dependency** | Uses-A (Transient) | N/A (Temporary) | Weakest | `- - >` | Method parameter, local variable |
| **Association** | Knows-A | Independent | Weak | `───>` | Instance field reference |
| **Aggregation** | Has-A (Whole-Part) | Independent | Medium | `──◇` | Instance field, passed via constructor |
| **Composition** | Part-Of (Whole-Part) | Dependent | Strong | `──◆` | Instance field, created inside owner |
| **Realization** | Implements | Contract-dependent | Strong | `- - ▷` | `implements` (Class -> Interface) |
| **Generalization** | Is-A | Superclass-dependent | Strongest | `──▷` | `extends` (Class -> Class / Interface -> Interface) |

---

## Relation with Spring Boot

These relationships directly correspond to enterprise application designs:

### 1. **Realization in Service Layer**
Realization is the default pattern for Spring Boot services to promote loose coupling and easy mocking during unit testing.
```java
// Interface (Contract)
public interface UserService {
    User getUserById(Long id);
}

// Realization (Implementation)
@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(Long id) {
        return new User();
    }
}
```

### 2. **Dependency Injection (Dependency)**
Spring's Core Container injects dependencies. A controller depends on a service to execute business logic.
```java
@RestController
public class UserController {
    // UserController depends on UserService (Dependency)
    private final UserService userService; 

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

### 3. **JPA Mappings (Association / Aggregation / Composition)**
-   **Association/Aggregation**: `@ManyToOne` or `@ManyToMany` where entities can exist independently.
-   **Composition**: `@ElementCollection` or `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)` where children (e.g., `OrderItem`) are deleted when the parent (`Order`) is deleted.
```java
@Entity
public class Order {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items; // Composition: items cannot exist without Order
}
```
```java
@Entity
public class Employee {
    @ManyToOne
    private Department department; // Aggregation: Employee can exist if Department is deleted
}
```
