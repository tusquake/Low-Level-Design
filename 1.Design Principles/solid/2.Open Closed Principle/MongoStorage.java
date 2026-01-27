public class MongoStorage implements Storage {
    @Override
    public void save(Cart cart) {
        System.out.println("Saving to MongoDB");
        // Mongo logic here
    }
}
