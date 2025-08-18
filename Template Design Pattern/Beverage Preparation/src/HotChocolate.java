import java.util.ArrayList;
import java.util.List;

class HotChocolate extends BeverageTemplate {
    private boolean wantsMarshmallows;
    private boolean wantsWhippedCream;

    public HotChocolate(boolean wantsMarshmallows, boolean wantsWhippedCream) {
        this.wantsMarshmallows = wantsMarshmallows;
        this.wantsWhippedCream = wantsWhippedCream;
    }

    @Override
    protected void brew() {
        System.out.println("Mixing cocoa powder with hot water...");
    }

    @Override
    protected void addCondiments() {
        List<String> toppings = new ArrayList<>();
        if (wantsMarshmallows) {
            toppings.add("marshmallows");
        }
        if (wantsWhippedCream) {
            toppings.add("whipped cream");
        }

        if (!toppings.isEmpty()) {
            System.out.println("Adding " + String.join(" and ", toppings));
        }
    }

    @Override
    protected boolean customerWantsCondiments() {
        return wantsMarshmallows || wantsWhippedCream;
    }
}