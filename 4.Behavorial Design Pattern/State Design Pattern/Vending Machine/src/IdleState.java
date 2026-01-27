public class IdleState extends VendingMachineState {
    public IdleState(VendingMachine machine) {
        super(machine);
    }

    @Override
    public void insertCoin() {
        System.out.println("Coin inserted");
        machine.setCoinCount(machine.getCoinCount() + 1);
        machine.setState(machine.getCoinInsertedState());
    }

    @Override
    public void selectProduct() {
        System.out.println("Please insert coin first");
    }

    @Override
    public void dispense() {
        System.out.println("Please insert coin and select product first");
    }

    @Override
    public void refund() {
        System.out.println("No coins to refund");
    }
}