/**
 * Dependency Inversion Principle (DIP) - CORRECTION
 * High-level and Low-level modules depend on an abstraction (BankCard).
 */

interface BankCard {
    void doTransaction(long amount);
}

class DebitCard implements BankCard {
    @Override
    public void doTransaction(long amount) {
        System.out.println("Payment using Debit Card: " + amount);
    }
}

class CreditCard implements BankCard {
    @Override
    public void doTransaction(long amount) {
        System.out.println("Payment using Credit Card: " + amount);
    }
}

class ShoppingMall {
    private BankCard bankCard;

    // Correction: ShoppingMall (high-level) now depends on the BankCard abstraction (interface)
    public ShoppingMall(BankCard bankCard) {
        this.bankCard = bankCard;
    }

    public void doPayment(Object order, long amount) {
        bankCard.doTransaction(amount);
    }
}

public class DIPCorrection {
    public static void main(String[] args) {
        // We can easily inject any type of BankCard into the ShoppingMall
        BankCard card = new DebitCard();
        ShoppingMall mall = new ShoppingMall(card);
        mall.doPayment("Some Order", 5000);

        // Switch to CreditCard without modifying ShoppingMall class
        card = new CreditCard();
        mall = new ShoppingMall(card);
        mall.doPayment("Another Order", 10000);
    }
}
