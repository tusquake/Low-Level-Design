# Proxy + Decorator Pattern: Controlling Access vs Adding Features

## Real-World Scenario: Image Gallery & Coffee Shop

**Problem:**
You're building systems that need to:
1. **Control access to expensive objects** (Proxy Pattern)
    - Load large images only when needed (lazy loading)
    - Add security checks before accessing resources
    - Cache expensive operations
2. **Add features dynamically to objects** (Decorator Pattern)
    - Add milk, sugar, whipped cream to coffee
    - Add features without modifying original class
    - Combine multiple features

---

## Architecture Overview

```
Both patterns wrap objects, but for different purposes:

Proxy: Controls access to object
    ↓
Subject ← Proxy → RealSubject
    
Decorator: Adds functionality to object
    ↓
Component ← Decorator → EnhancedComponent
```

---

## Pattern Comparison

| Aspect | Proxy | Decorator |
|--------|-------|-----------|
| **Purpose** | Control access | Add functionality |
| **Intent** | Represent another object | Enhance object behavior |
| **Relationship** | Same interface, controls access | Same interface, adds features |
| **Number** | Usually one proxy | Can have multiple decorators |
| **When Created** | Usually created upfront | Created dynamically as needed |
| **Focus** | When/How to access | What functionality to add |

---

## PART 1: PROXY PATTERN

### Problem
Loading large images from disk is expensive. Don't want to load them until actually needed.

### Solution
Use a Proxy that defers creating the real object until it's actually needed (lazy initialization).

---

## Implementation - Proxy Pattern

### 1. Subject Interface

```java
interface Image {
    void display();
}
```

### 2. Real Subject (Expensive Object)

```java
class RealImage implements Image {
    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk();  // Expensive operation!
    }

    private void loadFromDisk() {
        System.out.println("Loading image from disk: " + fileName);
    }

    public void display() {
        System.out.println("Displaying image: " + fileName);
    }
}
```

### 3. Proxy (Controls Access)

```java
class ProxyImage implements Image {
    private RealImage realImage;  // Real object (created lazily)
    private String fileName;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
        // Real image NOT created yet!
    }

    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);  // Create only when needed
        }
        realImage.display();  // Delegate to real object
    }
}
```

### 4. Usage

```java
public class ProxyDemo {
    public static void main(String[] args) {
        Image img = new ProxyImage("photo.png");
        // Image NOT loaded yet!

        img.display();  // NOW image is loaded
        img.display();  // Uses cached image, no reload
    }
}
```

### Output
```
Loading image from disk: photo.png
Displaying image: photo.png
Displaying image: photo.png
```

**Notice:** Image loaded only once (first display), then cached!

---

## Benefits - Proxy Pattern

**Lazy Initialization**
- Create expensive objects only when needed
- Saves memory and startup time

**Access Control**
- Add security checks before accessing real object
- Control who can access what

**Caching**
- Store results of expensive operations
- Reuse without recreating

**Remote Proxy**
- Represent objects in different address spaces
- Hide network communication details

**Smart Reference**
- Add additional actions when accessing object
- Reference counting, logging, etc.

---

## How Proxy Pattern Works

### First Call (Lazy Loading):

```
1. Client calls: img.display()
   ↓
2. ProxyImage checks: realImage == null? YES
   ↓
3. ProxyImage creates: new RealImage("photo.png")
   ↓
4. RealImage loads from disk (expensive!)
   ↓
5. ProxyImage delegates: realImage.display()
   ↓
Output: "Loading image from disk: photo.png"
        "Displaying image: photo.png"
```

### Second Call (Using Cache):

```
1. Client calls: img.display()
   ↓
2. ProxyImage checks: realImage == null? NO
   ↓
3. ProxyImage delegates: realImage.display()
   ↓
Output: "Displaying image: photo.png"
(No loading - uses cached object!)
```

### Visual Representation:

```
Before first display():
┌─────────────┐
│ ProxyImage  │
│ realImage=null
│ fileName="photo.png"
└─────────────┘

After first display():
┌─────────────┐
│ ProxyImage  │──────┐
│ realImage ────────→│ RealImage      │
│ fileName    │      │ fileName="photo.png"
└─────────────┘      │ [Loaded from disk]
                     └────────────────┘
```

