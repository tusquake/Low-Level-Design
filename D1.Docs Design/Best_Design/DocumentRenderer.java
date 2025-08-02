package Best_Design;

public class DocumentRenderer {
    private Document doc;

    public DocumentRenderer(Document doc) {
        this.doc = doc;
    }

    public void render() {
        for (DocumentElement element : doc.getElements()) {
           System.out.println("Document Rendered");
        }
    }
}
