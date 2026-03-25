public interface Document {
    String read();
    String write(String content);
    String delete();
}
