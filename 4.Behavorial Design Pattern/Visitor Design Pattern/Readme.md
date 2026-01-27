# Visitor Design Pattern - Simple Guide

## What is Visitor Pattern?

Visitor pattern lets you add new operations to existing classes **without modifying them**. It separates operations from the objects they operate on.

**Think of it as:** Tax inspector visiting different businesses. Each business accepts the visitor, but the inspector performs different calculations based on business type.

---

## Real-World Analogy: Home Inspector

**Scenario:** Home inspector visits different rooms to perform inspections.

```
Inspector visits:
- Kitchen → Check appliances, plumbing
- Bedroom → Check electrical outlets, windows
- Bathroom → Check water pressure, ventilation

Each room "accepts" the inspector.
Inspector performs different checks per room type.
Rooms don't need to know how to inspect themselves!
```

**Key Point:** Adding new inspection types (fire safety, pest control) doesn't require changing room classes!

---

## Problem Without Visitor

```java
// Adding operations requires modifying each class
class Circle {
    void draw() { }
    void calculateArea() { }
    void exportToXML() { }      // New operation - modify class
    void exportToJSON() { }     // New operation - modify class
    void exportToPDF() { }      // New operation - modify class
    // Every new operation needs code change in Circle!
}

class Rectangle {
    void draw() { }
    void calculateArea() { }
    void exportToXML() { }      // Duplicate code
    void exportToJSON() { }     // Duplicate code
    void exportToPDF() { }      // Duplicate code
}

class Triangle {
    // Same operations repeated...
}
```

**Problem:**
- Violates Open/Closed Principle
- Operations scattered across classes
- Hard to add new operations

---

## Solution With Visitor

```java
// Add new operations without modifying shape classes
Shape shape = new Circle();
shape.accept(new XMLExportVisitor());  // Export to XML
shape.accept(new JSONExportVisitor()); // Export to JSON
shape.accept(new PDFExportVisitor());  // Export to PDF
// No changes to Circle class!
```

---

## Implementation

### 1. Visitor Interface

```java
public interface ShapeVisitor {
    void visit(Circle circle);
    void visit(Rectangle rectangle);
    void visit(Triangle triangle);
}
```

### 2. Element Interface

```java
public interface Shape {
    void accept(ShapeVisitor visitor);
}
```

### 3. Concrete Elements

```java
public class Circle implements Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    public double getRadius() {
        return radius;
    }
    
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this); // Double dispatch
    }
}

public class Rectangle implements Shape {
    private double width;
    private double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
}

public class Triangle implements Shape {
    private double base;
    private double height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    public double getBase() { return base; }
    public double getHeight() { return height; }
    
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
}
```

### 4. Concrete Visitors

```java
// Area Calculator Visitor
public class AreaCalculator implements ShapeVisitor {
    
    @Override
    public void visit(Circle circle) {
        double area = Math.PI * circle.getRadius() * circle.getRadius();
        System.out.println("Circle area: " + area);
    }
    
    @Override
    public void visit(Rectangle rectangle) {
        double area = rectangle.getWidth() * rectangle.getHeight();
        System.out.println("Rectangle area: " + area);
    }
    
    @Override
    public void visit(Triangle triangle) {
        double area = 0.5 * triangle.getBase() * triangle.getHeight();
        System.out.println("Triangle area: " + area);
    }
}

// Perimeter Calculator Visitor
public class PerimeterCalculator implements ShapeVisitor {
    
    @Override
    public void visit(Circle circle) {
        double perimeter = 2 * Math.PI * circle.getRadius();
        System.out.println("Circle perimeter: " + perimeter);
    }
    
    @Override
    public void visit(Rectangle rectangle) {
        double perimeter = 2 * (rectangle.getWidth() + rectangle.getHeight());
        System.out.println("Rectangle perimeter: " + perimeter);
    }
    
    @Override
    public void visit(Triangle triangle) {
        // Simplified - assuming equilateral
        double perimeter = 3 * triangle.getBase();
        System.out.println("Triangle perimeter: " + perimeter);
    }
}

// Export Visitor
public class XMLExportVisitor implements ShapeVisitor {
    
    @Override
    public void visit(Circle circle) {
        System.out.println("<circle radius='" + circle.getRadius() + "'/>");
    }
    
    @Override
    public void visit(Rectangle rectangle) {
        System.out.println("<rectangle width='" + rectangle.getWidth() + 
                         "' height='" + rectangle.getHeight() + "'/>");
    }
    
    @Override
    public void visit(Triangle triangle) {
        System.out.println("<triangle base='" + triangle.getBase() + 
                         "' height='" + triangle.getHeight() + "'/>");
    }
}
```

