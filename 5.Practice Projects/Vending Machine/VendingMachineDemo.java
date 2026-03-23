/**
 * State Pattern: Vending Machine System
 * 
 * --- Simple Analogy ---
 * Vending Machine:
 * - No Coin State: You can't press a product button.
 * - Has Coin State: You can select a product or eject coin.
 * - Dispensing State: Machine gives you the product (cannot press other buttons).
 * - Out of Stock State: Shows error, doesn't take coins.
 * 
 * The behavior of the machine changes completely based on its current internal state.
 * 
 * --- Key Benefits ---
 * - Removes if-else / switch: No more if (state == HAS_COIN) checks in every method.
 * - Easy to Add States: Adding a "MaintenanceState" is easy without breaking existing code.
 * - Context Stays Clean: The VendingMachine doesn't need to know how to handle a coin; it just delegates to the state.
 */

public class VendingMachineDemo {

    interface VendingState {
        void insertCoin();
        void ejectCoin();
        void selectProduct();
        void dispense();
    }

    static class NoCoinState implements VendingState {
        VendingMachine machine;

        public NoCoinState(VendingMachine machine) {
            this.machine = machine;
        }

        @Override
        public void insertCoin() {
            System.out.println("Coin inserted.");
            machine.setState(machine.getHasCoinState());
        }

        @Override
        public void ejectCoin() {
            System.out.println("You haven't inserted a coin.");
        }

        @Override
        public void selectProduct() {
            System.out.println("Insert coin first.");
        }

        @Override
        public void dispense() {
            System.out.println("Pay first.");
        }
    }

    static class HasCoinState implements VendingState {
        VendingMachine machine;

        public HasCoinState(VendingMachine machine) {
            this.machine = machine;
        }

        @Override
        public void insertCoin() {
            System.out.println("Coin already inserted.");
        }

        @Override
        public void ejectCoin() {
            System.out.println("Coin returned.");
            machine.setState(machine.getNoCoinState());
        }

        @Override
        public void selectProduct() {
            System.out.println("Product selected.");
            machine.setState(machine.getDispensingState());
        }

        @Override
        public void dispense() {
            System.out.println("Select product first.");
        }
    }

    static class DispensingState implements VendingState {
        VendingMachine machine;

        public DispensingState(VendingMachine machine) {
            this.machine = machine;
        }

        @Override
        public void insertCoin() {
            System.out.println("Please wait, dispensing...");
        }

        @Override
        public void ejectCoin() {
            System.out.println("Too late, already dispensing.");
        }

        @Override
        public void selectProduct() {
            System.out.println("Already dispensing.");
        }

        @Override
        public void dispense() {
            machine.releaseProduct();
            if (machine.getCount() > 0) {
                machine.setState(machine.getNoCoinState());
            } else {
                System.out.println("Out of products!");
                machine.setState(machine.getSoldOutState());
            }
        }
    }

    static class SoldOutState implements VendingState {
        VendingMachine machine;

        public SoldOutState(VendingMachine machine) {
            this.machine = machine;
        }

        @Override
        public void insertCoin() {
            System.out.println("Machine is sold out.");
        }

        @Override
        public void ejectCoin() {
            System.out.println("No coin to eject.");
        }

        @Override
        public void selectProduct() {
            System.out.println("Product is sold out.");
        }

        @Override
        public void dispense() {
            System.out.println("Nothing to dispense.");
        }
    }

    static class VendingMachine {
        private VendingState noCoinState;
        private VendingState hasCoinState;
        private VendingState dispensingState;
        private VendingState soldOutState;

        private VendingState currentState;
        private int count = 0;

        public VendingMachine(int productCount) {
            noCoinState = new NoCoinState(this);
            hasCoinState = new HasCoinState(this);
            dispensingState = new DispensingState(this);
            soldOutState = new SoldOutState(this);

            this.count = productCount;
            if (productCount > 0) {
                currentState = noCoinState;
            } else {
                currentState = soldOutState;
            }
        }

        public void insertCoin() { currentState.insertCoin(); }
        public void ejectCoin() { currentState.ejectCoin(); }
        public void selectProduct() { 
            currentState.selectProduct(); 
            currentState.dispense(); 
        }

        void releaseProduct() {
            System.out.println("A product is rolling out...");
            if (count != 0) count--;
        }

        public void setState(VendingState state) { this.currentState = state; }
        public VendingState getNoCoinState() { return noCoinState; }
        public VendingState getHasCoinState() { return hasCoinState; }
        public VendingState getDispensingState() { return dispensingState; }
        public VendingState getSoldOutState() { return soldOutState; }
        public int getCount() { return count; }
    }

    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine(2);

        System.out.println("--- Buying First Item ---");
        machine.insertCoin();
        machine.selectProduct();

        System.out.println("\n--- Trying to Eject Without Coin ---");
        machine.ejectCoin();

        System.out.println("\n--- Buying Second Item ---");
        machine.insertCoin();
        machine.selectProduct();

        System.out.println("\n--- Trying to Buy When Sold Out ---");
        machine.insertCoin();
    }
}
