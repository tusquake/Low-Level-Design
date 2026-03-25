abstract class BeverageTemplate {

    public final void prepareBeverage() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) {
            addCondiments();
        }
        serve();
    }

    private void boilWater() {
        System.out.println("Boiling water...");
    }

    private void pourInCup() {
        System.out.println("Pouring into cup...");
    }

    private void serve() {
        System.out.println("Your beverage is ready! Enjoy!");
        System.out.println("---");
    }

    protected abstract void brew();
    protected abstract void addCondiments();

    protected boolean customerWantsCondiments() {
        return true;
    }

    protected void cleanup() {
        System.out.println("Cleaning up...");
    }
}