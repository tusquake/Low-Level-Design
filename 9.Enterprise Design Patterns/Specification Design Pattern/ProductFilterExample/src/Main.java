import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
            new Product("Apple", Color.RED, Size.SMALL),
            new Product("Tree", Color.GREEN, Size.LARGE),
            new Product("House", Color.BLUE, Size.LARGE),
            new Product("Car", Color.BLUE, Size.MEDIUM),
            new Product("T-Shirt", Color.BLUE, Size.SMALL)
        );

        Specification<Product> blueSpec = new ColorSpecification(Color.BLUE);
        Specification<Product> smallSpec = new SizeSpecification(Size.SMALL);
        
        // Chain specifications using AND
        Specification<Product> blueAndSmall = blueSpec.and(smallSpec);

        System.out.println("All Products:");
        products.forEach(System.out::println);

        System.out.println("\nBlue and Small Products:");
        products.stream()
                .filter(blueAndSmall::isSatisfiedBy)
                .forEach(System.out::println);

        // Chain specifications using OR
        Specification<Product> redOrLarge = new ColorSpecification(Color.RED)
                                            .or(new SizeSpecification(Size.LARGE));

        System.out.println("\nRed or Large Products:");
        products.stream()
                .filter(redOrLarge::isSatisfiedBy)
                .forEach(System.out::println);
    }
}
