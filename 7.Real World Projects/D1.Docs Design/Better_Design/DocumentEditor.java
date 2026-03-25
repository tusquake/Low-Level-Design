package Better_Design;

public class DocumentEditor {
    private Document doc;
    private Persistence persistence;

    public DocumentEditor(Document doc, Persistence persistence) {
        this.doc = doc;
        this.persistence = persistence;
    }

    public void addText(String text) {
        doc.addElement(new TextElement(text));
    }

    public void addImage(String path) {
        doc.addElement(new ImageElement(path));
    }

    public void renderDoc() {
        doc.render();
    }

    public void save() {
        persistence.save(doc);
    }
}

