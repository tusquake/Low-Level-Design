class Card {
    public void makePayment() {
        System.out.println("Payment done");
    }
}

class Wallet {
    Card card = new Card();

    public Card getCard() {
        return card;
    }
}

class Person {
    Wallet wallet = new Wallet();

    public Wallet getWallet() {
        return wallet;
    }
}


public class lodViolation {
    public static void main(String[] args) {
        Person p = new Person();
        p.getWallet().getCard().makePayment();
    }
}
