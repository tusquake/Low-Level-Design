package Better_Design;

public class TextElement implements DocumentElement {
    private String text;

    public TextElement(String text) {
        this.text = text;
    }

    @Override
    public void render() {
        System.out.println("Rendering Text: " + text);
    }

    public String getText() {
        return text;
    }
}
