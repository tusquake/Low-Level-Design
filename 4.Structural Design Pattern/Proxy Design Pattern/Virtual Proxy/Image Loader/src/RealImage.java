public class RealImage implements Image {
    private String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading " + filename + " from disk...");
        try {
            Thread.sleep(2000); // Simulate expensive loading operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(filename + " loaded successfully!");
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}
