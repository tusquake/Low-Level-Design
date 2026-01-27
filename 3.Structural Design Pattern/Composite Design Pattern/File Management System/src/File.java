class FileItem extends FileSystemComponent {
    private long size;

    FileItem(String name, long size) {
        super(name);
        this.size = size;
    }

    long getSize() {
        return size;
    }

    void display(int d) {
        System.out.println(indent(d) + "📄 " + name + " (" + size + ")");
    }
}