public class CompositePatternDemo {
    public static void main(String[] args) {
        Directory root = new Directory("Root");
        Directory docs = new Directory("Documents");

        root.add(docs);
        root.add(new FileItem("readme.txt", 200));
        docs.add(new FileItem("resume.pdf", 1000));
        docs.add(new FileItem("notes.txt", 300));

        root.display(0);
        System.out.println("Total size: " + root.getSize());
    }
}