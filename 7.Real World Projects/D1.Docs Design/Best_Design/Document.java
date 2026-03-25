package Best_Design;

import java.util.ArrayList;
import java.util.List;

class Document {
    private List<DocumentElement> elements = new ArrayList<>();

    public void addElement(DocumentElement el) {
        elements.add(el);
    }

    public List<DocumentElement> getElements() {
        return elements;
    }
}


