package Best_Design;

public class Main {
    public static void main(String[] args) {
        // Step 1: Create a new Document
        Document doc = new Document();

        // Step 2: Choose your persistence strategy (file or database)
        Persistence fileSaver = new SaveToFile("document.txt");
        Persistence dbSaver = new SaveToDatabase();

        // Step 3: Use DocumentEditor with selected persistence
        DocumentEditor editor1 = new DocumentEditor(doc, fileSaver);
        DocumentEditor editor2 = new DocumentEditor(doc, dbSaver);

        // Step 4: Add elements
        editor1.addText("This is the first line.");
        editor1.addImage("/images/photo1.jpg");
        editor1.addText("This is the second line.");
        editor1.addImage("/images/photo2.jpg");

        // Step 5: Render the document
        System.out.println("\n--- Document Preview ---");
        editor1.renderDoc();

        // Step 6: Save to file
        System.out.println("\n--- Saving to File ---");
        editor1.save();

        // Step 7: Save to database (mock)
        System.out.println("\n--- Saving to Database ---");
        editor2.save();
    }
}
