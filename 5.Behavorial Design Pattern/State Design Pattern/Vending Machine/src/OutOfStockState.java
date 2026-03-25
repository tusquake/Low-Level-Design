public class OutOfStockState extends VendingMachineState {
    public OutOfStockState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void insertCoin() {
        System.out.println("Machine is out of stock. Coin returned.");
    }

    @Override
    public void selectProduct() {
        System.out.println("Sorry, out of stock");
    }

    @Override
    public void dispense() {
        System.out.println("Cannot dispense - out of stock");
    }

    @Override
    public void refund() {
        if (machine.getCoinCount() > 0) {
            System.out.println("Coins refunded: " + machine.getCoinCount());
            machine.setCoinCount(0);
        } else {
            System.out.println("No coins to refund");
        }
        machine.setState(machine.getIdleState());
    }
}
