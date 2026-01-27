public class RealDocument implements Document {
    private String name;
    private String content;

    public RealDocument(String name) {
        this.name = name;
        this.content = "Content of " + name;
    }

    @Override
    public String read() {
        return "Reading: " + content;
    }

    @Override
    public String write(String content) {
        this.content = content;
        return "Writing to " + name + ": " + content;
    }

    @Override
    public String delete() {
        return "Deleting document: " + name;
    }
}
