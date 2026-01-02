# Observer Design Pattern Overview

The Observer pattern is a behavioral design pattern that defines a one-to-many dependency between objects so that when one object (subject) changes state, all its dependents (observers) are automatically notified and updated. It establishes a subscription mechanism to allow multiple objects to listen and react to events.

## Problem Statement

In many systems, multiple objects need to stay synchronized with the state of another object. Traditional approaches create tight coupling between objects, making the system rigid, difficult to maintain, and hard to extend.

### Without Observer Pattern

Consider a weather station that needs to update multiple displays:

```java
class WeatherStation {
    private float temperature;
    private float humidity;
    private float pressure;
    
    private PhoneDisplay phoneDisplay;
    private TVDisplay tvDisplay;
    private WebDisplay webDisplay;
    
    public void setMeasurements(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        
        // Manually update each display
        phoneDisplay.update(temperature, humidity, pressure);
        tvDisplay.update(temperature, humidity, pressure);
        webDisplay.update(temperature, humidity, pressure);
    }
}
```

**Issues:**
- Tight coupling between WeatherStation and all display types
- Cannot add new displays without modifying WeatherStation
- Cannot remove displays dynamically
- WeatherStation must know about all display implementations
- Violates Open/Closed Principle
- Hard to test and maintain

## Solution

The Observer pattern solves this by:

1. **Subject (Observable)**: Maintains a list of observers and provides methods to attach/detach them
2. **Observer**: Defines an updating interface for objects that should be notified of changes
3. **Concrete Subject**: Stores state and notifies observers when state changes
4. **Concrete Observers**: Implement the observer interface and react to updates

## Architecture

```
        Subject (Observable)
              |
    ┌─────────┴─────────┐
    |                   |
Observer1          Observer2          Observer3
    |                   |                   |
  update()            update()            update()
    ↓                   ↓                   ↓
[React to          [React to          [React to
 changes]           changes]           changes]
```

## Implementation

### Core Components

**Observer Interface**
```java
interface Observer {
    void update(float temperature, float humidity, float pressure);
}
```

**Subject Interface**
```java
interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
```

**Concrete Subject (Weather Station)**
```java
class WeatherStation implements Subject {
    private final List<Observer> observers;
    private float temperature;
    private float humidity;
    private float pressure;
    
    public WeatherStation() {
        this.observers = new ArrayList<>();
    }
    
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
        System.out.println("Observer registered: " + observer.getClass().getSimpleName());
    }
    
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
        System.out.println("Observer removed: " + observer.getClass().getSimpleName());
    }
    
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(temperature, humidity, pressure);
        }
    }
    
    // Called when measurements change
    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }
    
    private void measurementsChanged() {
        notifyObservers();
    }
    
    // Getters for pull model (optional)
    public float getTemperature() { return temperature; }
    public float getHumidity() { return humidity; }
    public float getPressure() { return pressure; }
}
```

**Concrete Observers**

```java
class PhoneDisplay implements Observer {
    private float temperature;
    private float humidity;
    
    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }
    
    public void display() {
        System.out.println("Phone Display: Temperature = " + temperature + "°C, " +
                         "Humidity = " + humidity + "%");
    }
}

class TVDisplay implements Observer {
    private float temperature;
    private float pressure;
    
    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.pressure = pressure;
        display();
    }
    
    public void display() {
        System.out.println("TV Display: Temperature = " + temperature + "°C, " +
                         "Pressure = " + pressure + " hPa");
    }
}

class StatisticsDisplay implements Observer {
    private final List<Float> temperatureHistory = new ArrayList<>();
    
    @Override
    public void update(float temperature, float humidity, float pressure) {
        temperatureHistory.add(temperature);
        display();
    }
    
    public void display() {
        float avg = (float) temperatureHistory.stream()
                                              .mapToDouble(Float::doubleValue)
                                              .average()
                                              .orElse(0.0);
        System.out.println("Statistics Display: Avg Temperature = " + 
                         String.format("%.2f", avg) + "°C " +
                         "(based on " + temperatureHistory.size() + " readings)");
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        // Create subject
        WeatherStation weatherStation = new WeatherStation();
        
        // Create observers
        PhoneDisplay phoneDisplay = new PhoneDisplay();
        TVDisplay tvDisplay = new TVDisplay();
        StatisticsDisplay statsDisplay = new StatisticsDisplay();
        
        // Register observers
        weatherStation.registerObserver(phoneDisplay);
        weatherStation.registerObserver(tvDisplay);
        weatherStation.registerObserver(statsDisplay);
        
        System.out.println("\n=== First Measurement ===");
        weatherStation.setMeasurements(25.5f, 65.0f, 1013.0f);
        
        System.out.println("\n=== Second Measurement ===");
        weatherStation.setMeasurements(27.0f, 70.0f, 1012.5f);
        
        // Remove one observer
        System.out.println("\n=== Removing TV Display ===");
        weatherStation.removeObserver(tvDisplay);
        
        System.out.println("\n=== Third Measurement ===");
        weatherStation.setMeasurements(26.0f, 68.0f, 1014.0f);
    }
}
```

## Push vs Pull Model

### Push Model (Used Above)
Subject pushes data to observers through update parameters.

```java
void update(float temperature, float humidity, float pressure);
```

**Pros:** Observers get all data immediately  
**Cons:** Less flexible, observers get data they might not need

### Pull Model
Observers pull data from subject when needed.

```java
interface Observer {
    void update(Subject subject);
}

class PhoneDisplay implements Observer {
    @Override
    public void update(Subject subject) {
        WeatherStation station = (WeatherStation) subject;
        this.temperature = station.getTemperature();
        this.humidity = station.getHumidity();
        display();
    }
}
```

