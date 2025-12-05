# Facade Design Pattern - Home Theater Example

## Table of Contents
- [Overview](#overview)
- [Problem Statement](#problem-statement)
- [Solution](#solution)
- [UML Diagram](#uml-diagram)
- [Implementation](#implementation)
- [Key Components](#key-components)
- [Advantages](#advantages)
- [Disadvantages](#disadvantages)
- [When to Use](#when-to-use)
- [Real-World Applications](#real-world-applications)
- [Example Usage](#example-usage)

## Overview

The **Facade Design Pattern** is a structural design pattern that provides a simplified interface to a complex subsystem. It hides the complexities of the system and provides a unified interface through which the client can access the system.

This pattern is particularly useful when you need to interact with complex libraries, frameworks, or sets of classes, and you want to provide a simpler way to use them.

## Problem Statement

Imagine you have a home theater system with multiple components:
- DVD Player
- Projector
- Sound System
- Lights
- Screen
- Amplifier

To watch a movie, you need to:
1. Turn on the projector
2. Set projector input to DVD
3. Lower the screen
4. Turn on the amplifier
5. Set amplifier to DVD input
6. Set amplifier volume
7. Turn on the DVD player
8. Dim the lights
9. Play the movie

This process is complex and requires the client to know about all subsystems and their interactions. If any component changes, the client code needs to be updated.

## Solution

The Facade pattern introduces a **HomeTheaterFacade** class that provides simple methods like `watchMovie()` and `endMovie()`. This facade handles all the complex interactions internally, providing a clean and easy-to-use interface to the client.

## UML Diagram

```
┌─────────────────────┐
│      Client         │
└──────────┬──────────┘
           │
           │ uses
           ▼
┌─────────────────────┐
│ HomeTheaterFacade   │
│─────────────────────│
│ - dvdPlayer         │
│ - projector         │
│ - soundSystem       │
│ - lights            │
│ - screen            │
│─────────────────────│
│ + watchMovie()      │
│ + endMovie()        │
└──────────┬──────────┘
           │
           │ coordinates
           │
    ┌──────┴──────┬──────────┬──────────┬──────────┐
    ▼             ▼          ▼          ▼          ▼
┌─────────┐  ┌─────────┐ ┌─────────┐ ┌──────┐ ┌────────┐
│DVDPlayer│  │Projector│ │  Sound  │ │Lights│ │ Screen │
│         │  │         │ │ System  │ │      │ │        │
└─────────┘  └─────────┘ └─────────┘ └──────┘ └────────┘
```

## Implementation

### Subsystem Classes

Each component of the home theater system is a separate class with its own methods:

**DVDPlayer.java**
```java
public class DVDPlayer {
    public void on() {
        System.out.println("DVD Player is ON");
    }
    
    public void play(String movie) {
        System.out.println("Playing movie: " + movie);
    }
    
    public void stop() {
        System.out.println("DVD Player stopped");
    }
    
    public void off() {
        System.out.println("DVD Player is OFF");
    }
}
```

**Projector.java**
```java
public class Projector {
    public void on() {
        System.out.println("Projector is ON");
    }
    
    public void setInput(String input) {
        System.out.println("Projector input set to: " + input);
    }
    
    public void off() {
        System.out.println("Projector is OFF");
    }
}
```

**SoundSystem.java**
```java
public class SoundSystem {
    public void on() {
        System.out.println("Sound System is ON");
    }
    
    public void setVolume(int level) {
        System.out.println("Sound System volume set to: " + level);
    }
    
    public void off() {
        System.out.println("Sound System is OFF");
    }
}
```

**Lights.java**
```java
public class Lights {
    public void dim(int level) {
        System.out.println("Lights dimmed to " + level + "%");
    }
    
    public void on() {
        System.out.println("Lights are ON (100%)");
    }
}
```

**Screen.java**
```java
public class Screen {
    public void down() {
        System.out.println("Screen is coming down");
    }
    
    public void up() {
        System.out.println("Screen is going up");
    }
}
```

### Facade Class

**HomeTheaterFacade.java**
```java
public class HomeTheaterFacade {
    private DVDPlayer dvdPlayer;
    private Projector projector;
    private SoundSystem soundSystem;
    private Lights lights;
    private Screen screen;
    
    public HomeTheaterFacade(DVDPlayer dvd, Projector projector, 
                            SoundSystem sound, Lights lights, Screen screen) {
        this.dvdPlayer = dvd;
        this.projector = projector;
        this.soundSystem = sound;
        this.lights = lights;
        this.screen = screen;
    }
    
    public void watchMovie(String movie) {
        System.out.println("\nGet ready to watch a movie...\n");
        lights.dim(10);
        screen.down();
        projector.on();
        projector.setInput("DVD");
        soundSystem.on();
        soundSystem.setVolume(5);
        dvdPlayer.on();
        dvdPlayer.play(movie);
        System.out.println("\nMovie is now playing. Enjoy!\n");
    }
    
    public void endMovie() {
        System.out.println("\nShutting down the home theater...\n");
        dvdPlayer.stop();
        dvdPlayer.off();
        soundSystem.off();
        projector.off();
        screen.up();
        lights.on();
        System.out.println("\nHome theater is shut down.\n");
    }
}
```

### Client Code

**Main.java**
```java
public class Main {
    public static void main(String[] args) {
        // Create subsystem components
        DVDPlayer dvdPlayer = new DVDPlayer();
        Projector projector = new Projector();
        SoundSystem soundSystem = new SoundSystem();
        Lights lights = new Lights();
        Screen screen = new Screen();
        
        // Create facade
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
            dvdPlayer, projector, soundSystem, lights, screen
        );
        
        // Use the simplified interface
        homeTheater.watchMovie("Inception");
        
        // Some time later...
        homeTheater.endMovie();
    }
}
```

## Key Components

1. **Facade (HomeTheaterFacade)**: Provides simplified methods and delegates work to subsystem classes
2. **Subsystem Classes (DVDPlayer, Projector, etc.)**: Implement subsystem functionality and handle work assigned by the Facade
3. **Client (Main)**: Uses the Facade instead of calling subsystem classes directly

## Advantages

✅ **Simplicity**: Provides a simple interface to a complex system  
✅ **Decoupling**: Reduces dependencies between client code and subsystems  
✅ **Easier Maintenance**: Changes to subsystems don't affect client code  
✅ **Better Organization**: Centralizes complex operations in one place  
✅ **Flexibility**: Clients can still access subsystem classes directly if needed  

## Disadvantages

❌ **God Object**: Facade can become too complex if not designed properly  
❌ **Limited Functionality**: May not expose all features of subsystems  
❌ **Additional Layer**: Adds another layer of abstraction  

## When to Use

Use the Facade pattern when:

- You need to provide a simple interface to a complex subsystem
- You want to layer your subsystems and define entry points to each level
- There are many dependencies between clients and implementation classes
- You want to decouple subsystems from clients and other subsystems

## Real-World Applications

- **Computer Startup**: Starting a computer involves complex processes (POST, boot loader, OS initialization), but you just press the power button
- **Online Shopping**: Behind "Place Order" button are payment processing, inventory management, shipping, and notification systems
- **Database Connections**: JDBC provides a facade over complex database operations
- **Frameworks**: Spring Framework's JdbcTemplate is a facade over JDBC API
- **APIs**: REST APIs act as facades over complex backend systems

## Example Usage

### Without Facade Pattern
```java
// Client needs to know all the steps
lights.dim(10);
screen.down();
projector.on();
projector.setInput("DVD");
soundSystem.on();
soundSystem.setVolume(5);
dvdPlayer.on();
dvdPlayer.play("Inception");
```

### With Facade Pattern
```java
// Simple and clean
homeTheater.watchMovie("Inception");
```
