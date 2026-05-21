import java.util.*;

/**
 * Object Relationships & UML Connections Demo
 * 
 * Shows Association, Aggregation, Composition, Generalization, Realization, and Dependency.
 */

// 1. ASSOCIATION (Teacher <-> Student)
// Both can exist independently.
class Teacher {
    private String name;
    public Teacher(String name) { this.name = name; }
    public String getName() { return name; }
}

class Student {
    private String name;
    public Student(String name) { this.name = name; }
    public String getName() { return name; }
}

// 2. AGGREGATION (Department -> Professors)
// The Part (Professor) can exist without the Whole (Department).
class Professor {
    private String name;
    public Professor(String name) { this.name = name; }
    public String getName() { return name; }
}

class Department {
    private String deptName;
    private List<Professor> professors;

    public Department(String deptName, List<Professor> professors) {
        this.deptName = deptName;
        this.professors = professors;
    }

    public void display() {
        System.out.println("Department: " + deptName);
        for (Professor p : professors) {
            System.out.println("  Professor: " + p.getName());
        }
    }
}

// 3. COMPOSITION (House -> Rooms)
// The Part (Room) cannot exist without the Whole (House).
// Lifecycle is managed by the House.
class Room {
    private String roomType;
    public Room(String roomType) { this.roomType = roomType; }
    public String getRoomType() { return roomType; }
}

class House {
    private List<Room> rooms;

    public House() {
        // Rooms are created inside the House constructor (Composition)
        this.rooms = new ArrayList<>();
        rooms.add(new Room("Bedroom"));
        rooms.add(new Room("Kitchen"));
        rooms.add(new Room("Living Room"));
    }

    public void display() {
        System.out.println("House Layout:");
        for (Room r : rooms) {
            System.out.println("  Room: " + r.getRoomType());
        }
    }
}

// 4. GENERALIZATION (Vehicle -> Car)
// A class inherits from another class. Represents a strict "IS-A" relationship.
class Vehicle {
    private String brand;
    public Vehicle(String brand) { this.brand = brand; }
    public void startEngine() {
        System.out.println("Engine started for brand: " + brand);
    }
}

class Car extends Vehicle {
    private int numberOfDoors;
    public Car(String brand, int numberOfDoors) {
        super(brand);
        this.numberOfDoors = numberOfDoors;
    }
    public void drive() {
        System.out.println("Car is driving with " + numberOfDoors + " doors.");
    }
}

// 5. REALIZATION (Printer interface -> LaserPrinter class)
// A class implements an interface. Represents a contract-based relationship ("implements" or "behaves-as").
interface Printer {
    void printDocument(String text);
}

class LaserPrinter implements Printer {
    private String model;
    public LaserPrinter(String model) { this.model = model; }

    @Override
    public void printDocument(String text) {
        System.out.println("LaserPrinter (" + model + ") prints: " + text);
    }
}

// 6. DEPENDENCY (OrderProcessor depends on DatabaseConnector)
// A class transiently uses another class within a method. Weakest relationship.
class DatabaseConnector {
    public void executeQuery(String query) {
        System.out.println("Executing query in DB: " + query);
    }
}

class OrderProcessor {
    // OrderProcessor depends on DatabaseConnector but does not hold it as an instance field
    public void processOrder(String orderId, DatabaseConnector connector) {
        System.out.println("Processing order: " + orderId);
        connector.executeQuery("INSERT INTO orders VALUES ('" + orderId + "', 'PROCESSED')");
    }
}

public class RelationshipDemo {
    public static void main(String[] args) {
        System.out.println("=== OBJECT RELATIONSHIPS & UML CONNECTIONS DEMO ===\n");

        // --- 1. ASSOCIATION ---
        Teacher t = new Teacher("Mr. Smith");
        Student s = new Student("Alice");
        System.out.println("Association: " + t.getName() + " is teaching " + s.getName() + ".");

        System.out.println("\n--------------------------\n");

        // --- 2. AGGREGATION ---
        Professor p1 = new Professor("Dr. Brown");
        Professor p2 = new Professor("Dr. White");
        List<Professor> profs = Arrays.asList(p1, p2);

        Department csDept = new Department("Computer Science", profs);
        csDept.display();
        System.out.println("Note (Aggregation): If 'csDept' is deleted, " + p1.getName() + " still exists independently!");

        System.out.println("\n--------------------------\n");

        // --- 3. COMPOSITION ---
        House myHouse = new House();
        myHouse.display();
        System.out.println("Note (Composition): If 'myHouse' is destroyed, its rooms are also gone.");

        System.out.println("\n--------------------------\n");

        // --- 4. GENERALIZATION (Inheritance) ---
        Car myCar = new Car("Toyota", 4);
        myCar.startEngine(); // Inherited method
        myCar.drive();       // Subclass method
        System.out.println("Note (Generalization): Car 'IS-A' Vehicle.");

        System.out.println("\n--------------------------\n");

        // --- 5. REALIZATION (Implementation) ---
        Printer myPrinter = new LaserPrinter("HP LaserJet Pro");
        myPrinter.printDocument("Designing low level architectures");
        System.out.println("Note (Realization): LaserPrinter realizes/implements the Printer contract.");

        System.out.println("\n--------------------------\n");

        // --- 6. DEPENDENCY (Uses-A) ---
        OrderProcessor processor = new OrderProcessor();
        DatabaseConnector dbConnector = new DatabaseConnector();
        // Passing dbConnector as a method parameter (transient dependency)
        processor.processOrder("ORD-10023", dbConnector);
        System.out.println("Note (Dependency): OrderProcessor uses DatabaseConnector temporarily in its method.");
    }
}
