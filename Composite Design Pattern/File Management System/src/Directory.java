class Directory extends FileSystemComponent {
    private List<FileSystemComponent> children = new ArrayList<>();

    Directory(String name) {
        super(name);
    }

    void add(FileSystemComponent c) {
        children.add(c);
    }

    long getSize() {
        return children.stream().mapToLong(FileSystemComponent::getSize).sum();
    }

    void display(int d) {
        System.out.println(indent(d) + "📁 " + name);
        for (FileSystemComponent c : children) {
            c.display(d + 1);
        }
    }
}