# Object Relationships (Association, Aggregation, Composition)

In Object-Oriented Design, relationships between objects describe how they interact and depend on each other.

## 1. Association (General Relationship)
Association is a general term for a relationship where two objects are connected but have **independent lifecycles**.
-   **Example**: A **Teacher** and a **Student**. 
-   A teacher can exist without students, and a student can exist without a specific teacher.
-   Represented as a simple reference field.

## 2. Aggregation (Weak Association - "Has-A")
Aggregation is a special form of association where there is a **whole-part** relationship, but the parts can still exist independently.
-   **Example**: A **Department** and a **Professor**. 
-   If the department is closed, the professor still exists.
-   **Key**: "Whole" and "Part" have separate lifecycles.

## 3. Composition (Strong Association - "Part-of")
Composition is a strong "has-a" relationship where the **part cannot exist without the whole**.
-   **Example**: A **House** and a **Room**.
-   If the house is destroyed, the rooms are also destroyed.
-   **Key**: The lifecycle of the "Part" is strictly managed by the "Whole".

---

## Comparison Table

| Type | Relationship | Lifecycle | Strength |
|------|--------------|-----------|----------|
| Association | Peer-to-peer | Independent | Weak |
| Aggregation | Whole-Part | Independent | Medium |
| Composition | Whole-Part | Dependent | Strong |

---

## Relation with Spring Boot

Object relationships are directly mapped to database relationships using Hibernate/JPA:

### 1. **@OneToOne (Association/Composition)**
A user and their profile. If the profile is deleted when the user is deleted, it's composition.

### 2. **@OneToMany / @ManyToOne (Aggregation)**
A department and its employees. Usually, if a department is deleted, we don't delete the employees (Aggregation).
```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;
```

### 3. **@Embedded and @Embeddable (Composition)**
A `User` entity and an `Address` class. The address doesn't have its own ID and exists only as part of the User.
```java
@Embedded
private Address address;
```

### 4. **Cascade Types**
`CascadeType.ALL` vs `CascadeType.REMOVE` is often used to enforce **Composition** at the database level (e.g., deleting an Order also deletes all OrderItems).
