public class CompositePatternDemo {
    public static void main(String[] args) {
        System.out.println("=== Composite Pattern Demo ===\n");

        // Create file system structure
        Directory root = new Directory("Root");
        Directory documents = new Directory("Documents");

        root.add(documents);
        root.add(new File("readme.txt", 256));

        documents.add(new File("resume.pdf", 1024));
        documents.add(new File("project.java", 4096));

        // Display structure
        System.out.println("File System:");
        root.display(0);

        // Demonstrate uniform treatment
        System.out.println("\n=== Uniform Operations ===");
        showInfo(root);           // Directory
        showInfo(documents);      // Directory
        showInfo(new File("test.txt", 512)); // File

        // Show total size
        System.out.println("Total size: " + root.getSize() + " bytes");
    }

    private static void showInfo(FileSystemComponent component) {
        System.out.println(component.getName() + ": " + component.getSize() + " bytes");
    }
}