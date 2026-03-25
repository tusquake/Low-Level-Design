/**
 * Interface Segregation Principle (ISP) - CORRECTION
 * Interfaces are split into smaller, more specific ones.
 * Clients only implement what they actually need.
 */

interface Printer {
    void print();
}

interface Scanner {
    void scan();
}

interface Fax {
    void fax();
}

// All-in-one device implements all relevant interfaces
class MultiFunctionPrinter implements Printer, Scanner {
    @Override
    public void print() {
        System.out.println("Printing...");
    }

    @Override
    public void scan() {
        System.out.println("Scanning...");
    }
}

// Basic printer only implements the Printer interface
class EconomicPrinter implements Printer {
    @Override
    public void print() {
        System.out.println("Economic Printer: Printing...");
    }
}

public class ISPCorrection {
    public static void main(String[] args) {
        Printer basic = new EconomicPrinter();
        basic.print(); // Only print() is available, no scan/fax methods to accidentally call

        Printer smart = new MultiFunctionPrinter();
        smart.print();

        // We can cast to Scanner if we need scanning functionality
        if (smart instanceof Scanner) {
            ((Scanner) smart).scan();
        }
    }
}
