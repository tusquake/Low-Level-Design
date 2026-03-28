import java.io.*;
import java.lang.reflect.Constructor;

/**
 * Demo class to verify that the Unbreakable Singleton remains unbreakable.
 */
public class UnbreakableSingletonDemo {

    public static void main(String[] args) {
        UnbreakableSingleton instance1 = UnbreakableSingleton.getInstance();

        System.out.println("--- Singleton Verification Demo ---\n");

        // 1. Reflection Attack
        System.out.println("1. Attempting Reflection Attack...");
        try {
            Constructor<UnbreakableSingleton> constructor = UnbreakableSingleton.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            UnbreakableSingleton instanceReflected = constructor.newInstance();
            System.out.println("FAILURE: Reflection attack succeeded (New Instance Created!)");
        } catch (Exception e) {
            System.out.println("SUCCESS: Reflection attack failed. Error: " + e.getCause().getMessage());
        }

        // 2. Serialization Attack
        System.out.println("\n2. Attempting Serialization Attack...");
        try {
            // Serialize
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("singleton.ser"));
            oos.writeObject(instance1);
            oos.close();

            // Deserialize
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("singleton.ser"));
            UnbreakableSingleton instanceDeserialized = (UnbreakableSingleton) ois.readObject();
            ois.close();

            System.out.println("Instance 1 HashCode: " + instance1.hashCode());
            System.out.println("Instance Deserialized HashCode: " + instanceDeserialized.hashCode());

            if (instance1 == instanceDeserialized) {
                System.out.println("SUCCESS: Serialization attack failed (Same instances!)");
            } else {
                System.out.println("FAILURE: Serialization attack succeeded (New Instance Created!)");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Serialization error occurred: " + e.getMessage());
        }

        // 3. Cloning Attack
        System.out.println("\n3. Attempting Cloning Attack...");
        try {
            // Java's Object.clone() is protected, so we access it via reflection for the demo if not overridden,
            // but we'll try to use the method if it was public (UnbreakableSingleton keeps it protected but throws).
            // Actually, simply calling clone() on the object directly works if we're in the same package or if public.
            // Since it's protected, we need to handle it.
            // Let's create a proxy or subclass? No, we just try to call it via reflection to show the guard.
            java.lang.reflect.Method cloneMethod = UnbreakableSingleton.class.getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);
            UnbreakableSingleton instanceCloned = (UnbreakableSingleton) cloneMethod.invoke(instance1);
            System.out.println("FAILURE: Cloning attack succeeded (New Instance Created!)");
        } catch (Exception e) {
            System.out.println("SUCCESS: Cloning attack failed. Error: " + e.getCause().getMessage());
        }

        // 4. Enum Singleton Verification
        System.out.println("\n4. Verifying Enum Singleton...");
        EnumSingleton enumInstance1 = EnumSingleton.INSTANCE;
        EnumSingleton enumInstance2 = EnumSingleton.INSTANCE;
        System.out.println("Enum Instance 1 HashCode: " + enumInstance1.hashCode());
        System.out.println("Enum Instance 2 HashCode: " + enumInstance2.hashCode());
        if (enumInstance1 == enumInstance2) {
             System.out.println("SUCCESS: Enum Singleton works as expected.");
             enumInstance1.showMessage();
        }

        // Cleanup
        new File("singleton.ser").delete();
    }
}
