public class ProtectionDocumentProxy implements Document {
    private RealDocument document;
    private UserRole userRole;

    public ProtectionDocumentProxy(RealDocument document, UserRole userRole) {
        this.document = document;
        this.userRole = userRole;
    }

    private boolean hasPermission(String operation) {
        switch (userRole) {
            case ADMIN:
                return true; // Admin has all permissions
            case USER:
                return operation.equals("read") || operation.equals("write");
            case GUEST:
                return operation.equals("read");
            default:
                return false;
        }
    }

    private void checkAccess(String operation) {
        if (!hasPermission(operation)) {
            throw new SecurityException("Access denied: " + userRole + " cannot " + operation);
        }
    }

    @Override
    public String read() {
        checkAccess("read");
        return document.read();
    }

    @Override
    public String write(String content) {
        checkAccess("write");
        return document.write(content);
    }

    @Override
    public String delete() {
        checkAccess("delete");
        return document.delete();
    }
}