### 5. Client Code

```java
public class VisitorDemo {
    public static void main(String[] args) {
        
        // Create shapes
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(5));
        shapes.add(new Rectangle(4, 6));
        shapes.add(new Triangle(3, 8));
        
        // Calculate areas
        System.out.println("=== Area Calculation ===");
        ShapeVisitor areaCalculator = new AreaCalculator();
        for (Shape shape : shapes) {
            shape.accept(areaCalculator);
        }
        
        // Calculate perimeters
        System.out.println("\n=== Perimeter Calculation ===");
        ShapeVisitor perimeterCalculator = new PerimeterCalculator();
        for (Shape shape : shapes) {
            shape.accept(perimeterCalculator);
        }
        
        // Export to XML
        System.out.println("\n=== XML Export ===");
        ShapeVisitor xmlExporter = new XMLExportVisitor();
        for (Shape shape : shapes) {
            shape.accept(xmlExporter);
        }
    }
}
```

**Output:**
```
=== Area Calculation ===
Circle area: 78.53981633974483
Rectangle area: 24.0
Triangle area: 12.0

=== Perimeter Calculation ===
Circle perimeter: 31.41592653589793
Rectangle perimeter: 20.0
Triangle perimeter: 9.0

=== XML Export ===
<circle radius='5.0'/>
<rectangle width='4.0' height='6.0'/>
<triangle base='3.0' height='8.0'/>
```

---

## Real-World Example: Shopping Cart with Tax Calculator

```java
// Product interface
interface Product {
    void accept(ShoppingVisitor visitor);
    String getName();
    double getPrice();
}

// Concrete products
class Book implements Product {
    private String name;
    private double price;
    
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    
    @Override
    public void accept(ShoppingVisitor visitor) {
        visitor.visit(this);
    }
}

class Electronics implements Product {
    private String name;
    private double price;
    
    public Electronics(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    
    @Override
    public void accept(ShoppingVisitor visitor) {
        visitor.visit(this);
    }
}

class Grocery implements Product {
    private String name;
    private double price;
    
    public Grocery(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    
    @Override
    public void accept(ShoppingVisitor visitor) {
        visitor.visit(this);
    }
}

// Visitor interface
interface ShoppingVisitor {
    void visit(Book book);
    void visit(Electronics electronics);
    void visit(Grocery grocery);
}

// Tax calculator visitor
class TaxCalculator implements ShoppingVisitor {
    private double totalTax = 0;
    
    @Override
    public void visit(Book book) {
        // Books: 0% tax
        System.out.println(book.getName() + " - Tax: $0.00");
    }
    
    @Override
    public void visit(Electronics electronics) {
        // Electronics: 18% tax
        double tax = electronics.getPrice() * 0.18;
        totalTax += tax;
        System.out.println(electronics.getName() + " - Tax: $" + tax);
    }
    
    @Override
    public void visit(Grocery grocery) {
        // Grocery: 5% tax
        double tax = grocery.getPrice() * 0.05;
        totalTax += tax;
        System.out.println(grocery.getName() + " - Tax: $" + tax);
    }
    
    public double getTotalTax() {
        return totalTax;
    }
}

// Discount calculator visitor
class DiscountCalculator implements ShoppingVisitor {
    private double totalDiscount = 0;
    
    @Override
    public void visit(Book book) {
        // Books: 10% discount
        double discount = book.getPrice() * 0.10;
        totalDiscount += discount;
        System.out.println(book.getName() + " - Discount: $" + discount);
    }
    
    @Override
    public void visit(Electronics electronics) {
        // Electronics: 15% discount
        double discount = electronics.getPrice() * 0.15;
        totalDiscount += discount;
        System.out.println(electronics.getName() + " - Discount: $" + discount);
    }
    
    @Override
    public void visit(Grocery grocery) {
        // Grocery: No discount
        System.out.println(grocery.getName() + " - Discount: $0.00");
    }
    
    public double getTotalDiscount() {
        return totalDiscount;
    }
}

// Usage
public class ShoppingCartDemo {
    public static void main(String[] args) {
        
        List<Product> cart = new ArrayList<>();
        cart.add(new Book("Java Programming", 50));
        cart.add(new Electronics("Laptop", 1000));
        cart.add(new Grocery("Milk", 5));
        
        // Calculate tax
        System.out.println("=== Tax Calculation ===");
        TaxCalculator taxCalc = new TaxCalculator();
        for (Product product : cart) {
            product.accept(taxCalc);
        }
        System.out.println("Total Tax: $" + taxCalc.getTotalTax());
        
        // Calculate discount
        System.out.println("\n=== Discount Calculation ===");
        DiscountCalculator discountCalc = new DiscountCalculator();
        for (Product product : cart) {
            product.accept(discountCalc);
        }
        System.out.println("Total Discount: $" + discountCalc.getTotalDiscount());
    }
}
```

