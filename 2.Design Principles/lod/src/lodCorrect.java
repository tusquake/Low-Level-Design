class Cards {
    public void makePayment() {
        System.out.println("Payment done");
    }
}

class Wallets {
    private Card card = new Card();

    public void pays() {
        card.makePayment();
    }
}

class Persons {
    private Wallets wallet = new Wallets();

    public void makePayment() {
        wallet.pays();
    }
}



public class lodCorrect {
    public static void main(String[] args) {
        Persons p = new Persons();
        p.makePayment();
    }
}
