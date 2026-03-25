import java.util.*;

/**
 * Object Relationships Demo
 * 
 * Shows Association, Aggregation, and Composition.
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

public class RelationshipDemo {
    public static void main(String[] args) {
        System.out.println("=== OBJECT RELATIONSHIPS DEMO ===\n");

        // --- ASSOCIATION ---
        Teacher t = new Teacher("Mr. Smith");
        Student s = new Student("Alice");
        System.out.println("Association: " + t.getName() + " is teaching " + s.getName() + ".");

        System.out.println("\n--------------------------\n");

        // --- AGGREGATION ---
        Professor p1 = new Professor("Dr. Brown");
        Professor p2 = new Professor("Dr. White");
        List<Professor> profs = Arrays.asList(p1, p2);

        Department csDept = new Department("Computer Science", profs);
        csDept.display();
        System.out.println("Note: If 'csDept' is deleted, " + p1.getName() + " still exists!");

        System.out.println("\n--------------------------\n");

        // --- COMPOSITION ---
        House myHouse = new House();
        myHouse.display();
        System.out.println("Note: If 'myHouse' is destroyed, its rooms are also gone.");
    }
}
