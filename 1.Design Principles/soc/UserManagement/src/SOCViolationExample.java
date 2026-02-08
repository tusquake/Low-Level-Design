class UserManager {

    public void createUser(String name) {
        // validation
        if(name == null) {
            System.out.println("Invalid");
        }

        // business logic
        System.out.println("Creating user " + name);

        // database logic
        System.out.println("Saving user to DB");

        // notification
        System.out.println("Sending email");
    }
}


public class SOCViolationExample {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        userManager.createUser("Tushar");
    }
}