package Better_Design;

public class Main {
    public static void main(String[] args) {
        Document doc = new Document();
        Persistence persistence = new SaveToFile("document.txt");
        DocumentEditor editor = new DocumentEditor(doc, persistence);

        editor.addText("Hello World!");
        editor.addImage("/img/cat.png");

        editor.renderDoc();
        editor.save();
    }
}

