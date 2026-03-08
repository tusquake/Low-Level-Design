/**
 * Dependency Inversion Principle (DIP) - VIOLATION
 * High-level modules should not depend on low-level modules. 
 * Both should depend on abstractions.
 */

class DebitCard {
    public void doTransaction(long amount) {
        System.out.println("Payment using Debit Card: " + amount);
    }
}

class CreditCard {
    public void doTransaction(long amount) {
        System.out.println("Payment using Credit Card: " + amount);
    }
}

class ShoppingMall {
    private DebitCard debitCard;

    // Violation: ShoppingMall (high-level) is tightly coupled to DebitCard (low-level)
    public ShoppingMall(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public void doPayment(Object order, long amount) {
        debitCard.doTransaction(amount);
    }
}

public class DIPViolation {
    public static void main(String[] args) {
        DebitCard debitCard = new DebitCard();
        ShoppingMall mall = new ShoppingMall(debitCard);
        mall.doPayment("Some Order", 5000);
        
        // If we want to use CreditCard, we have to modify the ShoppingMall class!
    }
}
