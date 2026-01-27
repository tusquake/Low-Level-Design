public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();

        System.out.println("=== Vending Machine State Pattern Demo ===\n");

        System.out.println("1. Normal Purchase Flow:");
        machine.insertCoin();
        machine.selectProduct();
        machine.dispense();

        System.out.println("\n2. Multiple Coins:");
        machine.insertCoin();
        machine.insertCoin();
        machine.selectProduct();
        machine.dispense();
        machine.dispense();

        System.out.println("\n3. Refund Test:");
        machine.insertCoin();
        machine.insertCoin();
        machine.refund();

        System.out.println("\n4. Invalid Operations:");
        machine.selectProduct();
        machine.dispense();

        System.out.println("\n5. Out of Stock Test:");
        machine.setStockCount(0);
        machine.insertCoin();
        machine.selectProduct();
    }
}