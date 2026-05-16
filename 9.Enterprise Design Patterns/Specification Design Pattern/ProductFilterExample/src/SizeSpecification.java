public class SizeSpecification extends AbstractSpecification<Product> {
    private Size size;

    public SizeSpecification(Size size) {
        this.size = size;
    }

    @Override
    public boolean isSatisfiedBy(Product item) {
        return item.getSize() == size;
    }
}
