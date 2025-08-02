package Bad_Design;

import java.util.ArrayList;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentEditor {

    private List<String> elements = new ArrayList<>();

    public void addText(String text) {
        elements.add("TEXT:" + text);
    }

    public void addImage(String path) {
        elements.add("IMAGE:" + path);
    }

    public void renderDocument() {
        for (String element : elements) {
            if (element.startsWith("TEXT:")) {
                System.out.println("Rendering Text: " + element.substring(5));
            } else if (element.startsWith("IMAGE:")) {
                System.out.println("Rendering Image from path: " + element.substring(6));
            }
        }
    }

    public void saveToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String element : elements) {
                writer.write(element + "\n");
            }
            System.out.println("Document saved to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save file: " + e.getMessage());
        }
    }
}
