public class CoffeeShop {
    public static void main(String[] args) {
        // Simple coffee order karte hain
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " = ₹" + coffee.getCost());

        // Ab milk add karte hain
        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " = ₹" + coffee.getCost());

        // Sugar bhi add kar dete hain
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " = ₹" + coffee.getCost());

        // Last mein chocolate bhi dal dete hain
        coffee = new ChocolateDecorator(coffee);
        System.out.println(coffee.getDescription() + " = ₹" + coffee.getCost());

        System.out.println("\n" + "=".repeat(50));

        // Ek aur order - direct complex coffee
        Coffee specialCoffee = new ChocolateDecorator(
                new SugarDecorator(
                        new MilkDecorator(
                                new SimpleCoffee()
                        )
                )
        );

        System.out.println("Special Order:");
        System.out.println(specialCoffee.getDescription() + " = ₹" + specialCoffee.getCost());
    }
}