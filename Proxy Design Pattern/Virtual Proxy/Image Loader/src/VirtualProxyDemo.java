class VirtualProxyDemo {
    public static void main(String[] args) {
        System.out.println("=== Virtual Proxy Demo ===");

        // Image is not loaded yet (lazy loading is done to avoid costly object
        //creation operation
        Image image = new VirtualImageProxy("large_photo.jpg");
        System.out.println("Proxy created, but image not loaded yet\n");

        // Image gets loaded on first access
        image.display();
        System.out.println();

        // Subsequent calls use already loaded image
        image.display();
    }
}