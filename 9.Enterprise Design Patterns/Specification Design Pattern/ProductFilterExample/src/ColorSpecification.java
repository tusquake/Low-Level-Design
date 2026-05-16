public class ColorSpecification extends AbstractSpecification<Product> {
    private Color color;

    public ColorSpecification(Color color) {
        this.color = color;
    }

    @Override
    public boolean isSatisfiedBy(Product item) {
        return item.getColor() == color;
    }
}
