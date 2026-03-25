/**
 * Inheritance Demo
 * 
 * Shows how a 'Developer' class IS-A 'Employee' and inherits its properties.
 */

// 1. Parent Class (Superclass)
class Employee {
    protected String name;
    protected double baseSalary;

    public Employee(String name, double baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    public void displayDetails() {
        System.out.println("Employee: " + name);
        System.out.println("Base Salary: $" + baseSalary);
    }

    public double calculatePay() {
        return baseSalary;
    }
}

// 2. Child Class (Subclass) - IS-A Employee
class Developer extends Employee {
    private String programmingLanguage;
    private double bonus;

    public Developer(String name, double baseSalary, String programmingLanguage, double bonus) {
        // 3. Using super to call the parent's constructor
        super(name, baseSalary);
        this.programmingLanguage = programmingLanguage;
        this.bonus = bonus;
    }

    // 4. Method Overriding (Providing specific implementation)
    @Override
    public void displayDetails() {
        // Using super to call parent's method
        super.displayDetails();
        System.out.println("Specialization: " + programmingLanguage + " Developer");
    }

    @Override
    public double calculatePay() {
        // Child's logic for payment (salary + bonus)
        return baseSalary + bonus;
    }

    public void writeCode() {
        System.out.println(name + " is writing code in " + programmingLanguage + "...");
    }
}

public class InheritanceDemo {
    public static void main(String[] args) {
        System.out.println("=== INHERITANCE DEMO ===\n");

        // Creating a Developer object
        Developer dev = new Developer("Bob", 5000, "Java", 1200);

        // Accessing inherited and specific methods
        dev.displayDetails();
        System.out.println("Total Pay: $" + dev.calculatePay());
        
        dev.writeCode(); // Specific to Developer

        System.out.println("\n------------------------\n");

        // 5. Polymorphism (Brief Preview)
        // A Parent reference can point to a Child object
        Employee emp = new Developer("Alice", 6000, "Python", 1500);
        emp.displayDetails(); // Calls the OVERRIDDEN method in Developer!
        System.out.println("Alice's Pay: $" + emp.calculatePay());
    }
}
