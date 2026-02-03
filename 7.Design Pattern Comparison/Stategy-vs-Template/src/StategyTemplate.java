interface ValidationStrategy {
    void validate(String document);
}

class InvoiceValidationStrategy implements ValidationStrategy {
    @Override
    public void validate(String document) {
        System.out.println("Validating tax and invoice fields");
    }
}

class ResumeValidationStrategy implements ValidationStrategy {
    @Override
    public void validate(String document) {
        System.out.println("Validating keywords and experience");
    }
}

abstract class DocumentProcessor {

    protected ValidationStrategy validationStrategy;

    protected DocumentProcessor(ValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

    public final void process(String document) {
        read(document);
        validationStrategy.validate(document); // Strategy used here
        save(document);
        notifyUser();
    }

    protected void read(String document) {
        System.out.println("Reading document");
    }

    protected abstract void save(String document);

    protected void notifyUser() {
        System.out.println("Notifying user after processing");
    }
}

class InvoiceProcessor extends DocumentProcessor {

    public InvoiceProcessor(ValidationStrategy strategy) {
        super(strategy);
    }

    @Override
    protected void save(String document) {
        System.out.println("Saving invoice to billing system");
    }
}


class ResumeProcessor extends DocumentProcessor {

    public ResumeProcessor(ValidationStrategy strategy) {
        super(strategy);
    }

    @Override
    protected void save(String document) {
        System.out.println("Saving resume to HR system");
    }
}



public class StategyTemplate {
    public static void main(String[] args) {
        ValidationStrategy invoiceValidation =
                new InvoiceValidationStrategy();

        DocumentProcessor invoiceProcessor =
                new InvoiceProcessor(invoiceValidation);

        invoiceProcessor.process("invoice.pdf");

        ValidationStrategy resumeValidation =
                new ResumeValidationStrategy();

        DocumentProcessor resumeProcessor =
                new ResumeProcessor(resumeValidation);

        resumeProcessor.process("resume.pdf");
    }
}
