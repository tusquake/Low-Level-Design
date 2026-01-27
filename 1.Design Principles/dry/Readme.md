# DRY Principle Overview

DRY stands for "Don't Repeat Yourself" - a principle that states every piece of knowledge or logic should have a single, unambiguous representation within a system. Avoid duplication of code, logic, and data.

## Core Concept

**"Every piece of knowledge must have a single, unambiguous, authoritative representation within a system."**

When you find yourself writing the same code or logic multiple times, extract it into a reusable component.

## Problem Statement

Duplicated code leads to maintenance nightmares. When you need to fix a bug or change logic, you must remember to update it in multiple places, increasing the chance of errors and inconsistencies.

### Bad Example (Code Duplication)

```java
public class UserService {
    public void createUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (user.getEmail().length() > 255) {
            throw new IllegalArgumentException("Email too long");
        }
        // Save user
    }
    
    public void updateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (user.getEmail().length() > 255) {
            throw new IllegalArgumentException("Email too long");
        }
        // Update user
    }
}
```

### Good Example (DRY Applied)

```java
public class UserService {
    
    public void createUser(User user) {
        validateEmail(user.getEmail());
        // Save user
    }
    
    public void updateUser(User user) {
        validateEmail(user.getEmail());
        // Update user
    }
    
    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (email.length() > 255) {
            throw new IllegalArgumentException("Email too long");
        }
    }
}
```

## Types of Duplication

### 1. Code Duplication

**Bad:**
```java
public class ReportGenerator {
    public void generatePDFReport() {
        System.out.println("Connecting to database...");
        // Get data
        System.out.println("Generating report...");
        // Generate PDF
        System.out.println("Report generated successfully");
    }
    
    public void generateExcelReport() {
        System.out.println("Connecting to database...");
        // Get data
        System.out.println("Generating report...");
        // Generate Excel
        System.out.println("Report generated successfully");
    }
}
```

**Good:**
```java
public class ReportGenerator {
    
    public void generatePDFReport() {
        Data data = fetchData();
        generateReport(data, "PDF");
    }
    
    public void generateExcelReport() {
        Data data = fetchData();
        generateReport(data, "Excel");
    }
    
    private Data fetchData() {
        System.out.println("Connecting to database...");
        // Get data
        return data;
    }
    
    private void generateReport(Data data, String format) {
        System.out.println("Generating report...");
        // Generate based on format
        System.out.println("Report generated successfully");
    }
}
```

### 2. Logic Duplication

**Bad:**
```java
public class PriceCalculator {
    public double calculateStandardPrice(Product product) {
        double price = product.getBasePrice();
        double tax = price * 0.1;
        double total = price + tax;
        return total;
    }
    
    public double calculatePremiumPrice(Product product) {
        double price = product.getBasePrice() * 1.5;  // Premium markup
        double tax = price * 0.1;
        double total = price + tax;
        return total;
    }
}
```

**Good:**
```java
public class PriceCalculator {
    private static final double TAX_RATE = 0.1;
    
    public double calculateStandardPrice(Product product) {
        return calculatePriceWithTax(product.getBasePrice());
    }
    
    public double calculatePremiumPrice(Product product) {
        double premiumPrice = product.getBasePrice() * 1.5;
        return calculatePriceWithTax(premiumPrice);
    }
    
    private double calculatePriceWithTax(double price) {
        double tax = price * TAX_RATE;
        return price + tax;
    }
}
```

### 3. Data Duplication

**Bad:**
```java
public class Configuration {
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 5432;
}

public class DatabaseService {
    private static final String DB_HOST = "localhost";  // Duplicated
    private static final int DB_PORT = 5432;            // Duplicated
}
```

**Good:**
```java
public class Configuration {
    public static final String DB_HOST = "localhost";
    public static final int DB_PORT = 5432;
}

public class DatabaseService {
    private String host = Configuration.DB_HOST;
    private int port = Configuration.DB_PORT;
}
```

### 4. String/Magic Number Duplication

**Bad:**
```java
public class OrderService {
    public void processOrder(Order order) {
        if (order.getStatus().equals("PENDING")) {
            // Process
        }
    }
    
    public void cancelOrder(Order order) {
        if (order.getStatus().equals("PENDING")) {  // Duplicated string
            // Cancel
        }
    }
}
```

**Good:**
```java
public class OrderService {
    private static final String STATUS_PENDING = "PENDING";
    
    public void processOrder(Order order) {
        if (order.getStatus().equals(STATUS_PENDING)) {
            // Process
        }
    }
    
    public void cancelOrder(Order order) {
        if (order.getStatus().equals(STATUS_PENDING)) {
            // Cancel
        }
    }
}
```

## Benefits

1. **Maintainability**: Change logic in one place
2. **Consistency**: Same behavior everywhere
3. **Reduced Bugs**: Fix a bug once, fixed everywhere
4. **Easier Testing**: Test logic in one place
5. **Readability**: Less code to read and understand
6. **Faster Development**: Reuse existing code

## DRY Techniques

### 1. Extract Method

