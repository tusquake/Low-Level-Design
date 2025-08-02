package Best_Design;


class ImageElement extends DocumentElement {
    private String path;

    public ImageElement(String path) {
        this.path = path;
    }

    public void render() {
        System.out.println("Image: " + path);
    }

     public String getPath() {
        return path;
    }
}