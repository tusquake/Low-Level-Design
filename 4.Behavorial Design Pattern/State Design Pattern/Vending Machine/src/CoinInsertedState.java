public class CoinInsertedState extends VendingMachineState {
    public CoinInsertedState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void insertCoin() {
        System.out.println("Another coin inserted");
        machine.setCoinCount(machine.getCoinCount() + 1);
    }

    @Override
    public void selectProduct() {
        if (machine.getStockCount() > 0) {
            System.out.println("Product selected");
            machine.setState(machine.getProductSelectedState());
        } else {
            System.out.println("Product out of stock");
            machine.setState(machine.getOutOfStockState());
        }
    }

    @Override
    public void dispense() {
        System.out.println("Please select a product first");
    }

    @Override
    public void refund() {
        System.out.println("Coins refunded: " + machine.getCoinCount());
        machine.setCoinCount(0);
        machine.setState(machine.getIdleState());
    }
}
