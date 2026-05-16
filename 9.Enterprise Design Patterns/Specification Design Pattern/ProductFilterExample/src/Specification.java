public interface Specification<T> {
    boolean isSatisfiedBy(T item);
    Specification<T> and(Specification<T> other);
    Specification<T> or(Specification<T> other);
    Specification<T> not();
}
