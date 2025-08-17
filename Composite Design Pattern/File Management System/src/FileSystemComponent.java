import java.util.*;

// Component - Abstract base class
abstract class FileSystemComponent {
    protected String name;

    public FileSystemComponent(String name) {
        this.name = name;
    }

    // Common operations
    public String getName() {
        return name;
    }

    // Operations that should be implemented by both Leaf and Composite
    public abstract long getSize();
    public abstract void display(int depth);
    public abstract FileSystemComponent copy();

    // Operations for Composite only (default implementations)
    public void add(FileSystemComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf component");
    }

    public void remove(FileSystemComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf component");
    }

    public List<FileSystemComponent> getChildren() {
        throw new UnsupportedOperationException("Leaf components don't have children");
    }

    // Utility method for display indentation
    protected String getIndent(int depth) {
        return "  ".repeat(depth);
    }
}
