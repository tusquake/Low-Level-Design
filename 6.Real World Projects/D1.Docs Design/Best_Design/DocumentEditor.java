package Best_Design;

class DocumentEditor {
    private Document doc;
    private Persistence storage;

    public DocumentEditor(Document doc, Persistence storage) {
        this.doc = doc;
        this.storage = storage;
    }

    public void addText(String text) {
        doc.addElement(new TextElement(text));
    }

    public void addImage(String path) {
        doc.addElement(new ImageElement(path));
    }

    public void renderDoc() {
        DocumentRenderer renderer = new DocumentRenderer(doc);
        renderer.render();
    }

    public void save() {
        storage.save(doc);
    }
}
