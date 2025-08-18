import java.util.ArrayList;
import java.util.List;

class Tea extends BeverageTemplate {
    private String teaType;
    private boolean wantsLemon;
    private boolean wantsHoney;

    public Tea(String teaType, boolean wantsLemon, boolean wantsHoney) {
        this.teaType = teaType;
        this.wantsLemon = wantsLemon;
        this.wantsHoney = wantsHoney;
    }

    @Override
    protected void brew() {
        System.out.println("Steeping " + teaType + " tea for 3-5 minutes...");
    }

    @Override
    protected void addCondiments() {
        List<String> condiments = new ArrayList<>();
        if (wantsLemon) {
            condiments.add("lemon slice");
        }
        if (wantsHoney) {
            condiments.add("honey");
        }

        if (!condiments.isEmpty()) {
            System.out.println("Adding " + String.join(" and ", condiments));
        }
    }

    @Override
    protected boolean customerWantsCondiments() {
        return wantsLemon || wantsHoney;
    }
}
