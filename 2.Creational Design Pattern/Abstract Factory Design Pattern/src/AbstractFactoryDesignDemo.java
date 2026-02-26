interface Pizza {
    void prepare();
}

interface Fries {
    void serve();
}

interface Drink {
    void pour();
}

interface Oregano {
    void add();
}

class VegPizza implements Pizza {
    public void prepare() {
        System.out.println("Preparing Veg Pizza");
    }
}

class VegFries implements Fries {
    public void serve() {
        System.out.println("Serving Veg Fries");
    }
}

class Coke implements Drink {
    public void pour() {
        System.out.println("Pouring Coke");
    }
}

class RegularOregano implements Oregano {
    public void add() {
        System.out.println("Adding Oregano");
    }
}

class NonVegPizza implements Pizza {
    public void prepare() {
        System.out.println("Preparing Chicken Pizza");
    }
}

class NonVegFries implements Fries {
    public void serve() {
        System.out.println("Serving Loaded Chicken Fries");
    }
}

class Pepsi implements Drink {
    public void pour() {
        System.out.println("Pouring Pepsi");
    }
}

class SpicyOregano implements Oregano {
    public void add() {
        System.out.println("Adding Spicy Oregano");
    }
}

interface MealFactory {
    Pizza createPizza();
    Fries createFries();
    Drink createDrink();
    Oregano createOregano();
}

class VegMealFactory implements MealFactory {

    public Pizza createPizza() {
        return new VegPizza();
    }

    public Fries createFries() {
        return new VegFries();
    }

    public Drink createDrink() {
        return new Coke();
    }

    public Oregano createOregano() {
        return new RegularOregano();
    }
}

class NonVegMealFactory implements MealFactory {

    public Pizza createPizza() {
        return new NonVegPizza();
    }

    public Fries createFries() {
        return new NonVegFries();
    }

    public Drink createDrink() {
        return new Pepsi();
    }

    public Oregano createOregano() {
        return new SpicyOregano();
    }
}

public class AbstractFactoryDesignDemo {

    public static void main(String[] args) {

        //Veg
        MealFactory factory = new VegMealFactory();

        Pizza pizza = factory.createPizza();
        Fries fries = factory.createFries();
        Drink drink = factory.createDrink();
        Oregano oregano = factory.createOregano();

        pizza.prepare();
        fries.serve();
        drink.pour();
        oregano.add();

        //Non-Veg
        factory = new NonVegMealFactory();

        pizza = factory.createPizza();
        fries = factory.createFries();
        drink = factory.createDrink();
        oregano = factory.createOregano();

        pizza.prepare();
        fries.serve();
        drink.pour();
        oregano.add();
    }
}