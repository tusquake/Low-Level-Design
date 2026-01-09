# Composite Design Pattern - Simple Example

## What is it?

The Composite Pattern lets you treat single objects and groups of objects the same way. Think of files and folders - you can get the size of a single file or an entire folder with subfolders using the same method.

---

## The Pattern

```
Component (Interface)
    ↑
    |
    ├─── Leaf (Individual item)
    └─── Composite (Container with children)
```

---

## Simple Code Example

### Step 1: Create the Component Interface

```java
public interface Employee {
    void showDetails();
    int getSalary();
}
```

### Step 2: Create Leaf (Individual Employee)

```java
public class Developer implements Employee {
    private String name;
    private int salary;
    
    public Developer(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }
    
    @Override
    public void showDetails() {
        System.out.println("Developer: " + name + " ($" + salary + ")");
    }
    
    @Override
    public int getSalary() {
        return salary;
    }
}
```

### Step 3: Create Composite (Manager with Team)

```java
public class Manager implements Employee {
    private String name;
    private int salary;
    private List<Employee> team = new ArrayList<>();
    
    public Manager(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }
    
    public void addTeamMember(Employee emp) {
        team.add(emp);
    }
    
    @Override
    public void showDetails() {
        System.out.println("Manager: " + name + " ($" + salary + ")");
        for (Employee emp : team) {
            System.out.print("  ");
            emp.showDetails();
        }
    }
    
    @Override
    public int getSalary() {
        int total = salary;
        for (Employee emp : team) {
            total += emp.getSalary();
        }
        return total;
    }
}
```

### Step 4: Use It

```java
public class Main {
    public static void main(String[] args) {
        // Individual developers
        Developer dev1 = new Developer("Alice", 80000);
        Developer dev2 = new Developer("Bob", 75000);
        Developer dev3 = new Developer("Carol", 85000);
        
        // Team lead managing developers
        Manager teamLead = new Manager("David", 95000);
        teamLead.addTeamMember(dev1);
        teamLead.addTeamMember(dev2);
        
        // Department head managing team lead and a developer
        Manager deptHead = new Manager("Eve", 120000);
        deptHead.addTeamMember(teamLead);
        deptHead.addTeamMember(dev3);
        
        // Show organization structure
        System.out.println("=== Organization Structure ===\n");
        deptHead.showDetails();
        
        // Calculate total salary cost
        System.out.println("\n=== Salary Summary ===\n");
        System.out.println("Total Department Cost: $" + deptHead.getSalary());
    }
}
```

---

## Output

```
=== Organization Structure ===

Manager: Eve ($120000)
  Manager: David ($95000)
    Developer: Alice ($80000)
    Developer: Bob ($75000)
  Developer: Carol ($85000)

=== Salary Summary ===

Total Department Cost: $455000
```

---

## Key Point

Notice how both `Developer` and `Manager` implement the same `Employee` interface. This means you can call `showDetails()` and `getSalary()` on either type - the code doesn't need to know if it's dealing with an individual or a group!

---

## Real-World Uses

- **File systems**: Files and folders
- **UI components**: Buttons and panels
- **Organization charts**: Employees and departments
- **Graphics**: Individual shapes and grouped shapes
- **Menus**: Menu items and submenus

---

## When to Use

✅ You have a tree structure (hierarchy)  
✅ You want to treat individuals and groups uniformly  
✅ You need operations that work on the whole tree

---

## Advantages

- **Simple client code**: No need for type checking
- **Easy to add new components**: Just implement the interface
- **Recursive operations**: Automatically traverse the tree