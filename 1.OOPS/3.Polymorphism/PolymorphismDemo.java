import java.util.*;

/**
 * Polymorphism Demo
 * 
 * Shows both Static Binding (Overloading) and Dynamic Binding (Overriding).
 */

// 1. Runtime Polymorphism (Dynamic Binding)
interface PaymentMethod {
    void processPayment(double amount);
}

class CreditCard implements PaymentMethod {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount + "...");
    }
}

class PayPal implements PaymentMethod {
    @Override
    public void processPayment(double amount) {
        System.out.println("Redirecting to PayPal for payment of $" + amount + "...");
    }
}

// 2. Compile-time Polymorphism (Static Binding)
class Calculator {
    // Method Overloading
    public int multiply(int a, int b) {
        return a * b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }
}

public class PolymorphismDemo {
    public static void main(String[] args) {
        System.out.println("=== POLYMORPHISM DEMO ===\n");

        // --- 1. Static Binding (Overloading) ---
        Calculator calc = new Calculator();
        System.out.println("Integer Multiply: 5 * 10 = " + calc.multiply(5, 10));
        System.out.println("Double Multiply: 5.5 * 2.1 = " + calc.multiply(5.5, 2.1));

        System.out.println("\n--------------------------\n");

        // --- 2. Dynamic Binding (Overriding) ---
        // A single reference 'myPayment' takes many forms!
        PaymentMethod myPayment;

        // Form 1: CreditCard
        myPayment = new CreditCard();
        myPayment.processPayment(100.0);

        // Form 2: PayPal
        myPayment = new PayPal();
        myPayment.processPayment(250.0);

        System.out.println("\n--------------------------\n");

        // --- 3. Polymorphism in Collections ---
        List<PaymentMethod> history = new ArrayList<>();
        history.add(new CreditCard());
        history.add(new PayPal());
        history.add(new CreditCard());

        System.out.println("Processing Payment History Polymorphically:");
        for (PaymentMethod p : history) {
            p.processPayment(50.0); // JVM decides WHICH processPayment to call at runtime
        }
    }
}
