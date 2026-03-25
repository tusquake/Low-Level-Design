import java.util.ArrayList;
import java.util.List;

interface EmailProvider {
    void send(Email email);
}

class GmailProvider implements EmailProvider {
    @Override
    public void send(Email email) {
        System.out.println("[Gmail] Sending: " + email);
    }
}

class SendGridProvider implements EmailProvider {
    @Override
    public void send(Email email) {
        System.out.println("[SendGrid] Sending: " + email);
    }
}

class AWSProvider implements EmailProvider {
    @Override
    public void send(Email email) {
        System.out.println("[AWS SES] Sending: " + email);
    }
}

class EmailManager {
    private static EmailManager instance;
    private EmailProvider provider;

    // Singleton
    private EmailManager() {}

    public static EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
        }
        return instance;
    }

    // Factory Method
    public void setProvider(String providerType) {
        switch (providerType.toUpperCase()) {
            case "GMAIL":
                this.provider = new GmailProvider();
                break;
            case "SENDGRID":
                this.provider = new SendGridProvider();
                break;
            case "AWS":
                this.provider = new AWSProvider();
                break;
            default:
                this.provider = new GmailProvider();
        }
        System.out.println("Provider set to: " + providerType);
    }

    public void sendEmail(Email email) {
        if (provider == null) {
            throw new IllegalStateException("Provider not set!");
        }
        provider.send(email);
    }
}

class Email {
    private String to;
    private String from;
    private String subject;
    private String body;
    private List<String> cc;
    private List<String> bcc;
    private List<String> attachments;

    private Email() {}

    // Builder
    public static class Builder {
        private String to;
        private String from;
        private String subject;
        private String body;
        private List<String> cc = new ArrayList<>();
        private List<String> bcc = new ArrayList<>();
        private List<String> attachments = new ArrayList<>();

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder addCC(String cc) {
            this.cc.add(cc);
            return this;
        }

        public Builder addBCC(String bcc) {
            this.bcc.add(bcc);
            return this;
        }

        public Builder addAttachment(String attachment) {
            this.attachments.add(attachment);
            return this;
        }

        public Email build() {
            Email email = new Email();
            email.to = this.to;
            email.from = this.from;
            email.subject = this.subject;
            email.body = this.body;
            email.cc = this.cc;
            email.bcc = this.bcc;
            email.attachments = this.attachments;
            return email;
        }
    }

    @Override
    public String toString() {
        return "To: " + to + " | Subject: " + subject +
                (cc.isEmpty() ? "" : " | CC: " + cc) +
                (attachments.isEmpty() ? "" : " | Attachments: " + attachments.size());
    }
}

public class EmailDemo {
    public static void main(String[] args) {

        // Singleton - Get instance
        EmailManager manager = EmailManager.getInstance();

        // Factory - Set provider
        manager.setProvider("GMAIL");

        // Builder - Create simple email
        Email email1 = new Email.Builder()
                .to("user@example.com")
                .from("no-reply@myapp.com")
                .subject("Welcome!")
                .body("Thanks for signing up")
                .build();

        manager.sendEmail(email1);


        // Change provider (Factory)
        System.out.println("\n--- Switching Provider ---");
        manager.setProvider("AWS");

        // Builder - Create complex email
        Email email2 = new Email.Builder()
                .to("client@company.com")
                .from("sales@myapp.com")
                .subject("Q4 Report")
                .body("Please find attached report")
                .addCC("manager@company.com")
                .addCC("team@company.com")
                .addBCC("audit@company.com")
                .addAttachment("report.pdf")
                .addAttachment("charts.xlsx")
                .build();

        manager.sendEmail(email2);


        // Verify Singleton
        System.out.println("\n--- Singleton Test ---");
        EmailManager manager2 = EmailManager.getInstance();
        System.out.println("Same instance? " + (manager == manager2));
    }
}