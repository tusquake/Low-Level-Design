public class VirtualImageProxy implements Image {
    private String filename;
    private RealImage realImage;

    public VirtualImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            System.out.println("First access - creating real image...");
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}