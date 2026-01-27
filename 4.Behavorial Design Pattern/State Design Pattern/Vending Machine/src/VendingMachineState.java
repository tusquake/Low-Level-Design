public abstract class VendingMachineState {
    protected VendingMachine machine;

    public VendingMachineState(VendingMachine machine) {
        this.machine = machine;
    }

    public abstract void insertCoin();
    public abstract void selectProduct();
    public abstract void dispense();
    public abstract void refund();
}
