public class ProtectionProxyDemo {
    public static void main(String[] args) {
        System.out.println("=== Protection Proxy Demo ===");
        RealDocument realDoc = new RealDocument("secret_document.txt");

        // Admin user - full access
        Document adminProxy = new ProtectionDocumentProxy(realDoc, UserRole.ADMIN);
        System.out.println("Admin user:");
        System.out.println(adminProxy.read());
        System.out.println(adminProxy.write("Updated by admin"));
        System.out.println(adminProxy.delete());
        System.out.println();

        // Regular user - read and write only
        Document userProxy = new ProtectionDocumentProxy(realDoc, UserRole.USER);
        System.out.println("Regular user:");
        System.out.println(userProxy.read());
        System.out.println(userProxy.write("Updated by user"));
        try {
            System.out.println(userProxy.delete());
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();

        // Guest user - read only
        Document guestProxy = new ProtectionDocumentProxy(realDoc, UserRole.GUEST);
        System.out.println("Guest user:");
        System.out.println(guestProxy.read());
        try {
            System.out.println(guestProxy.write("Trying to write as guest"));
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
