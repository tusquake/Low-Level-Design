package Best_Design;

public class SaveToDatabase extends Persistence {
    @Override
    public void save(Document document) {
        System.out.println("Saving document to database...");
        for (DocumentElement el : document.getElements()) {
            if (el instanceof TextElement) {
                System.out.println("Inserting TEXT: " + ((TextElement) el).getText());
            } else if (el instanceof ImageElement) {
                System.out.println("Inserting IMAGE: " + ((ImageElement) el).getPath());
            }
        }
        System.out.println("Document saved to database.");
    }
}