**Pros:** More flexible, observers fetch only needed data  
**Cons:** Requires observers to know subject interface

## Benefits

1. **Loose Coupling**: Subject and observers are loosely coupled
2. **Dynamic Relationships**: Add/remove observers at runtime
3. **Open/Closed Principle**: Add new observers without modifying subject
4. **Broadcast Communication**: One-to-many notification automatically
5. **Reusability**: Observers can be reused with different subjects

## Drawbacks

1. **Unexpected Updates**: Observers may be notified in unpredictable order
2. **Memory Leaks**: Forgetting to unregister observers can cause memory leaks
3. **Performance**: Notifying many observers can be expensive
4. **Complexity**: Can make system flow harder to understand
5. **Update Cascades**: Changes can trigger chain reactions

## When to Use

Apply the Observer pattern when:

- One object's state change needs to trigger updates in other objects
- The number of dependent objects is unknown or changes dynamically
- You want to avoid tight coupling between objects
- An abstraction has two aspects, one dependent on the other
- You need to implement event handling systems
- You want to implement Model-View-Controller (MVC) architecture

## Real-World Examples

1. **Event Handling Systems**: GUI frameworks (button clicks, mouse events)
2. **Model-View-Controller**: View components observing model changes
3. **Social Media**: Followers receiving notifications when someone posts
4. **Stock Market**: Multiple displays tracking stock price changes
5. **News Subscriptions**: Subscribers notified of new articles
6. **Chat Applications**: Users receiving messages in real-time
7. **Sensor Networks**: Multiple monitors reacting to sensor data

## Class Diagram

```
┌─────────────────┐
│    Subject      │◁───────────┐
├─────────────────┤             │
│ + register()    │             │
│ + remove()      │             │
│ + notify()      │             │
└────────┬────────┘             │
         △                      │
         │                      │
         │                      │
┌────────┴────────┐             │
│ WeatherStation  │             │
├─────────────────┤             │
│ - temperature   │             │
│ - humidity      │             │
│ - pressure      │             │
│ - observers     │─────────────┘
├─────────────────┤
│ + setMeasure()  │
│ + notify()      │
└─────────────────┘


┌─────────────────┐
│    Observer     │
├─────────────────┤
│ + update()      │
└─────────────────┘
         △
         │
    ┌────┴─────┬─────────────┬──────────────┐
    │          │             │              │
┌───┴────┐ ┌──┴─────┐ ┌─────┴──────┐ ┌────┴────┐
│ Phone  │ │   TV   │ │ Statistics │ │  Web    │
│Display │ │Display │ │  Display   │ │Display  │
└────────┘ └────────┘ └────────────┘ └─────────┘
```

## Output Example

```
Observer registered: PhoneDisplay
Observer registered: TVDisplay
Observer registered: StatisticsDisplay

=== First Measurement ===
Phone Display: Temperature = 25.5°C, Humidity = 65.0%
TV Display: Temperature = 25.5°C, Pressure = 1013.0 hPa
Statistics Display: Avg Temperature = 25.50°C (based on 1 readings)

=== Second Measurement ===
Phone Display: Temperature = 27.0°C, Humidity = 70.0%
TV Display: Temperature = 27.0°C, Pressure = 1012.5 hPa
Statistics Display: Avg Temperature = 26.25°C (based on 2 readings)

=== Removing TV Display ===
Observer removed: TVDisplay

=== Third Measurement ===
Phone Display: Temperature = 26.0°C, Humidity = 68.0%
Statistics Display: Avg Temperature = 26.17°C (based on 3 readings)
```

## Advanced Implementations

### Generic Observer Pattern

```java
interface Observer<T> {
    void update(T data);
}

interface Subject<T> {
    void registerObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);
    void notifyObservers(T data);
}
```

### Thread-Safe Implementation

```java
class ThreadSafeWeatherStation implements Subject {
    private final List<Observer> observers = 
        Collections.synchronizedList(new ArrayList<>());
    
    @Override
    public synchronized void notifyObservers() {
        // Create a copy to avoid ConcurrentModificationException
        List<Observer> observersCopy = new ArrayList<>(observers);
        for (Observer observer : observersCopy) {
            observer.update(temperature, humidity, pressure);
        }
    }
}
```

### Event Object Pattern

```java
class WeatherEvent {
    private final float temperature;
    private final float humidity;
    private final float pressure;
    private final long timestamp;
    
    // Constructor and getters
}

interface Observer {
    void update(WeatherEvent event);
}
```

## Comparison with Similar Patterns

| Pattern | Purpose | Key Difference |
|---------|---------|----------------|
| **Observer** | One-to-many notification | Push-based, automatic notification |
| **Mediator** | Many-to-many communication | Centralized communication hub |
| **Publish-Subscribe** | Decoupled messaging | Message broker, topic-based |
| **Event Bus** | System-wide events | Global event distribution |

## Best Practices

1. **Avoid Memory Leaks**: Always unregister observers when no longer needed
2. **Weak References**: Consider using weak references for observers
3. **Avoid Cycles**: Prevent circular dependencies between subjects and observers
4. **Error Handling**: Catch exceptions in observer updates to prevent cascade failures
5. **Update Filtering**: Allow observers to specify what updates they want
6. **Asynchronous Updates**: Consider async notification for expensive operations

## Conclusion

The Observer pattern is fundamental for building reactive and event-driven systems. It promotes loose coupling by allowing objects to interact without knowing each other's concrete implementations. This pattern is essential for implementing distributed event handling systems, MVC architectures, and any scenario where multiple objects need to stay synchronized with state changes. While it introduces some complexity and potential performance considerations, its benefits in terms of flexibility and maintainability make it one of the most widely used design patterns in software development.