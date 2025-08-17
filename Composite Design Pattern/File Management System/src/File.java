class File extends FileSystemComponent {
    private long size;
    private String extension;

    public File(String name, long size) {
        super(name);
        this.size = size;
        this.extension = getFileExtension(name);
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void display(int depth) {
        System.out.println(getIndent(depth) + "📄 " + name + " (" + size + " bytes)");
    }

    @Override
    public FileSystemComponent copy() {
        return new File(name, size);
    }

    public String getExtension() {
        return extension;
    }

    // File-specific operations
    public void open() {
        System.out.println("Opening file: " + name);
    }

    public void edit(String content) {
        System.out.println("Editing file: " + name);
        // Simulate size change
        this.size = content.length();
    }
}