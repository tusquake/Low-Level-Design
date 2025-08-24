public class ProductSelectedState extends VendingMachineState {
    public ProductSelectedState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void insertCoin() {
        System.out.println("Product already selected, processing...");
    }

    @Override
    public void selectProduct() {
        System.out.println("Product already selected");
    }

    @Override
    public void dispense() {
        if (machine.getCoinCount() >= 1 && machine.getStockCount() > 0) {
            System.out.println("Product dispensed! Enjoy your purchase!");
            machine.setCoinCount(machine.getCoinCount() - 1);
            machine.setStockCount(machine.getStockCount() - 1);

            if (machine.getCoinCount() > 0) {
                System.out.println("Remaining coins: " + machine.getCoinCount());
                machine.setState(machine.getCoinInsertedState());
            } else {
                machine.setState(machine.getIdleState());
            }
        } else {
            System.out.println("Unable to dispense product");
            machine.setState(machine.getIdleState());
        }
    }

    @Override
    public void refund() {
        System.out.println("Coins refunded: " + machine.getCoinCount());
        machine.setCoinCount(0);
        machine.setState(machine.getIdleState());
    }
}