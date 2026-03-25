# Music Player - Adapter Design Pattern

A demonstration of the **Adapter Design Pattern** implemented in Java, showcasing how to integrate incompatible audio format interfaces into a unified music player system.

## Table of Contents

- [Overview](#overview)
- [Design Pattern](#design-pattern)
- [Problem Statement](#problem-statement)
- [Solution](#solution)
- [Project Structure](#project-structure)
- [Class Diagram](#class-diagram)
- [Implementation Details](#implementation-details)
- [How to Run](#how-to-run)
- [Example Output](#example-output)
- [Key Concepts](#key-concepts)
- [Benefits](#benefits)
- [Contributing](#contributing)
- [License](#license)

## Overview

This project demonstrates the Adapter Design Pattern through a music player application that can play multiple audio formats. The system uses adapters to make incompatible audio format interfaces work seamlessly with a common music player interface.

## Design Pattern

**Pattern Type:** Structural Design Pattern

**Intent:** Convert the interface of a class into another interface that clients expect. The Adapter pattern allows classes to work together that couldn't otherwise because of incompatible interfaces.

## Problem Statement

Modern music players need to support various audio formats (MP3, VLC, MP4, etc.). Each format may have its own specific player implementation with different interfaces. The challenge is to create a unified music player that can handle all these formats without tightly coupling the code to specific implementations.

### Challenges:
- Different audio formats have different playback interfaces
- Client code expects a consistent interface for all audio types
- Adding new format support should not require modifying existing code
- Need to maintain backward compatibility with legacy player implementations

## Solution

The Adapter Pattern provides a solution by:

1. **Target Interface (`MediaPlayer`)**: Defines the interface expected by the client
2. **Adaptee (`AdvancedMediaPlayer`)**: Represents the incompatible interface that needs to be adapted
3. **Adapter (`MediaAdapter`)**: Bridges the gap between the target interface and the adaptee
4. **Client (`AudioPlayer`)**: Uses the target interface to play various audio formats

## Project Structure

```
src/
├── MediaPlayer.java          # Target interface
├── AdvancedMediaPlayer.java  # Adaptee interface
├── VlcPlayer.java            # Concrete adaptee implementation
├── Mp4Player.java            # Concrete adaptee implementation
├── MediaAdapter.java         # Adapter class
├── AudioPlayer.java          # Client/Context class
└── Main.java                 # Demo application
```

## Class Diagram

```
┌─────────────────┐
│  MediaPlayer    │ (Interface)
├─────────────────┤
│ + play(type,    │
│   fileName)     │
└─────────────────┘
        ▲
        │ implements
        │
┌───────┴──────────┐
│   AudioPlayer    │
├──────────────────┤
│ - mediaAdapter   │
├──────────────────┤
│ + play(type,     │
│   fileName)      │
└──────────────────┘
        │
        │ uses
        ▼
┌──────────────────┐
│  MediaAdapter    │
├──────────────────┤
│ - advancedPlayer │
├──────────────────┤
│ + play(type,     │
│   fileName)      │
└──────────────────┘
        │
        │ uses
        ▼
┌─────────────────────┐
│ AdvancedMediaPlayer │ (Interface)
├─────────────────────┤
│ + playVlc(fileName) │
│ + playMp4(fileName) │
└─────────────────────┘
        ▲
        │ implements
        │
    ┌───┴───┐
    │       │
┌───┴───┐ ┌─┴──────┐
│VlcPlayer│ │Mp4Player│
└─────────┘ └────────┘
```

## 🔧 Implementation Details

### 1. Target Interface (MediaPlayer)

Defines the standard interface for playing media files.

```java
public interface MediaPlayer {
    void play(String audioType, String fileName);
}
```

### 2. Adaptee Interface (AdvancedMediaPlayer)

Represents the incompatible interface for advanced media formats.

```java
public interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
}
```

### 3. Concrete Adaptees

Implementations for specific advanced media formats.

```java
public class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: " + fileName);
    }
    
    @Override
    public void playMp4(String fileName) {
        // Do nothing
    }
}

public class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        // Do nothing
    }
    
    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: " + fileName);
    }
}
```

### 4. Adapter Class (MediaAdapter)

Adapts the AdvancedMediaPlayer interface to the MediaPlayer interface.

```java
public class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedMusicPlayer;
    
    public MediaAdapter(String audioType) {
        if(audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer = new VlcPlayer();
        } else if(audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer = new Mp4Player();
        }
    }
    
    @Override
    public void play(String audioType, String fileName) {
        if(audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(fileName);
        } else if(audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(fileName);
        }
    }
}
```

### 5. Client Class (AudioPlayer)

Uses the MediaPlayer interface and delegates to MediaAdapter for unsupported formats.

```java
public class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;
    
    @Override
    public void play(String audioType, String fileName) {
        // Built-in support for mp3 music files
        if(audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file. Name: " + fileName);
        }
        // MediaAdapter provides support for other file formats
        else if(audioType.equalsIgnoreCase("vlc") || 
                audioType.equalsIgnoreCase("mp4")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        }
        else {
            System.out.println("Invalid media. " + 
                audioType + " format not supported");
        }
    }
}
```

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

### Compilation

```bash
# Navigate to the src directory
cd src

# Compile all Java files
javac *.java

# Run the main class
java Main
```

### Using an IDE
1. Open the project in your preferred Java IDE
2. Locate the `Main.java` file
3. Run the main method

## Example Output

```
Playing mp3 file. Name: song.mp3
Playing mp4 file. Name: video.mp4
Playing vlc file. Name: movie.vlc
Invalid media. avi format not supported
```

## Key Concepts

### Adapter Pattern Components

1. **Target (MediaPlayer)**
   - The interface that the client expects to work with
   - Defines the domain-specific interface that the client uses

2. **Adaptee (AdvancedMediaPlayer)**
   - The existing interface that needs adapting
   - Contains functionality that the client wants to use but has an incompatible interface

3. **Adapter (MediaAdapter)**
   - Implements the target interface
   - Maintains a reference to an adaptee object
   - Translates requests from the target interface to the adaptee

4. **Client (AudioPlayer)**
   - Collaborates with objects conforming to the target interface
   - Works seamlessly with both native and adapted implementations

### When to Use Adapter Pattern

- You want to use an existing class with an incompatible interface
- You need to create a reusable class that cooperates with unrelated classes
- You need to use several existing subclasses but it's impractical to adapt their interface by subclassing each one
- You want to integrate third-party libraries without modifying their code

## Benefits

### Advantages

1. **Single Responsibility Principle**: Separates interface conversion logic from business logic
2. **Open/Closed Principle**: New adapters can be introduced without modifying existing code
3. **Flexibility**: Makes it easy to add support for new formats
4. **Reusability**: Existing classes can be reused even with incompatible interfaces
5. **Decoupling**: Client code is decoupled from the concrete classes of adaptees

### Real-World Applications

- Database drivers (JDBC adapters for different databases)
- Legacy system integration
- Third-party library integration
- Cross-platform compatibility layers
- File format converters

## Design Principles Applied

- **Dependency Inversion Principle**: High-level modules (AudioPlayer) don't depend on low-level modules (VlcPlayer, Mp4Player); both depend on abstractions
- **Interface Segregation Principle**: Clients aren't forced to depend on interfaces they don't use
- **Composition over Inheritance**: Uses object composition to achieve flexibility