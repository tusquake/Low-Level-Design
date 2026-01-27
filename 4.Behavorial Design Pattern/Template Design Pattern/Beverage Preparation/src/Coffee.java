import java.util.ArrayList;
import java.util.List;

class Coffee extends BeverageTemplate {
    private boolean wantsMilk;
    private boolean wantsSugar;

    public Coffee(boolean wantsMilk, boolean wantsSugar) {
        this.wantsMilk = wantsMilk;
        this.wantsSugar = wantsSugar;
    }

    @Override
    protected void brew() {
        System.out.println("Dripping coffee through filter...");
    }

    @Override
    protected void addCondiments() {
        List<String> condiments = new ArrayList<>();
        if (wantsSugar) {
            condiments.add("sugar");
        }
        if (wantsMilk) {
            condiments.add("milk");
        }

        if (!condiments.isEmpty()) {
            System.out.println("Adding " + String.join(" and ", condiments));
        }
    }

    @Override
    protected boolean customerWantsCondiments() {
        return wantsMilk || wantsSugar;
    }

    @Override
    protected void cleanup() {
        System.out.println("Cleaning coffee machine...");
    }
}