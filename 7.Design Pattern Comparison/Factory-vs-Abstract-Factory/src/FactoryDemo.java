interface Pizza {
    void bake();
}

class VegPizza implements Pizza {
    public void bake() {
        System.out.println("Baking Veg Pizza");
    }
}

class CheesePizza implements Pizza {
    public void bake() {
        System.out.println("Baking Cheese Pizza");
    }
}

class PizzaFactory {
    public static Pizza getPizza(String type) {
        if (type.equalsIgnoreCase("veg"))
            return new VegPizza();
        else if (type.equalsIgnoreCase("cheese"))
            return new CheesePizza();
        return null;
    }
}

public class FactoryDemo {
    public static void main(String[] args) {
        Pizza pizza = PizzaFactory.getPizza("cheese");
        pizza.bake();
    }
}
