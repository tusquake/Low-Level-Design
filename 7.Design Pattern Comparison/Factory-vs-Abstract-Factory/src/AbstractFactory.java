interface Pizzas {
    void bake();
}

interface Drink {
    void pour();
}

interface Side {
    void prepare();
}

class VegPizzas implements Pizzas {
    public void bake() {
        System.out.println("Baking Veg Pizzas");
    }
}

class Coke implements Drink {
    public void pour() {
        System.out.println("Pouring Coke");
    }
}

class Fries implements Side {
    public void prepare() {
        System.out.println("Preparing Fries");
    }
}

class ChickenPizzas implements Pizzas {
    public void bake() {
        System.out.println("Baking Chicken Pizzas");
    }
}

class Pepsi implements Drink {
    public void pour() {
        System.out.println("Pouring Pepsi");
    }
}

class Wings implements Side {
    public void prepare() {
        System.out.println("Preparing Chicken Wings");
    }
}

interface MealFactory {
    Pizzas createPizzas();
    Drink createDrink();
    Side createSide();
}

class VegMealFactory implements MealFactory {
    public Pizzas createPizzas() {
        return new VegPizzas();
    }
    public Drink createDrink() {
        return new Coke();
    }
    public Side createSide() {
        return new Fries();
    }
}

class NonVegMealFactory implements MealFactory {
    public Pizzas createPizzas() {
        return new ChickenPizzas();
    }
    public Drink createDrink() {
        return new Pepsi();
    }
    public Side createSide() {
        return new Wings();
    }
}

public class AbstractFactory {
    public static void main(String[] args) {

        MealFactory factory = new VegMealFactory();

        Pizzas Pizzas = factory.createPizzas();
        Drink drink = factory.createDrink();
        Side side = factory.createSide();

        Pizzas.bake();
        drink.pour();
        side.prepare();
    }
}



