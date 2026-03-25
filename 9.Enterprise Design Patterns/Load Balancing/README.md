# 📌 Load Balancing Pattern

## 1️⃣ Definition (Interview Ready)
**Load Balancing** is the process of distributing network traffic across multiple servers. This ensures that no single server bears too much demand. By spreading the load, load balancing improves application responsiveness and increases availability of applications and services.

- **Purpose**: To handle high traffic volumes and ensure high availability (Reliability).
- **Core Mechanism**: A Load Balancer sits between the client and the server pool, accepting incoming traffic and routing it to a healthy server instance based on a specific algorithm.

---

## 2️⃣ Real-World Analogy
Think of a **Checkout Line at a Supermarket**.
- If there is only one cashier (Server), a long line forms, and customers (Clients) wait a long time.
- If the store opens 5 checkout counters, a **Store Manager (Load Balancer)** directs arriving customers to different lines.
- If one cashier goes on a break (Server Down), the Manager directs people to the other 4 active counters.

This keeps the flow of customers smooth and prevents any one cashier from being overwhelmed.

---

## 3️⃣ When to Use (Practical Scenarios)
- **High Traffic Websites**: E-commerce during a sale, Social Media platforms.
- **Microservices Internal Calls**: When Service A calls Service B, it uses a load balancer to choose between multiple instances of Service B.
- **Redundancy/High Availability**: Ensuring that if one server fails, the traffic is redirected to another without the user noticing.
- **Scaling Out**: When you want to add more servers to your pool horizontally to handle more users.

---

## 4️⃣ When NOT to Use
- **Small Applications**: If your app only handles 10 users a day, a load balancer is an unnecessary cost and complexity.
- **Stateful Applications (Carefully)**: If the server keeps local session data that isn't shared (Sticky Sessions are needed here, but it's better to use a stateless design).

---

## 5️⃣ Structure Diagram (Textual UML)
```text
      [ Client ]
          |
          v
  [ LOAD BALANCER ]
  /       |       \
[Svc 1] [Svc 2] [Svc 3] (Server Pool)
```

---

## 6️⃣ Complete Real Java Code Example
### Load Balancing Strategy Interface
```java
interface LoadBalancingStrategy {
    String getNextServer(List<String> servers);
}
```

### Round Robin Implementation
```java
class RoundRobinStrategy implements LoadBalancingStrategy {
    private int index = 0;

    @Override
    public synchronized String getNextServer(List<String> servers) {
        String server = servers.get(index);
        index = (index + 1) % servers.size();
        return server;
    }
}
```

### The Load Balancer Context
```java
public class LoadBalancer {
    private List<String> servers = Arrays.asList("192.168.1.1", "192.168.1.2");
    private LoadBalancingStrategy strategy;

    public LoadBalancer(LoadBalancingStrategy strategy) {
        this.strategy = strategy;
    }

    public void routeRequest() {
        String server = strategy.getNextServer(servers);
        System.out.println("Routing request to: " + server);
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In Spring Cloud, **Spring Cloud LoadBalancer** (which replaced Netflix Ribbon) is used.

### Usage with RestTemplate
```java
@Bean
@LoadBalanced // This magic annotation enables client-side load balancing
public RestTemplate restTemplate() {
    return new RestTemplate();
}

// Now you can call services by name, not IP
restTemplate.getForObject("http://PAYMENT-SERVICE/pay", String.class);
```
Spring will automatically look up `PAYMENT-SERVICE` in the **Service Registry** and use its internal load balancer to pick an instance.

---

## 8️⃣ Interview Questions
### Basic
1. **What is the main purpose of a Load Balancer?**
   - **Answer**: To distribute incoming network traffic across multiple servers to ensure high availability, prevent server overload, and improve the overall responsiveness of the application.

2. **Mention three common Load Balancing algorithms.**
   - **Answer**: 
     - **Round Robin**: Requests are distributed sequentially.
     - **Least Connections**: Sends requests to the server with the fewest active connections.
     - **IP Hash**: Uses the client's IP address to consistently route them to the same server.

3. **Difference between Hardware and Software Load Balancers?**
   - **Answer**: 
     - **Hardware**: Dedicated physical devices (like F5 BIG-IP). Extremely powerful but expensive and less flexible.
     - **Software**: Applications running on standard servers (like Nginx, HAProxy, or AWS ELB). More flexible, easier to scale, and cost-effective.

### Intermediate
1. **What are Sticky Sessions (Session Affinity)?**
   - **Answer**: A feature where a Load Balancer "remembers" which server handled a client's first request and routes all subsequent requests from that same client to that same server. This is necessary if the application stores session data locally on the server.

2. **How does a Load Balancer know if a server has crashed?**
   - **Answer**: Through **Health Checks**. The Load Balancer periodically sends a "Ping" or an HTTP request to a specific endpoint (e.g., `/health`). If the server doesn't respond or returns an error, the LB stops sending traffic to it.

3. **Distinguish between L4 (Transport Layer) and L7 (Application Layer) Load Balancing.**
   - **Answer**: 
     - **L4**: Operates at the network/transport level (TCP/UDP). It only looks at the IP and Port. It's faster but can't make routing decisions based on content.
     - **L7**: Operates at the application level (HTTP). It can look at headers, cookies, and URL paths (e.g., send `/images` to one server and `/api` to another).

### Advanced (Scenario-based)
1. **You have two servers: one with 32GB RAM and another with 8GB RAM. Which algorithm would you use?**
   - **Answer**: **Weighted Round Robin**. You can assign a higher "weight" to the 32GB server so it receives more requests (e.g., a 4:1 ratio) than the 8GB server.

2. **How would you handle a "Global Server Load Balancing" (GSLB) for a user in India vs. a user in the USA?**
   - **Answer**: Use **DNS-based GSLB** (like AWS Route53). When the user resolves the domain name, the DNS server checks their location and returns the IP of the data center closest to them to reduce latency.

### Trick Question
- **Q**: Does a Load Balancer reduce the latency of a single request?
- **A**: **No.** In fact, it adds a tiny bit of network overhead. However, it significantly improves the **overall system throughput** and prevents "Tail Latency" caused by a single server being overloaded.

---

## 9️⃣ Common Interview Follow-Up Questions
- **SSL Termination**: Decrypting traffic at the Load Balancer to save server CPU.
- **Static vs. Dynamic Algorithms**: Comparison between Round Robin and Least Response Time.
- **Failover**: What happens if the primary Load Balancer itself fails? (Mention Active-Passive setup).

---

## 🔟 Pros and Cons
### Pros
- ✅ **Availability**: No downtime if one server fails.
- ✅ **Scalability**: Add/Remove servers on the fly.
- ✅ **Clean Separation**: Clients only deal with one entry point.

### Cons
- ❌ **Single Point of Failure**: If not configured in High Availability (HA) mode.
- ❌ **Cost**: Extra infrastructure costs (Cloud LB or specialized hardware).
- ❌ **Complexity**: Managing health checks and sticky sessions.

---

## 1️⃣1️⃣ One-Line Revision Summary
Load Balancing is the traffic cop of your architecture, directing requests to multiple servers to ensure high availability and optimal performance.
