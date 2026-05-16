public class NotSpecification<T> extends AbstractSpecification<T> {
    private Specification<T> spec;

    public NotSpecification(Specification<T> spec) {
        this.spec = spec;
    }

    @Override
    public boolean isSatisfiedBy(T item) {
        return !spec.isSatisfiedBy(item);
    }
}
