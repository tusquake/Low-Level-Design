import java.util.HashMap;
import java.util.Map;

public class TreeTypeFactory {
    private static final Map<String, TreeType> treeTypes = new HashMap<>();

    public static TreeType getTreeType(String name, String sprite) {
        String key = name + "_" + sprite;
        TreeType treeType = treeTypes.get(key);

        if (treeType == null) {
            System.out.println("Creating new flyweight for: " + name);
            treeType = new TreeTypeFlyweight(name, sprite);
            treeTypes.put(key, treeType);
        } else {
            System.out.println("Reusing existing flyweight for: " + name);
        }

        return treeType;
    }

    public static int getCreatedFlyweightsCount() {
        return treeTypes.size();
    }
}