class PDFCreator extends DocumentCreator {
    @Override
    public PDFDocument createDocument(String title) {
        return new PDFDocument(title);
    }
}