public class PrinterDemo {
    public static void main(String[] args) {
        Printable p = new BasicPrinter();
        p.print();

        AllinOnePrinter al = new AllinOnePrinter();
        al.print();
        al.fax();
        al.scan();
    }
}

interface Printable {
    void print();
}

interface Scannable {
    void scan();
}

interface Faxable {
    void fax();
}

class BasicPrinter implements Printable{

    @Override
    public void print() {
        System.out.println("Basic Printer");
    }
}

class AllinOnePrinter implements Printable,Scannable,Faxable{

    @Override
    public void fax() {
        System.out.println("Faxing............");
    }

    @Override
    public void print() {
        System.out.println("Printing...........");
    }

    @Override
    public void scan() {
        System.out.println("Scanning...........");
    }
}
