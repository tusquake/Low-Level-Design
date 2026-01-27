import java.util.ArrayList;
import java.util.List;

public class DocumentTemplate implements Cloneable {
    private String templateName;
    private String header;
    private String footer;
    private List<String> sections;

    public DocumentTemplate(String templateName) {
        this.templateName = templateName;
        this.sections = new ArrayList<>();

        // Expensive template loading
        System.out.println("Loading template: " + templateName + " from disk...");
        loadTemplate();
    }

    private void loadTemplate() {
        this.header = "Standard Company Header";
        this.footer = "© 2024 Company Name";
        sections.add("Title Section");
        sections.add("Content Section");
    }

    @Override
    public DocumentTemplate clone() {
        try {
            DocumentTemplate cloned = (DocumentTemplate) super.clone();
            cloned.sections = new ArrayList<>(this.sections);
            System.out.println("Cloning template: " + templateName);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void customizeForClient(String clientName) {
        this.header = "Document for " + clientName;
        this.sections.add("Client-specific section for " + clientName);
    }

    public void generate() {
        System.out.println("=== Generated Document ===");
        System.out.println("Header: " + header);
        System.out.println("Sections: " + sections);
        System.out.println("Footer: " + footer);
        System.out.println("========================");
    }
}
