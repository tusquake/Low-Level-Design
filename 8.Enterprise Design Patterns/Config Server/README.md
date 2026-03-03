# 📌 Externalized Configuration (Config Server) Pattern

## 1️⃣ Definition (Interview Ready)
The **Externalized Configuration Pattern** (often implemented via a **Config Server**) centralizes the management of configuration properties for all microservices in a single place. Instead of bundling properties like DB URLs or API keys inside the service's JAR/WAR file, the service fetches them from a remote server at startup.

- **Purpose**: To manage configuration separately from code, allowing for environment-specific settings (Dev, Stage, Prod) without rebuilding the application.
- **Problem it solves**: Prevents the need for redeploying 50 microservices just to change a common timeout value or a database password.

---

## 2️⃣ Real-World Analogy
Think of a **Centralized Control Tower** at an airport.
- Each **Airplane (Microservice)** doesn't decide its own runway or gate independently based on a manual kept inside the cockpit.
- When an airplane is ready to take off or land, it contacts the **Control Tower (Config Server)**.
- The Tower gives the pilot the current, most up-to-date instructions (Configuration) based on current traffic and weather.

If a runway is closed, the Tower updates the information once, and every airplane gets the new instructions immediately when they "check in".

---

## 3️⃣ When to Use (Practical Scenarios)
- **Microservices Architecture**: When you have many services sharing common settings (e.g., logging levels, security headers).
- **Multiple Environments**: Managing different database credentials for `local`, `uat`, and `prod` environments cleanly.
- **Dynamic Updates**: When you want to change a feature flag or a timeout value without restarting the entire service.
- **Security/Secrets**: Storing sensitive data in a secure vault (like HashiCorp Vault) and serving it via a config server.

---

## 4️⃣ When NOT to Use
- **Monolithic Applications**: Storing a single `application.properties` file is usually sufficient.
- **Static Configurations**: If your settings never change and are the same across all environments.
- **Air-gapped Systems**: If services cannot reach a central server due to strict network isolation.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
[ Git / Vault / Database ] (Storage)
           |
           v
    [ CONFIG SERVER ]
      /      |      \
[ Svc A ] [ Svc B ] [ Svc C ] (Clients)
```

---

## 6️⃣ Complete Real Java Code Example
### Internal Config Server Representation
```java
public class ConfigServer {
    private static Map<String, String> properties = new HashMap<>();

    static {
        // Simulating loading from a Git Repo
        properties.put("database.url", "jdbc:mysql://prod-db:3306/orders");
        properties.put("feature.discount.enabled", "true");
    }

    public static String fetch(String key) {
        return properties.get(key);
    }
}
```

### Client Service
```java
public class OrderService {
    private String dbUrl;
    private boolean isDiscountActive;

    public void init() {
        // Fetch config at startup
        this.dbUrl = ConfigServer.fetch("database.url");
        this.isDiscountActive = Boolean.parseBoolean(ConfigServer.fetch("feature.discount.enabled"));
        
        System.out.println("Started with DB: " + dbUrl);
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In the Spring ecosystem, **Spring Cloud Config** is the industry standard.

### Config Server (@EnableConfigServer)
Typically uses a **Git Repository** as the backend for configuration files (`orderservice-prod.yml`, `paymentservice-dev.properties`).

### Config Client (bootstrap.yml)
Services use a `bootstrap.yml` file to find the Config Server before the main application context starts.
```yaml
spring:
  application:
    name: order-service
  cloud:
    config:
      uri: http://config-server:8888
```
Using **`@RefreshScope`** allows the service to reload specific beans when the configuration changes without a full restart.

---

## 8️⃣ Interview Questions
### Basic
1. What is a Config Server?
2. Why should we separate configuration from code?
3. Mention two common backends for a Config Server. (Answer: Git, HashiCorp Vault, JDBC).

### Intermediate
1. What is the role of `bootstrap.yml` in a Spring Cloud application?
2. Explain the **`@RefreshScope`** annotation.
3. How do you handle secrets (passwords) in a Config Server? (Answer: Encryption/Decryption features or integration with Vault).

### Advanced (Scenario-based)
1. What happens if the Config Server is down when a microservice starts? (Answer: The service usually fails to start, but can be configured to use local "failover" properties).
2. How would you notify 100 microservices that a configuration has changed in Git? (Answer: Using **Spring Cloud Bus** with a message broker like RabbitMQ).

### Trick Question
- **Q**: Is it safe to store database passwords in plain text in a Git-backed Config Server?
- **A**: **Absolutely not.** Passwords should be encrypted via the Config Server's encryption keys or stored in a dedicated Secrets Manager.

---

## 9️⃣ Common Interview Follow-Up Questions
- **Hierarchy of Overrides**: How Spring resolves properties (Local file vs. Config Server vs. Env Variables).
- **Profiles**: Using `prod`, `test`, `dev` profiles for environment-specific settings.

---

## 🔟 Pros and Cons
### Pros
- ✅ **Consistency**: One source of truth for the entire system.
- ✅ **Version Control**: Track history of config changes in Git.
- ✅ **Dynamic**: Update settings without rebuilding or (sometimes) restarting.

### Cons
- ❌ **Complexity**: Adds another moving part to the infrastructure.
- ❌ **Dependency**: Services depend on the Config Server for startup.
- ❌ **Latency**: Slight increase in startup time due to network calls.

---

## 1️⃣1️⃣ One-Line Revision Summary
Config Server centralizes all environment-specific settings in one place, allowing changes to be made without rebuilding or redeploying the microservices.
