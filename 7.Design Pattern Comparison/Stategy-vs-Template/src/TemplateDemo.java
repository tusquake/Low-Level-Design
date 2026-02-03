import javax.xml.crypto.Data;

abstract class DataProcessor {

    public final void process() {
        readData();
        processData();
        saveData();
    }

    protected abstract void readData();
    protected abstract void processData();

    protected void saveData() {
        System.out.println("Saving data to database");
    }
}

class CSVProcessor extends DataProcessor {

    protected void readData() {
        System.out.println("Reading CSV file");
    }

    protected void processData() {
        System.out.println("Processing CSV data");
    }
}

class XMLProcessor extends DataProcessor {

    protected void readData() {
        System.out.println("Reading XML file");
    }

    protected void processData() {
        System.out.println("Processing XML data");
    }
}


public class TemplateDemo {
    public static void main(String[] args) {
        DataProcessor dataProcessor = new CSVProcessor();
        dataProcessor.process();

        dataProcessor = new XMLProcessor();
        dataProcessor.process();
    }
}
