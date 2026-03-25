/**
 * Interface Segregation Principle (ISP) - VIOLATION
 * No client should be forced to depend on methods it does not use.
 */

interface SmartDevice {
    void print();
    void scan();
    void fax();
}

class AllInOnePrinter implements SmartDevice {
    @Override
    public void print() {
        System.out.println("Printing...");
    }

    @Override
    public void scan() {
        System.out.println("Scanning...");
    }

    @Override
    public void fax() {
        System.out.println("Faxing...");
    }
}

class BasicPrinter implements SmartDevice {
    @Override
    public void print() {
        System.out.println("Basic Printer: Printing...");
    }

    @Override
    public void scan() {
        // Violation: Basic printer cannot scan!
        throw new UnsupportedOperationException("Scan not supported");
    }

    @Override
    public void fax() {
        // Violation: Basic printer cannot fax!
        throw new UnsupportedOperationException("Fax not supported");
    }
}

public class ISPViolation {
    public static void main(String[] args) {
        SmartDevice device = new BasicPrinter();
        device.print();
        
        try {
            // This will throw an exception because the interface is too "fat"
            device.scan();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
