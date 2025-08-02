package Better_Design;

public class ImageElement implements DocumentElement {
    private String path;

    public ImageElement(String path) {
        this.path = path;
    }

    @Override
    public void render() {
        System.out.println("Rendering Image from: " + path);
    }

    public String getPath() {
        return path;
    }
}
