import java.util.ArrayList;
import java.util.List;

public class Forest {
    public List<Tree> trees = new ArrayList<>();

    public void plantTree(int x, int y, String color, String name, String sprite) {
        TreeType type = TreeTypeFactory.getTreeType(name, sprite);
        Tree tree = new Tree(x, y, color, type);
        trees.add(tree);
    }

    public void render() {
        System.out.println("\n--- Rendering Forest ---");
        for (Tree tree : trees) {
            tree.render();
        }
        System.out.println("Total trees: " + trees.size());
        System.out.println("Flyweight objects created: " + TreeTypeFactory.getCreatedFlyweightsCount());
    }
}
