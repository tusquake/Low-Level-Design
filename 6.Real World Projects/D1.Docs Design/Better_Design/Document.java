package Better_Design;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private List<DocumentElement> elements = new ArrayList<>();

    public void addElement(DocumentElement el) {
        elements.add(el);
    }

    public void render() {
        for (DocumentElement el : elements) {
            el.render();
        }
    }

    public List<DocumentElement> getElements() {
        return elements;
    }
}

