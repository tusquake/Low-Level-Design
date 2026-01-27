interface AgeRule {
    boolean validate(int age);
}

class AdultAgeRule implements AgeRule {
    @Override
    public boolean validate(int age) {
        return age >= 18;
    }
}

class AgeValidationContext {
    private AgeRule rule;

    public AgeValidationContext(AgeRule rule) {
        this.rule = rule;
    }

    public boolean execute(int age) {
        return rule.validate(age);
    }
}

public class KISSViolationExample {

    public static void main(String[] args) {
        AgeRule rule = new AdultAgeRule();
        AgeValidationContext context = new AgeValidationContext(rule);

        int age = 20;
        System.out.println("Is Adult: " + context.execute(age));
    }
}
