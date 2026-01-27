package Best_Design;

import java.io.FileWriter;
import java.io.IOException;

public class SaveToFile extends Persistence {
    private String filePath;

    public SaveToFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Document document) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (DocumentElement el : document.getElements()) {
                if (el instanceof TextElement) {
                    writer.write("TEXT: " + ((TextElement) el).getText() + "\n");
                } else if (el instanceof ImageElement) {
                    writer.write("IMAGE: " + ((ImageElement) el).getPath() + "\n");
                }
            }
            System.out.println("Document saved to file: " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
