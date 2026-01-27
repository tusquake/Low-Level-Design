package Bad_Design;

public class Main {
    public static void main(String[] args) {
        DocumentEditor editor = new DocumentEditor();

        editor.addText("Hello, this is a test document.");
        editor.addImage("/images/sample.png");
        editor.addText("This is the second paragraph.");
        editor.addImage("/images/diagram.jpg");

        editor.renderDocument();

        editor.saveToFile("document.txt");
    }
    
}

