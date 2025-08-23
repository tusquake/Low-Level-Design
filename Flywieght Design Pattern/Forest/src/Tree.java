public class Tree {
    private int x, y;           // Extrinsic state
    private String color;       // Extrinsic state
    private TreeType treeType;  // Reference to flyweight

    public Tree(int x, int y, String color, TreeType treeType) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.treeType = treeType;
    }

    public void render() {
        treeType.render(x, y, color);
    }
}
