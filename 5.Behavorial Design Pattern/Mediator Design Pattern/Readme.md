# Mediator Design Pattern: Smart Home System

## What is Mediator Pattern?

Objects communicate through a central mediator instead of directly with each other.

**Example:** Smart home hub coordinates all devices.

---

## Problem

**Without Mediator:** Motion Sensor → Light, Camera, AC (complex connections)

**With Mediator:** All devices → Smart Hub → Coordinated actions

---

## Implementation

### 1. Mediator Interface

```java
public interface SmartHomeHub {
    void notify(Device device, String event);
    void registerDevice(Device device);
}
```

### 2. Concrete Mediator

```java
public class HomeHub implements SmartHomeHub {
    private Map<String, Device> devices = new HashMap<>();
    
    @Override
    public void registerDevice(Device device) {
        devices.put(device.getName(), device);
    }
    
    @Override
    public void notify(Device device, String event) {
        System.out.println("[Hub] " + device.getName() + ": " + event);
        
        if (event.equals("MOTION_DETECTED")) {
            devices.get("Light").trigger("Turn ON");
            devices.get("Camera").trigger("Start Recording");
        } else if (event.equals("DOOR_OPENED")) {
            devices.get("Light").trigger("Turn ON");
            devices.get("AC").trigger("Turn ON");
        }
    }
}
```

### 3. Device Class

```java
public abstract class Device {
    protected String name;
    protected SmartHomeHub hub;
    
    public Device(String name, SmartHomeHub hub) {
        this.name = name;
        this.hub = hub;
        hub.registerDevice(this);
    }
    
    public abstract void trigger(String action);
    
    public void sendEvent(String event) {
        hub.notify(this, event);
    }
    
    public String getName() {
        return name;
    }
}
```

### 4. Concrete Devices

```java
public class Light extends Device {
    public Light(String name, SmartHomeHub hub) {
        super(name, hub);
    }
    
    @Override
    public void trigger(String action) {
        System.out.println("  [Light] " + action);
    }
}

public class Camera extends Device {
    public Camera(String name, SmartHomeHub hub) {
        super(name, hub);
    }
    
    @Override
    public void trigger(String action) {
        System.out.println("  [Camera] " + action);
    }
}

public class AC extends Device {
    public AC(String name, SmartHomeHub hub) {
        super(name, hub);
    }
    
    @Override
    public void trigger(String action) {
        System.out.println("  [AC] " + action);
    }
}

public class MotionSensor extends Device {
    public MotionSensor(String name, SmartHomeHub hub) {
        super(name, hub);
    }
    
    @Override
    public void trigger(String action) {}
    
    public void detectMotion() {
        System.out.println("[Motion Sensor] Motion detected!");
        sendEvent("MOTION_DETECTED");
    }
}
```

### 5. Demo

```java
public class SmartHomeDemo {
    public static void main(String[] args) {
        
        // Create hub
        SmartHomeHub hub = new HomeHub();
        
        // Create devices
        Light light = new Light("Light", hub);
        Camera camera = new Camera("Camera", hub);
        AC ac = new AC("AC", hub);
        MotionSensor sensor = new MotionSensor("Motion Sensor", hub);
        
        // Trigger event
        sensor.detectMotion();
    }
}
```

---

## Output

```
[Motion Sensor] Motion detected!
[Hub] Motion Sensor: MOTION_DETECTED
  [Light] Turn ON
  [Camera] Start Recording
```

---

## Benefits

- **Loose Coupling:** Devices don't know about each other
- **Centralized Control:** All logic in hub
- **Easy to Extend:** Add new devices easily

---

## Real-World Uses

- Smart homes (Google Home, Alexa)
- Chat applications (Slack, WhatsApp)
- Air traffic control systems
- Game lobbies

---

## Quick Summary

| Aspect | Description |
|--------|-------------|
| **Problem** | Complex device-to-device communication |
| **Solution** | Central hub coordinates all devices |
| **Benefit** | Loose coupling, centralized logic |