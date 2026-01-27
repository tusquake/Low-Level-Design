public class VendingMachine {
    private VendingMachineState currentState;
    private VendingMachineState idleState;
    private VendingMachineState coinInsertedState;
    private VendingMachineState productSelectedState;
    private VendingMachineState outOfStockState;

    private int coinCount = 0;
    private int stockCount = 5;

    public VendingMachine() {
        idleState = new IdleState(this);
        coinInsertedState = new CoinInsertedState(this);
        productSelectedState = new ProductSelectedState(this);
        outOfStockState = new OutOfStockState(this);

        currentState = idleState;
    }

    public void setState(VendingMachineState state) {
        this.currentState = state;
        System.out.println("State changed to: " + state.getClass().getSimpleName());
    }

    public void insertCoin() { currentState.insertCoin(); }
    public void selectProduct() { currentState.selectProduct(); }
    public void dispense() { currentState.dispense(); }
    public void refund() { currentState.refund(); }

    public VendingMachineState getIdleState() { return idleState; }
    public VendingMachineState getCoinInsertedState() { return coinInsertedState; }
    public VendingMachineState getProductSelectedState() { return productSelectedState; }
    public VendingMachineState getOutOfStockState() { return outOfStockState; }

    public int getCoinCount() { return coinCount; }
    public void setCoinCount(int coinCount) { this.coinCount = coinCount; }
    public int getStockCount() { return stockCount; }
    public void setStockCount(int stockCount) { this.stockCount = stockCount; }
}
