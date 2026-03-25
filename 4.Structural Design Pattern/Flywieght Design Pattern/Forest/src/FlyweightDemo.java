public class FlyweightDemo {
    public static void main(String[] args) {
        Forest forest = new Forest();

        // Plant many trees of same types
        forest.plantTree(1, 1, "Green", "Oak", "oak_sprite.png");
        forest.plantTree(2, 3, "Brown", "Oak", "oak_sprite.png");
        forest.plantTree(5, 2, "Green", "Pine", "pine_sprite.png");
        forest.plantTree(3, 4, "Dark Green", "Oak", "oak_sprite.png");
        forest.plantTree(7, 1, "Green", "Pine", "pine_sprite.png");
        forest.plantTree(4, 6, "Brown", "Birch", "birch_sprite.png");
        forest.plantTree(8, 5, "Green", "Oak", "oak_sprite.png");
        forest.plantTree(6, 3, "Light Green", "Pine", "pine_sprite.png");

        // Render the forest
        forest.render();

        System.out.println("\nMemory saved: Instead of " + forest.trees.size() +
                " tree type objects, we only created " +
                TreeTypeFactory.getCreatedFlyweightsCount() + " flyweights!");
    }
}
