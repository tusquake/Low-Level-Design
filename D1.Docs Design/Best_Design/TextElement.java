package Best_Design;

class TextElement extends DocumentElement {
    private String text;

    public TextElement(String text) {
        this.text = text;
    }

    public void render() {
        System.out.println("Text: " + text);
    }

    public String getText() {
        return text;
    }
}
