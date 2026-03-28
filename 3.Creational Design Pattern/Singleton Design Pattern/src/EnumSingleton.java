/**
 * An Enum-based Singleton is the modern, preferred way to implement a Singleton in Java.
 * It is naturally protected from Reflection, Serialization, and Cloning attacks by the Java Runtime Environment (JVM).
 */
public enum EnumSingleton {
    INSTANCE;

    // Add any necessary methods here
    public void showMessage() {
        System.out.println("Enum Singleton: Method Called Successfully.");
    }
}
