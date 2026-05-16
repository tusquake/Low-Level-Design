public class Product {
    private String name;
    private Color color;
    private Size size;

    public Product(String name, Color color, Size size) {
        this.name = name;
        this.color = color;
        this.size = size;
    }

    public String getName() { return name; }
    public Color getColor() { return color; }
    public Size getSize() { return size; }

    @Override
    public String toString() {
        return name + " (" + color + ", " + size + ")";
    }
}

enum Color { RED, GREEN, BLUE }
enum Size { SMALL, MEDIUM, LARGE }
