public class TemplateMethodDemo {
    public static void main(String[] args) {
        System.out.println("=== Template Method Pattern Demo: Beverage Preparation ===\n");

        // Prepare different beverages
        System.out.println("Preparing Coffee:");
        BeverageTemplate coffee = new Coffee(true, true);
        coffee.prepareBeverage();

        System.out.println("Preparing Tea:");
        BeverageTemplate tea = new Tea("Green", true, false);
        tea.prepareBeverage();

        System.out.println("Preparing Hot Chocolate:");
        BeverageTemplate hotChocolate = new HotChocolate(true, true);
        hotChocolate.prepareBeverage();

        // Demonstrate hook method customization
        System.out.println("Preparing Plain Coffee (no condiments):");
        BeverageTemplate plainCoffee = new Coffee(false, false);
        plainCoffee.prepareBeverage();
    }
}