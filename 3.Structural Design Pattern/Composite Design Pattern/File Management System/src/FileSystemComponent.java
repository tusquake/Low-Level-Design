import java.util.*;

abstract class FileSystemComponent {
    protected String name;

    FileSystemComponent(String name) {
        this.name = name;
    }

    abstract long getSize();
    abstract void display(int depth);

    void add(FileSystemComponent c) {
        throw new UnsupportedOperationException();
    }

    protected String indent(int d) {
        return "  ".repeat(d);
    }
}
