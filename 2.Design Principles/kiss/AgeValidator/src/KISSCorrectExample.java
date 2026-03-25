public class KISSCorrectExample {

    public static void main(String[] args) {
        int age = 20;
        System.out.println("Is Adult: " + isAdult(age));
    }

    private static boolean isAdult(int age) {
        return age >= 18;
    }
}
