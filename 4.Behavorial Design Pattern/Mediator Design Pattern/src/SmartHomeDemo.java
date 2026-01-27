import java.util.HashMap;
import java.util.Map;

interface SmartHomeHub {
    void notify(Device device, String event);
    void registerDevice(Device device);
}

class HomeHub implements SmartHomeHub {
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

abstract class Device {
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

class Light extends Device {
    public Light(String name, SmartHomeHub hub) {
        super(name, hub);
    }

    @Override
    public void trigger(String action) {
        System.out.println("  [Light] " + action);
    }
}

class Camera extends Device {
    public Camera(String name, SmartHomeHub hub) {
        super(name, hub);
    }

    @Override
    public void trigger(String action) {
        System.out.println("  [Camera] " + action);
    }
}

class AC extends Device {
    public AC(String name, SmartHomeHub hub) {
        super(name, hub);
    }

    @Override
    public void trigger(String action) {
        System.out.println("  [AC] " + action);
    }
}

class MotionSensor extends Device {
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

public class SmartHomeDemo {
    public static void main(String[] args) {

        SmartHomeHub hub = new HomeHub();

        Light light = new Light("Light", hub);
        Camera camera = new Camera("Camera", hub);
        AC ac = new AC("AC", hub);
        MotionSensor sensor = new MotionSensor("Motion Sensor", hub);

        sensor.detectMotion();
    }
}