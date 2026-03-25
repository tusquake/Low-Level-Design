/**
 * Liskov Substitution Principle (LSP) - CORRECTION
 * Instead of having a single-base Bird class with fly(), 
 * we split birds into different categories using interfaces.
 */

interface Bird {
    void eat();
}

interface FlyingBird extends Bird {
    void fly();
}

class Sparrow implements FlyingBird {
    @Override
    public void eat() {
        System.out.println("Sparrow is eating seeds...");
    }

    @Override
    public void fly() {
        System.out.println("Sparrow is flying high...");
    }
}

class Ostrich implements Bird {
    @Override
    public void eat() {
        System.out.println("Ostrich is eating insects...");
    }
    // Ostrich does not implement FlyingBird, so it doesn't have fly() method.
    // This avoids breaking the contract and follows LSP.
}

public class LSPCorrection {
    public static void main(String[] args) {
        // We can substitute a Sparrow where a FlyingBird or Bird is expected
        FlyingBird mySparrow = new Sparrow();
        mySparrow.eat();
        mySparrow.fly();

        // An Ostrich can only be used where a general Bird is expected
        Bird myOstrich = new Ostrich();
        myOstrich.eat();
        // Since myOstrich is a Bird (not FlyingBird), 
        // there's no fly() method and thus NO ERROR.
    }
}