---

## Types of Proxies

### 1. Virtual Proxy (Lazy Loading)

```java
class VirtualProxy implements ExpensiveObject {
    private ExpensiveObject realObject;
    
    public void operation() {
        if (realObject == null) {
            realObject = new RealExpensiveObject();  // Create on demand
        }
        realObject.operation();
    }
}
```

**Use Case:** Loading large images, videos, documents

### 2. Protection Proxy (Access Control)

```java
class ProtectionProxy implements Document {
    private RealDocument document;
    private User currentUser;
    
    public void view() {
        if (currentUser.hasPermission("READ")) {
            document.view();
        } else {
            throw new SecurityException("Access denied");
        }
    }
}
```

**Use Case:** Authentication, authorization

### 3. Remote Proxy (Network Communication)

```java
class RemoteProxy implements Service {
    private String serverUrl;
    
    public Data getData() {
        // Hides network communication
        return httpClient.get(serverUrl + "/data");
    }
}
```

**Use Case:** Web services, RMI, distributed systems

### 4. Caching Proxy

```java
class CachingProxy implements DataService {
    private RealDataService service;
    private Map<String, Data> cache = new HashMap<>();
    
    public Data getData(String key) {
        if (!cache.containsKey(key)) {
            cache.put(key, service.getData(key));  // Cache result
        }
        return cache.get(key);
    }
}
```

**Use Case:** Database queries, API calls

### 5. Logging Proxy

```java
class LoggingProxy implements Operation {
    private RealOperation operation;
    
    public void execute() {
        System.out.println("Before execution");
        operation.execute();
        System.out.println("After execution");
    }
}
```

**Use Case:** Debugging, monitoring, audit trails

---

## PART 2: DECORATOR PATTERN

### Problem
Coffee shop needs to add optional ingredients (milk, sugar, whipped cream) to coffee. Each combination has different price.

### Solution
Wrap coffee objects with decorator layers, each adding its own feature and cost.

---

## Implementation - Decorator Pattern

### 1. Component Interface

```java
interface Coffee {
    String getDescription();
    int cost();
}
```

### 2. Concrete Component (Base)

```java
class SimpleCoffee implements Coffee {
    public String getDescription() {
        return "Simple coffee";
    }

    public int cost() {
        return 50;
    }
}
```

### 3. Abstract Decorator

```java
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}
```

### 4. Concrete Decorators

```java
class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    public int cost() {
        return coffee.cost() + 20;
    }
}

class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Sugar";
    }

    public int cost() {
        return coffee.cost() + 10;
    }
}
```

### 5. Usage

```java
public class DecoratorDemo {
    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " = Rs." + coffee.cost());
        
        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " = Rs." + coffee.cost());
        
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " = Rs." + coffee.cost());
    }
}
```

### Output
```
Simple coffee = Rs.50
Simple coffee, Milk = Rs.70
Simple coffee, Milk, Sugar = Rs.80
```

---

## Benefits - Decorator Pattern

**Add Features Dynamically**
- Add functionality at runtime
- No need to modify original class

**Flexible Combinations**
- Any combination of decorators
- No class explosion

