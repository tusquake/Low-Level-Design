public class AndSpecification<T> extends AbstractSpecification<T> {
    private Specification<T> left;
    private Specification<T> right;

    public AndSpecification(Specification<T> left, Specification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T item) {
        return left.isSatisfiedBy(item) && right.isSatisfiedBy(item);
    }
}
