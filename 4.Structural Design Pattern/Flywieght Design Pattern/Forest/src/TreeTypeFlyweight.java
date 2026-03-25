public class TreeTypeFlyweight implements TreeType {
    private final String name;        // Intrinsic state
    private final String sprite;      // Intrinsic state

    public TreeTypeFlyweight(String name, String sprite) {
        this.name = name;
        this.sprite = sprite;
    }

    @Override
    public void render(int x, int y, String color) {
        // Use intrinsic state (name, sprite) and extrinsic state (x, y, color)
        System.out.println("Rendering " + name + " tree with " + sprite +
                " sprite at (" + x + "," + y + ") in " + color + " color");
    }
}
