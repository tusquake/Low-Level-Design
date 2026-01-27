class Users {
    String name;
    String email;
}

class UsersService {

    public void saveUser(Users user) {
        saveToDatabase(user);
    }

    private void saveToDatabase(Users user) {
        System.out.println("Saving user to DB");
    }
}

public class YAGNIExample {

    public static void main(String[] args) {
        Users user = new Users();
        user.name = "Tushar";
        user.email = "tushar@email.com";

        new UsersService().saveUser(user);
    }
}
