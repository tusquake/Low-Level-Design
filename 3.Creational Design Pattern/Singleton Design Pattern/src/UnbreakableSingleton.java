import java.io.Serializable;

/**
 * A "Boss Level" Singleton implementation that is protected against common Java "backdoors":
 * 1. Reflection
 * 2. Serialization
 * 3. Cloning
 * 
 * It also uses Thread-safe Double-Checked Locking for performance.
 */
public class UnbreakableSingleton implements Serializable, Cloneable {

    // Using volatile for thread safety in double-checked locking
    private static volatile UnbreakableSingleton instance;

    // 1. Reflection Guard: Throw exception if constructor is called when instance already exists
    private UnbreakableSingleton() {
        if (instance != null) {
            throw new RuntimeException("Singleton instance already created. Use getInstance() method.");
        }
    }

    // Double-Checked Locking for efficiency and thread safety
    public static UnbreakableSingleton getInstance() {
        if (instance == null) {
            synchronized (UnbreakableSingleton.class) {
                if (instance == null) {
                    instance = new UnbreakableSingleton();
                }
            }
        }
        return instance;
    }

    // 2. Serialization Guard: This method is called during deserialization.
    // By returning the existing instance, we prevent a new one from being created.
    protected Object readResolve() {
        return getInstance();
    }

    // 3. Cloning Guard: Override clone and throw Exception
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Prevents creating a copy of the singleton
        throw new CloneNotSupportedException("Singleton cannot be cloned.");
    }
}
