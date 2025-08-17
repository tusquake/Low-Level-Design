import java.util.ArrayList;
import java.util.List;

class Directory extends FileSystemComponent {
    private List<FileSystemComponent> children;

    public Directory(String name) {
        super(name);
        this.children = new ArrayList<>();
    }

    @Override
    public long getSize() {
        long totalSize = 0;
        for (FileSystemComponent child : children) {
            totalSize += child.getSize();
        }
        return totalSize;
    }

    @Override
    public void display(int depth) {
        System.out.println(getIndent(depth) + "📁 " + name + "/ (" + getSize() + " bytes total)");
        for (FileSystemComponent child : children) {
            child.display(depth + 1);
        }
    }

    @Override
    public FileSystemComponent copy() {
        Directory copyDir = new Directory(name);
        for (FileSystemComponent child : children) {
            copyDir.add(child.copy());
        }
        return copyDir;
    }

    @Override
    public void add(FileSystemComponent component) {
        children.add(component);
        System.out.println("Added " + component.getName() + " to " + this.name);
    }

    @Override
    public void remove(FileSystemComponent component) {
        children.remove(component);
        System.out.println("Removed " + component.getName() + " from " + this.name);
    }

    @Override
    public List<FileSystemComponent> getChildren() {
        return new ArrayList<>(children);
    }

    // Directory-specific operations
    public FileSystemComponent find(String fileName) {
        for (FileSystemComponent child : children) {
            if (child.getName().equals(fileName)) {
                return child;
            }
            if (child instanceof Directory) {
                FileSystemComponent found = ((Directory) child).find(fileName);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    public List<File> getAllFiles() {
        List<File> allFiles = new ArrayList<>();
        for (FileSystemComponent child : children) {
            if (child instanceof File) {
                allFiles.add((File) child);
            } else if (child instanceof Directory) {
                allFiles.addAll(((Directory) child).getAllFiles());
            }
        }
        return allFiles;
    }

    public int getFileCount() {
        int count = 0;
        for (FileSystemComponent child : children) {
            if (child instanceof File) {
                count++;
            } else if (child instanceof Directory) {
                count += ((Directory) child).getFileCount();
            }
        }
        return count;
    }
}