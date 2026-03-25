# 📌 Strangler Fig Pattern (Strangler Pattern)

## 1️⃣ Definition (Interview Ready)
The **Strangler Fig Pattern** is a software migration strategy used to incrementally replace a legacy system with a new one. It works by creating a "facade" or "router" that intercepts requests and directs them to either the old system or the new system. Over time, more features are migrated to the new system until the old system is completely "strangled" and can be retired.

- **Purpose**: To migrate large, complex monolithic applications to microservices without a "Big Bang" rewrite.
- **Key Move**: Transforming the monolith branch by branch, service by service.
- **Problem it solves**: High risk of failure, long downtime, and delayed value delivery associated with complete rewrites.

---

## 2️⃣ Real-World Analogy
Think of the **Strangler Fig tree found in rain forests**.
- It grows its roots around a host tree.
- Slowly, it climbs higher and wraps itself completely around the host.
- Eventually, the original host tree dies off because it is no longer needed or covered by the new, stronger fig tree.

The "New System" is the Strangler Fig, and the "Legacy Monolith" is the original tree.

---

## 3️⃣ When to Use (Practical Scenarios)
- **Legacy Migration**: Moving from a 10-year-old COBOL or old Java monolith to a modern Spring Boot microservice.
- **Tech Stack Upgrade**: Moving from JSP/Servlets to a React/Node.js or Spring Boot/Angular setup.
- **Cloud Migration**: Gradually moving on-premise components to AWS/GCP.
- **Risk Mitigation**: When the business cannot afford a 6-month development cycle where no new features are released.

---

## 4️⃣ When NOT to Use
- **Small Applications**: If the system is small enough to be rewritten in a few weeks, don't waste time with a strangler facade.
- **Dead Ends**: If the legacy system is already well-designed and just needs minor updates.
- **High Interdependence**: If the monolith's database is so tightly coupled that extracting one service requires extracting everything (in which case, you might need a different data-focused strategy first).

---

## 5️⃣ Structure Diagram (Textual UML)
```text
        [ External Client ]
               |
               v
      [ STRANGLER FACADE ] (Router/API Gateway)
      /                \
(Route A)           (Route B)
    |                  |
    v                  v
[ NEW SERVICE ]     [ LEGACY MONOLITH ]
(Microservice)      (Old System)
```

---

## 6️⃣ Complete Real Java Code Example
### Simplified Router Logic
```java
public class StranglerRouter {
    private LegacyOrderService legacy = new LegacyOrderService();
    private NewOrderService modern = new NewOrderService();

    public void processRequest(String feature) {
        // Feature Toggling / Routing logic
        if ("order_v2".equals(feature)) {
            // New logic for migrated feature
            modern.execute();
        } else {
            // Fallback to legacy
            legacy.execute();
        }
    }
}
```

### The New Service (Clean)
```java
class NewOrderService {
    public void execute() {
        System.out.println("Processing in Modern Microservice...");
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In real-world projects, the **API Gateway** (like Spring Cloud Gateway or NGINX) acts as the Strangler Facade.

### NGINX Routing Strategy
```nginx
location /api/orders {
    proxy_pass http://modern-microservice;
}

location /api/legacy {
    proxy_pass http://legacy-monolith;
}
```
As you migrate more endpoints, you simply update the NGINX configuration to point to the new microservice instead of the monolith.

---

## 8️⃣ Interview Questions
### Basic
1. **What is the Strangler Pattern?**
   - **Answer**: It's an incremental migration strategy to replace a legacy system with a modern one by slowly wrapping it in new services until the old system is completely "strangled" and can be retired.

2. **Why is it called "Strangler Fig"?**
   - **Answer**: It's named after the Strangler Fig tree, which grows around a host tree, eventually replacing it entirely. In software, the new system is the "fig" and the legacy is the "host."

3. **What is the main benefit of incremental migration vs. a "Big Bang" rewrite?**
   - **Answer**: Lower risk. A "Big Bang" rewrite takes months/years before any value is delivered to the business. Strangler delivers value service-by-service and allows for easy rollbacks if a single service fails.

### Intermediate
1. **What is the role of a "Facade" in the Strangler pattern?**
   - **Answer**: The Facade (usually an API Gateway or Reverse Proxy) intercepts all incoming traffic and routes it to either the legacy system or the new microservice based on the URL path or headers.

2. **How do you handle data consistency between the old and new systems during migration?**
   - **Answer**: 
     - **Database Synchronizer**: A background job that mirrors data between the old and new DBs.
     - **Dual Writes**: The application writes to both databases simultaneously (temporary logic).
     - **Event-Driven**: The new service publishes an event that the legacy system consumes to update its own DB.

3. **When is the "Monolith" finally shut down?**
   - **Answer**: Only when 100% of its business functionality has been successfully migrated to the new system, verified in production, and all traffic is permanently routed to the new services.

### Advanced (Scenario-based)
1. **You have migrated the "Order" feature, but it still needs to call the "User" logic which is still in the monolith. How do you handle this?**
   - **Answer**: Use an **Anti-Corruption Layer (ACL)**. The new "Order" service calls the ACL, which handles the "ugly" logic of talking to the legacy monolith's API or Database, keeping the new system clean.

2. **How do you handle authentication during the transition period?**
   - **Answer**: Centralize authentication at the **API Gateway** level using JWT. Both the legacy monolith and the new microservices should be updated to accept the same JWT token for identity.

### Trick Question
- **Q**: Is the Strangler Pattern only for Microservices?
- **A**: **No.** It can be used anytime you replace one component with another incrementally. For example, replacing an old React class component with Hooks, or moving from an old XML-based configuration to Java-based config in Spring.

---

## 9️⃣ Common Interview Follow-Up Questions
- **Rollback Strategy**: How easily can we revert to the legacy system if the new one fails?
- **Feature Flags**: Using tools like LaunchDarkly to switch traffic at the application level.
- **Database Refactoring**: The "Branch by Abstraction" technique.

---

## 🔟 Pros and Cons
### Pros
- ✅ **Continuous Delivery**: New features can be released while old ones are being migrated.
- ✅ **Low Risk**: If a new service fails, you can quickly route back to the legacy system.
- ✅ **Early ROI**: The business gets value from the new tech stack much earlier.

### Cons
- ❌ **Maintenance**: You have to maintain two systems (and the router) for a period.
- ❌ **Latency**: The router/facade adds a small network hop.
- ❌ **Infrastructure Overhead**: You need twice the monitoring and logging.

---

## 1️⃣1️⃣ One-Line Revision Summary
Strangler pattern is an incremental migration technique that replaces legacy features one by one via a routing facade until the entire system is modernized.