**Open/Closed Principle**
- Open for extension (add decorators)
- Closed for modification (don't change base class)

**Single Responsibility**
- Each decorator has one job
- Clean separation of concerns

---

## How Decorator Pattern Works

### Wrapping Process:

```
Start: SimpleCoffee
    ↓
Wrap with Milk: MilkDecorator(SimpleCoffee)
    ↓
Wrap with Sugar: SugarDecorator(MilkDecorator(SimpleCoffee))
```

### Cost Calculation:

```
coffee.cost() called on outermost decorator
    ↓
SugarDecorator.cost()
    → return coffee.cost() + 10
    ↓
MilkDecorator.cost()
    → return coffee.cost() + 20
    ↓
SimpleCoffee.cost()
    → return 50
    ↓
Unwinding:
50 (SimpleCoffee) + 20 (Milk) + 10 (Sugar) = 80
```

### Visual Representation:

```
┌──────────────────────────┐
│   SugarDecorator         │
│   cost = coffee.cost()+10│
│   ┌────────────────────┐ │
│   │  MilkDecorator     │ │
│   │  cost = coffee.cost()+20
│   │  ┌──────────────┐  │ │
│   │  │ SimpleCoffee │  │ │
│   │  │ cost = 50    │  │ │
│   │  └──────────────┘  │ │
│   └────────────────────┘ │
└──────────────────────────┘

Each layer adds its cost!
```

---

## Proxy vs Decorator: Key Differences

### Intent

**Proxy:**
```java
// Controls WHEN/HOW you access the object
Image img = new ProxyImage("photo.png");
img.display();  // Proxy decides when to create real image
```

**Decorator:**
```java
// Adds WHAT functionality the object has
Coffee coffee = new MilkDecorator(new SimpleCoffee());
coffee.cost();  // Adds milk feature to coffee
```

### Relationship to Real Object

**Proxy:**
```java
class ProxyImage {
    private RealImage realImage;  // MAY OR MAY NOT exist
    
    public void display() {
        if (realImage == null) {
            realImage = new RealImage();  // Creates when needed
        }
        realImage.display();
    }
}
```

**Decorator:**
```java
class MilkDecorator {
    private Coffee coffee;  // ALWAYS exists
    
    public int cost() {
        return coffee.cost() + 20;  // Enhances existing object
    }
}
```

### Number of Wrappers

**Proxy:**
- Usually ONE proxy per object
- ProxyImage wraps RealImage

**Decorator:**
- Can have MULTIPLE decorators
- SugarDecorator(MilkDecorator(WhipDecorator(SimpleCoffee)))

### When Created

**Proxy:**
```java
// Proxy created upfront
Image proxy = new ProxyImage("photo.png");  // Real image NOT created
// Real object created later (lazy)
proxy.display();  // NOW real image is created
```

**Decorator:**
```java
// All objects created immediately
Coffee coffee = new SugarDecorator(
                    new MilkDecorator(
                        new SimpleCoffee()  // All created now
                    ));
```

---

## When to Use Which Pattern?

### Use Proxy When:
- **Control access** to expensive objects
- **Lazy initialization** needed
- **Add security** before accessing object
- **Cache** expensive operations
- **Hide network** communication
- **Log/monitor** access to objects

### Use Decorator When:
- **Add features** dynamically
- **Avoid class explosion** from feature combinations
- **Multiple features** can be combined
- **Features are optional**
- **Want to extend** without modifying original class

---

## Real-World Examples

### Proxy Pattern

**Image Gallery App:**
```java
// Don't load all 1000 images at startup!
List<Image> gallery = new ArrayList<>();
for (String file : imageFiles) {
    gallery.add(new ProxyImage(file));  // Proxy, not real image
}
// Images loaded only when scrolled into view
```

**Database Connection Pool:**
```java
class ConnectionProxy implements Connection {
    private RealConnection connection;
    
    public void execute(String query) {
        if (connection == null || !connection.isValid()) {
            connection = connectionPool.getConnection();  // Lazy
        }
        connection.execute(query);
    }
}
```

**Spring Framework:**
```java
// Spring creates proxies for @Transactional methods
@Transactional
public void saveUser(User user) {
    // Spring proxy adds transaction management
}
```

### Decorator Pattern

**Java I/O Streams:**
```java
InputStream input = new BufferedInputStream(
                        new GZIPInputStream(
                            new FileInputStream("file.txt")
                        ));
// Each decorator adds functionality
```

**Pizza Order:**
```java
Pizza pizza = new ExtraCheeseDecorator(
                  new OlivesDecorator(
                      new MargheritaPizza()
                  ));
```

**Notification System:**
```java
Notification notification = new SMSDecorator(
                                new EmailDecorator(
                                    new BasicNotification()
                                ));
```

---

## Complete Example: Enhanced Image Proxy

```java
interface Image {
    void display();
    String getInfo();
}

class RealImage implements Image {
    private String fileName;
    private int fileSize;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading " + fileName + " from disk...");
        // Simulate expensive loading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.fileSize = 5120;  // 5MB
        System.out.println("Loaded " + fileName + " (" + fileSize + " KB)");
    }

    public void display() {
        System.out.println("Displaying: " + fileName);
    }
    
    public String getInfo() {
        return fileName + " (" + fileSize + " KB)";
    }
}

class ImageProxy implements Image {
    private RealImage realImage;
    private String fileName;
    private int accessCount = 0;

    public ImageProxy(String fileName) {
        this.fileName = fileName;
        System.out.println("Created proxy for: " + fileName);
    }

    public void display() {
        accessCount++;
        System.out.println("Access #" + accessCount);
        
        if (realImage == null) {
            System.out.println("First access - loading real image...");
            realImage = new RealImage(fileName);
        } else {
            System.out.println("Using cached image");
        }
        
        realImage.display();
    }
    
    public String getInfo() {
        if (realImage == null) {
            return fileName + " (not loaded yet)";
        }
        return realImage.getInfo();
    }
}

// Usage
public class EnhancedProxyDemo {
    public static void main(String[] args) {
        System.out.println("=== Creating Image Gallery ===");
        Image img1 = new ImageProxy("vacation.jpg");
        Image img2 = new ImageProxy("family.jpg");
        Image img3 = new ImageProxy("sunset.jpg");
        
        System.out.println("\n=== Displaying First Image ===");
        img1.display();
        
        System.out.println("\n=== Displaying First Image Again ===");
        img1.display();
        
        System.out.println("\n=== Getting Info ===");
        System.out.println(img1.getInfo());
        System.out.println(img2.getInfo());  // Not loaded yet
        
        System.out.println("\n=== Displaying Second Image ===");
        img2.display();
    }
}
```

### Output
```
=== Creating Image Gallery ===
Created proxy for: vacation.jpg
Created proxy for: family.jpg
Created proxy for: sunset.jpg

=== Displaying First Image ===
Access #1
First access - loading real image...
Loading vacation.jpg from disk...
Loaded vacation.jpg (5120 KB)
Displaying: vacation.jpg

=== Displaying First Image Again ===
Access #2
Using cached image
Displaying: vacation.jpg

=== Getting Info ===
vacation.jpg (5120 KB)
family.jpg (not loaded yet)

=== Displaying Second Image ===
Access #1
First access - loading real image...
Loading family.jpg from disk...
Loaded family.jpg (5120 KB)
Displaying: family.jpg
```

---

## Complete Example: Enhanced Coffee Decorator

```java
interface Coffee {
    String getDescription();
    int cost();
}

class Espresso implements Coffee {
    public String getDescription() {
        return "Espresso";
    }
    public int cost() {
        return 60;
    }
}

class Cappuccino implements Coffee {
    public String getDescription() {
        return "Cappuccino";
    }
    public int cost() {
        return 80;
    }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    public String getDescription() {
        return coffee.getDescription() + " + Milk";
    }
    public int cost() {
        return coffee.cost() + 20;
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }
    public String getDescription() {
        return coffee.getDescription() + " + Sugar";
    }
    public int cost() {
        return coffee.cost() + 10;
    }
}

class WhippedCreamDecorator extends CoffeeDecorator {
    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }
    public String getDescription() {
        return coffee.getDescription() + " + Whipped Cream";
    }
    public int cost() {
        return coffee.cost() + 30;
    }
}

class CaramelDecorator extends CoffeeDecorator {
    public CaramelDecorator(Coffee coffee) {
        super(coffee);
    }
    public String getDescription() {
        return coffee.getDescription() + " + Caramel";
    }
    public int cost() {
        return coffee.cost() + 25;
    }
}

// Usage
public class CoffeeShopDemo {
    public static void main(String[] args) {
        // Order 1: Simple espresso
        Coffee order1 = new Espresso();
        System.out.println(order1.getDescription() + " = Rs." + order1.cost());
        
        // Order 2: Espresso with milk
        Coffee order2 = new MilkDecorator(new Espresso());
        System.out.println(order2.getDescription() + " = Rs." + order2.cost());
        
        // Order 3: Cappuccino with everything!
        Coffee order3 = new CaramelDecorator(
                            new WhippedCreamDecorator(
                                new SugarDecorator(
                                    new MilkDecorator(
                                        new Cappuccino()
                                    )
                                )
                            )
                        );
        System.out.println(order3.getDescription() + " = Rs." + order3.cost());
        
        // Order 4: Build step by step
        System.out.println("\n=== Building Custom Coffee ===");
        Coffee custom = new Espresso();
        System.out.println("Base: " + custom.getDescription() + " = Rs." + custom.cost());
        
        custom = new MilkDecorator(custom);
        System.out.println("+ Milk: " + custom.getDescription() + " = Rs." + custom.cost());
        
        custom = new SugarDecorator(custom);
        System.out.println("+ Sugar: " + custom.getDescription() + " = Rs." + custom.cost());
        
        custom = new WhippedCreamDecorator(custom);
        System.out.println("+ Whipped Cream: " + custom.getDescription() + " = Rs." + custom.cost());
    }
}
```

### Output
```
Espresso = Rs.60
Espresso + Milk = Rs.80
Cappuccino + Milk + Sugar + Whipped Cream + Caramel = Rs.165

=== Building Custom Coffee ===
Base: Espresso = Rs.60
+ Milk: Espresso + Milk = Rs.80
+ Sugar: Espresso + Milk + Sugar = Rs.90
+ Whipped Cream: Espresso + Milk + Sugar + Whipped Cream = Rs.120
```

---

## Running the Code

```bash
# Proxy Pattern
javac ProxyDemo.java
java ProxyDemo

# Decorator Pattern
javac DecoratorDemo.java
java DecoratorDemo

# Enhanced Examples
javac EnhancedProxyDemo.java
java EnhancedProxyDemo

javac CoffeeShopDemo.java
java CoffeeShopDemo
```

---

## Common Mistakes

### Proxy Pattern Mistakes

**Mistake 1: Creating Real Object in Proxy Constructor**

```java
// BAD: Defeats lazy loading!
class ProxyImage {
    private RealImage realImage;
    
    public ProxyImage(String file) {
        this.realImage = new RealImage(file);  // Created immediately!
    }
}

// GOOD: Create only when needed
class ProxyImage {
    private RealImage realImage;
    
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(file);  // Lazy creation
        }
    }
}
```

**Mistake 2: Not Delegating All Methods**

```java
// BAD: Missing delegation
class ProxyImage implements Image {
    public void display() {
        if (realImage == null) realImage = new RealImage();
        realImage.display();
    }
    // Missing: getInfo(), getSize(), etc.
}

// GOOD: Delegate all interface methods
class ProxyImage implements Image {
    public void display() { /* ... */ }
    public String getInfo() { return realImage.getInfo(); }
    public int getSize() { return realImage.getSize(); }
}
```

### Decorator Pattern Mistakes

**Mistake 1: Forgetting to Call Wrapped Object**

```java
// BAD: Doesn't call wrapped object
class MilkDecorator extends CoffeeDecorator {
    public int cost() {
        return 20;  // Wrong! Ignores base coffee cost
    }
}

// GOOD: Add to wrapped object's result
class MilkDecorator extends CoffeeDecorator {
    public int cost() {
        return coffee.cost() + 20;  // Adds to base cost
    }
}
```

**Mistake 2: Making Decorators Too Specific**

```java
// BAD: Tied to specific class
class MilkDecorator extends CoffeeDecorator {
    public int cost() {
        if (coffee instanceof Espresso) {
            return 80;
        } else {
            return 100;
        }
    }
}

// GOOD: Works with any coffee
class MilkDecorator extends CoffeeDecorator {
    public int cost() {
        return coffee.cost() + 20;  // Generic
    }
}
```

---

## Combining Both Patterns

```java
// Use Proxy for lazy loading, Decorator for features

// Lazy load image (Proxy)
Image image = new ImageProxy("photo.jpg");

// Add filters (Decorator)
Image filtered = new BlurDecorator(
                     new SepiaDecorator(
                         image  // Can be a proxy!
                     ));

filtered.display();
// First access: Proxy loads real image
// Then: Decorators apply filters
```

---

## Design Principles Applied

### Both Patterns

**Open/Closed Principle**
- Open for extension (add proxies/decorators)
- Closed for modification (don't change original class)

**Liskov Substitution Principle**
- Proxy/Decorator can replace original object
- Client code doesn't know the difference

**Dependency Inversion**
- Both depend on abstractions (interfaces)
- Not on concrete classes

### Proxy Specifically

**Single Responsibility**
- Real object: business logic
- Proxy: access control, lazy loading, caching

### Decorator Specifically

**Favor Composition Over Inheritance**
- Uses wrapping instead of subclassing
- More flexible than inheritance hierarchies

---

## Interview Questions & Answers

### Q1: What's the main difference between Proxy and Decorator?

**A:** "The main difference is their intent:

**Proxy:**
- **Intent**: Control access to an object
- **Focus**: WHEN and HOW to access
- **Example**: Don't load image until needed (lazy loading)

**Decorator:**
- **Intent**: Add functionality to an object
- **Focus**: WHAT features to add
- **Example**: Add milk and sugar to coffee

**Code difference:**
```java
// Proxy: Controls access
class ImageProxy {
    public void display() {
        if (realImage == null) {  // Controls WHEN
            realImage = new RealImage();
        }
        realImage.display();
    }
}

// Decorator: Adds features
class MilkDecorator {
    public int cost() {
        return coffee.cost() + 20;  // Adds WHAT (milk cost)
    }
}
```

**Memory:**
- **Proxy**: 'Lazy loading Proxy'
- **Decorator**: 'Feature adding Decorator'"

---

### Q2: Can you explain lazy loading with an example?

**A:** "Lazy loading means creating objects only when they're actually needed, not upfront.

**Without Lazy Loading (BAD):**
```java
// Creating 1000 images at startup
List<Image> gallery = new ArrayList<>();
for (int i = 0; i < 1000; i++) {
    gallery.add(new RealImage(files[i]));  // All loaded NOW
}
// App startup: 30 seconds! User sees only 10 images!
```

**With Lazy Loading (GOOD):**
```java
// Creating 1000 proxies at startup
List<Image> gallery = new ArrayList<>();
for (int i = 0; i < 1000; i++) {
    gallery.add(new ProxyImage(files[i]));  // Just proxies
}
// App startup: Instant! Images load as user scrolls
```

**Benefits:**
- Faster startup time
- Lower memory usage
- Better user experience

**Real-world uses:**
- Image galleries (don't load all images)
- Database connections (connect only when querying)
- Large documents (load pages on demand)
- Video streaming (load chunks as needed)"

---

### Q3: Why use Decorator instead of inheritance for adding features?

**A:** "Decorator is better than inheritance because:

**Problem with Inheritance:**
```java
// 3 features = 8 classes!
Coffee
├── MilkCoffee
├── SugarCoffee
├── WhipCoffee
├── MilkSugarCoffee
├── MilkWhipCoffee
├── SugarWhipCoffee
└── MilkSugarWhipCoffee

// 5 features = 32 classes!
```

**Solution with Decorator:**
```java
// 3 features = 4 classes (1 base + 3 decorators)
Coffee (base)
MilkDecorator
SugarDecorator
WhipDecorator

// Any combination:
new MilkDecorator(new SugarDecorator(new Coffee()))
new WhipDecorator(new MilkDecorator(new Coffee()))
```

**Benefits:**
1. **No class explosion**: n features = n+1 classes instead of 2^n
2. **Runtime flexibility**: Add features dynamically
3. **Any combination**: Mix and match decorators
4. **Easy to extend**: Add new decorator without changing existing code

**Example:**
```java
// Start simple
Coffee coffee = new SimpleCoffee();

// Customer changes mind - add milk
coffee = new MilkDecorator(coffee);

// Add more features easily
coffee = new SugarDecorator(coffee);
coffee = new WhipDecorator(coffee);
```

Can't do this easily with inheritance!"

---

### Q4: When would you use a Protection Proxy in a real application?

**A:** "I'd use a Protection Proxy to control access to sensitive resources based on permissions.

**Real Example: Document Management System**

```java
interface Document {
    void view();
    void edit();
    void delete();
}

class SensitiveDocument implements Document {
    private String content;
    
    public void view() {
        System.out.println("Viewing: " + content);
    }
    
    public void edit() {
        System.out.println("Editing document");
    }
    
    public void delete() {
        System.out.println("Deleting document");
    }
}

class DocumentProxy implements Document {
    private SensitiveDocument document;
    private User currentUser;
    
    public DocumentProxy(SensitiveDocument doc, User user) {
        this.document = doc;
        this.currentUser = user;
    }
    
    public void view() {
        if (currentUser.hasPermission("READ")) {
            document.view();
        } else {
            throw new SecurityException("Access denied");
        }
    }
    
    public void edit() {
        if (currentUser.hasPermission("WRITE")) {
            document.edit();
        } else {
            throw new SecurityException("Access denied");
        }
    }
    
    public void delete() {
        if (currentUser.hasPermission("DELETE")) {
            document.delete();
        } else {
            throw new SecurityException("Access denied");
        }
    }
}

// Usage
User viewer = new User("viewer", Arrays.asList("READ"));
User editor = new User("editor", Arrays.asList("READ", "WRITE"));
User admin = new User("admin", Arrays.asList("READ", "WRITE", "DELETE"));

Document doc = new DocumentProxy(new SensitiveDocument(), viewer);
doc.view();    // OK
doc.edit();    // SecurityException!
doc.delete();  // SecurityException!
```

**Benefits:**
- Centralized security logic
- Can't bypass security by accessing document directly
- Easy to add logging, auditing
- Security rules can be changed in proxy without touching document class"

---

## Pattern Variations

### Smart Proxy

```java
class SmartProxy implements Resource {
    private RealResource resource;
    private int referenceCount = 0;
    
    public void use() {
        referenceCount++;
        if (resource == null) {
            resource = new RealResource();
        }
        resource.use();
    }
    
    public void release() {
        referenceCount--;
        if (referenceCount == 0) {
            resource.cleanup();  // Auto-cleanup when no references
            resource = null;
        }
    }
}
```

### Transparent Decorator

```java
// Decorator that can be turned on/off
class OptionalLoggingDecorator extends CoffeeDecorator {
    private boolean loggingEnabled = true;
    
    public String getDescription() {
        String desc = coffee.getDescription();
        if (loggingEnabled) {
            System.out.println("Getting description: " + desc);
        }
        return desc;
    }
    
    public void setLogging(boolean enabled) {
        this.loggingEnabled = enabled;
    }
}
```

---

## Summary Table

| Feature | Proxy | Decorator |
|---------|-------|-----------|
| **Purpose** | Control access | Add functionality |
| **Real object** | May not exist yet | Always exists |
| **Number of wrappers** | Usually one | Can be many |
| **When created** | Lazy (when needed) | Immediate |
| **Typical uses** | Lazy loading, security, caching | Adding features dynamically |
| **Code focus** | if (object == null) create | return wrapped.method() + extra |
| **Example** | Load image when needed | Add milk to coffee |

---

## Quick Reference

### Proxy Pattern Structure
```
Subject (interface)
    └── operation()

RealSubject implements Subject
    └── operation() { /* real work */ }

Proxy implements Subject
    ├── RealSubject realSubject
    └── operation() {
            if (realSubject == null)
                realSubject = new RealSubject()
            realSubject.operation()
        }
```

### Decorator Pattern Structure
```
Component (interface)
    └── operation()

ConcreteComponent implements Component
    └── operation() { /* base behavior */ }

Decorator implements Component
    ├── Component component
    └── operation() { /* can override */ }

ConcreteDecorator extends Decorator
    └── operation() {
            component.operation()  // Delegate
            /* add extra behavior */
        }
```

---