```java
// Before
public void method1() {
    // 10 lines of calculation
}

public void method2() {
    // Same 10 lines of calculation
}

// After
private double calculate() {
    // 10 lines of calculation
}

public void method1() {
    double result = calculate();
}

public void method2() {
    double result = calculate();
}
```

### 2. Use Constants

```java
// Before
public void method1() {
    if (user.getAge() >= 18) { }
}

public void method2() {
    if (customer.getAge() >= 18) { }
}

// After
private static final int LEGAL_AGE = 18;

public void method1() {
    if (user.getAge() >= LEGAL_AGE) { }
}

public void method2() {
    if (customer.getAge() >= LEGAL_AGE) { }
}
```

### 3. Inheritance

```java
// Before: Duplicate validation logic
public class UserController {
    public void validateInput(String input) {
        // Validation logic
    }
}

public class ProductController {
    public void validateInput(String input) {
        // Same validation logic
    }
}

// After: Shared base class
public abstract class BaseController {
    protected void validateInput(String input) {
        // Validation logic in one place
    }
}

public class UserController extends BaseController { }
public class ProductController extends BaseController { }
```

### 4. Utility Classes

```java
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static String capitalize(String str) {
        // Capitalization logic
        return str;
    }
}

// Usage everywhere
if (StringUtils.isEmpty(name)) { }
String title = StringUtils.capitalize(input);
```

## When NOT to Apply DRY

### 1. Accidental Duplication

```java
// These look similar but represent different concepts
public void calculateEmployeeSalary() {
    double base = 50000;
    double bonus = base * 0.1;
    return base + bonus;
}

public void calculateContractorPayment() {
    double base = 50000;
    double bonus = base * 0.1;  // Different business rule
    return base + bonus;
}

// Don't merge - they might change independently
```

### 2. Premature Abstraction

```java
// Don't abstract on first duplication
// Wait for 3rd occurrence to see the pattern

// First time: Write the code
// Second time: Notice duplication, but wait
// Third time: Now abstract it
```

### 3. Different Contexts

```java
// These serve different purposes, don't combine
public class User {
    private String email;  // User's email
}

public class EmailLog {
    private String email;  // Recipient email
}
```

## Common Violations

### Copy-Paste Programming

```java
// Copied and modified slightly
public void sendWelcomeEmail() {
    Email email = new Email();
    email.setSubject("Welcome!");
    email.setFrom("no-reply@example.com");
    email.send();
}

public void sendResetPasswordEmail() {
    Email email = new Email();
    email.setSubject("Reset Password");
    email.setFrom("no-reply@example.com");  // Duplicated setup
    email.send();
}
```

**Fix:**
```java
private void sendEmail(String subject) {
    Email email = new Email();
    email.setSubject(subject);
    email.setFrom("no-reply@example.com");
    email.send();
}

public void sendWelcomeEmail() {
    sendEmail("Welcome!");
}

public void sendResetPasswordEmail() {
    sendEmail("Reset Password");
}
```

## DRY in Different Layers

### Database Schema
```sql
-- Bad: Duplicated columns
CREATE TABLE orders (customer_name, customer_email, customer_phone);
CREATE TABLE invoices (customer_name, customer_email, customer_phone);

-- Good: Normalized
CREATE TABLE customers (id, name, email, phone);
CREATE TABLE orders (id, customer_id);
CREATE TABLE invoices (id, customer_id);
```

### Configuration
```java
// Bad: Duplicated config
public class DevConfig {
    String url = "http://dev.example.com/api";
}

public class TestConfig {
    String url = "http://dev.example.com/api";  // Same as dev
}

// Good: Shared base
public abstract class BaseConfig {
    protected String baseUrl = "http://dev.example.com";
}

public class DevConfig extends BaseConfig {
    String url = baseUrl + "/api";
}
```

## Testing DRY Code

```java
@Test
public void testEmailValidation() {
    // Test the single validation method
    assertThrows(IllegalArgumentException.class, 
        () -> validateEmail(null));
    assertThrows(IllegalArgumentException.class, 
        () -> validateEmail("invalid"));
    assertDoesNotThrow(() -> validateEmail("test@example.com"));
}

// No need to test validation in multiple places
```

## Rule of Three

**Wait for the third occurrence before abstracting:**

1. First time: Write the code
2. Second time: Note the duplication, but don't act yet
3. Third time: Now refactor and extract common code

This prevents premature abstraction based on insufficient patterns.

## Quotes

> "Don't Repeat Yourself" - Andy Hunt & Dave Thomas (The Pragmatic Programmer)

> "Every piece of knowledge must have a single, unambiguous, authoritative representation" - Andy Hunt

> "Copy and paste is a design error" - David Parnas

## DRY vs WET

**WET** = "Write Everything Twice" or "We Enjoy Typing"
- Anti-pattern of DRY
- Results in duplicated code across the codebase

## Conclusion

The DRY principle is fundamental to writing maintainable code. By eliminating duplication, you create a single source of truth for each piece of logic or data. However, apply it judiciously - not all similar-looking code is duplication. Consider the context and business meaning before extracting common code.

**Remember: Don't repeat yourself, but don't prematurely abstract either. Find the right balance.**