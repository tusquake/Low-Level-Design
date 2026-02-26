public class User {

    private final String name;
    private final int age;
    private final String email;
    private final String phone;

    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
        this.phone = builder.phone;
    }

    public static class Builder {

        private String name;
        private int age;
        private String email;
        private String phone;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public User build() {

            if (name == null) {
                throw new IllegalStateException("Name is required");
            }

            if (age <= 0) {
                throw new IllegalStateException("Age must be positive");
            }

            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age +
                ", email='" + email + "', phone='" + phone + "'}";
    }

    public static void main(String[] args) {

        User user = new User.Builder()
                .setName("Tushar")
                .setAge(25)
                .setEmail("tushar@gmail.com")
                .setPhone("9876543210")
                .build();

        System.out.println(user);
    }
}