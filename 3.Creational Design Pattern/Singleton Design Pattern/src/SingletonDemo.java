public class SingletonDemo {

    static class BasicSingleton {

        private static BasicSingleton instance;

        private BasicSingleton() {
            System.out.println("Basic Singleton Created");
        }

        public static BasicSingleton getInstance() {
            if (instance == null) {
                instance = new BasicSingleton();
            }
            return instance;
        }
    }

    static class SynchronizedSingleton {

        private static SynchronizedSingleton instance;

        private SynchronizedSingleton() {
            System.out.println("Synchronized Singleton Created");
        }

        public static synchronized SynchronizedSingleton getInstance() {
            if (instance == null) {
                instance = new SynchronizedSingleton();
            }
            return instance;
        }
    }


    static class DoubleCheckedSingleton {

        private static volatile DoubleCheckedSingleton instance;

        private DoubleCheckedSingleton() {
            System.out.println("Double Checked Singleton Created");
        }

        public static DoubleCheckedSingleton getInstance() {
            if (instance == null) {
                synchronized (DoubleCheckedSingleton.class) {
                    if (instance == null) {
                        instance = new DoubleCheckedSingleton();
                    }
                }
            }
            return instance;
        }
    }

    enum EnumSingleton {
        INSTANCE;

        public void show() {
            System.out.println("Enum Singleton Method Called");
        }
    }


    public static void main(String[] args) {
        BasicSingleton b1 = BasicSingleton.getInstance();
        BasicSingleton b2 = BasicSingleton.getInstance();
        System.out.println("Basic same instance: " + (b1 == b2));

        SynchronizedSingleton s1 = SynchronizedSingleton.getInstance();
        SynchronizedSingleton s2 = SynchronizedSingleton.getInstance();
        System.out.println("Synchronized same instance: " + (s1 == s2));

        DoubleCheckedSingleton d1 = DoubleCheckedSingleton.getInstance();
        DoubleCheckedSingleton d2 = DoubleCheckedSingleton.getInstance();
        System.out.println("Double Checked same instance: " + (d1 == d2));

        EnumSingleton e1 = EnumSingleton.INSTANCE;
        EnumSingleton e2 = EnumSingleton.INSTANCE;
        System.out.println("Enum same instance: " + (e1 == e2));
        e1.show();
    }
}