**Output:**
```
=== Tax Calculation ===
Java Programming - Tax: $0.00
Laptop - Tax: $180.0
Milk - Tax: $0.25
Total Tax: $180.25

=== Discount Calculation ===
Java Programming - Discount: $5.0
Laptop - Discount: $150.0
Milk - Discount: $0.00
Total Discount: $155.0
```

---

## Double Dispatch Explained

**Why do we need accept() method?**

```java
// Single dispatch (doesn't work well)
visitor.visit(shape); // Compiler doesn't know which visit() to call

// Double dispatch (works!)
shape.accept(visitor);
    ↓
visitor.visit(circle); // Now knows it's a Circle!
```

**Flow:**
1. `shape.accept(visitor)` - First dispatch (based on shape type)
2. `visitor.visit(this)` - Second dispatch (based on visitor type)

---

## When to Use Visitor Pattern

✅ **Use when:**
- Need to add many operations to existing classes
- Operations change frequently, classes don't
- Want to keep related operations together
- Need different operations on same object structure

**Examples:**
- Compilers (AST traversal)
- File system operations
- Report generation
- Tax/discount calculations

❌ **Don't use when:**
- Classes change frequently (need to update all visitors)
- Only a few operations
- Operations are simple

---

## Benefits

1. **Open/Closed Principle:** Add operations without modifying classes
2. **Single Responsibility:** Operations grouped in visitors
3. **Easy to Add Operations:** Just create new visitor
4. **Collect Information:** Visitor can accumulate data across elements

---

## Drawbacks

1. **Adding New Elements is Hard:** Must update all visitors
2. **Breaks Encapsulation:** Visitor needs access to element internals
3. **Circular Dependency:** Elements and visitors know about each other

---

## Quick Summary

| Concept | Description |
|---------|-------------|
| **Purpose** | Add operations to classes without modifying them |
| **Analogy** | Inspector visiting different rooms |
| **Key Methods** | accept(visitor), visit(element) |
| **Double Dispatch** | Two method calls to determine correct operation |
| **Use Case** | Multiple operations on stable class structure |
| **Benefit** | Easy to add new operations |
| **Drawback** | Hard to add new element types |

---

## Memory Trick

**Visitor = Health Inspector**
- Inspector visits different businesses (accept)
- Performs different checks per business type (visit)
- Adding new inspection types doesn't change businesses (Open/Closed)

**Remember:** Visitor brings operations, elements just accept visitors!