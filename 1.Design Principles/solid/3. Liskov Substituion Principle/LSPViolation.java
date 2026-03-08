/**
 * Liskov Substitution Principle (LSP) - VIOLATION
 * Objects of a superclass should be replaceable with objects of its subclasses 
 * without affecting the correctness of the program.
 */

class Bird {
    public void fly() {
        System.out.println("Bird is flying...");
    }
}

class Sparrow extends Bird {
    // Sparrow can fly, so it follows LSP
}

class Ostrich extends Bird {
    @Override
    public void fly() {
        // Violation: Ostrich cannot fly!
        // Throwing an exception changes the expected behavior of the base class.
        throw new UnsupportedOperationException("Ostrich cannot fly");
    }
}

public class LSPViolation {
    public static void main(String[] args) {
        Bird myBird = new Sparrow();
        myBird.fly(); // Works fine

        Bird myOstrich = new Ostrich();
        try {
            // This will crash the program if not handled, 
            // even though it's technically a "Bird"
            myOstrich.fly(); 
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
