# Factory + Singleton + Builder: Email Service Provider

## Real-World Example: SendGrid/AWS SES Integration

**Scenario:** Your app needs to send emails through different providers (Gmail, SendGrid, AWS SES) with complex configurations, but you want only one email manager instance.

---

## Why This Combination?

- **Singleton**: Only one EmailManager instance (avoid multiple connections)
- **Factory**: Choose email provider at runtime (Gmail/SendGrid/AWS)
- **Builder**: Configure complex email settings (attachments, CC, BCC, headers)

---

## Implementation

### 1. Email Provider Interface

```java
public interface EmailProvider {
    void send(Email email);
}
```

### 2. Concrete Providers

```java
public class GmailProvider implements EmailProvider {
    @Override
    public void send(Email email) {
        System.out.println("[Gmail] Sending: " + email);
    }
}

public class SendGridProvider implements EmailProvider {
    @Override
    public void send(Email email) {
        System.out.println("[SendGrid] Sending: " + email);
    }
}

public class AWSProvider implements EmailProvider {
    @Override
    public void send(Email email) {
        System.out.println("[AWS SES] Sending: " + email);
    }
}
```

### 3. Singleton EmailManager + Factory

```java
public class EmailManager {
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
```

### 4. Builder for Complex Email

```java
public class Email {
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
```

### 5. Demo

```java
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
        manager.setProvider("SENDGRID");
        
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
```

---

## Output

```
Provider set to: GMAIL
[Gmail] Sending: To: user@example.com | Subject: Welcome!

--- Switching Provider ---
Provider set to: SENDGRID
[SendGrid] Sending: To: client@company.com | Subject: Q4 Report | CC: [manager@company.com, team@company.com] | Attachments: 2

--- Singleton Test ---
Same instance? true
```

---

## How Patterns Work Together

### Singleton:
- Only ONE EmailManager instance in entire app
- Prevents multiple SMTP connections
- Centralized configuration

### Factory:
- Choose provider at runtime (Gmail/SendGrid/AWS)
- Switch providers without changing code
- Environment-based selection (dev uses Gmail, prod uses SendGrid)

### Builder:
- Construct complex emails step-by-step
- Optional parameters (CC, BCC, attachments)
- Readable and maintainable

---

## Real-World Flow

```
App starts
    ↓
Get EmailManager instance (Singleton)
    ↓
Set provider based on config (Factory)
    ↓
User triggers "Send Email"
    ↓
Build email with builder (Builder)
    ↓
Send through configured provider
```

---

## Benefits

**Singleton:**
- One connection pool
- Consistent configuration
- Memory efficient

**Factory:**
- Switch providers easily
- Environment-based routing
- No code changes

**Builder:**
- Clean email construction
- Optional fields
- Type safety

---

## Other Similar Examples

**Logger Service:**
- Singleton: One logger instance
- Factory: Console/File/Database logger
- Builder: Configure log format, level, filters

**Cache Manager:**
- Singleton: One cache instance
- Factory: Redis/Memcached/In-memory
- Builder: Configure TTL, eviction policy, size

**HTTP Client:**
- Singleton: One client instance
- Factory: RestTemplate/OkHttp/Apache
- Builder: Configure timeout, headers, interceptors

---

## Quick Summary

| Pattern | Purpose | Benefit |
|---------|---------|---------|
| Singleton | One instance | Resource management |
| Factory | Runtime selection | Flexibility |
| Builder | Complex construction | Readability |
| **Together** | Unified service with flexible providers and clean config | Production-ready system |