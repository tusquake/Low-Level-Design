# Flyweight Design Pattern Overview

The Flyweight pattern is a structural design pattern that minimizes memory usage by sharing common data among multiple objects. It achieves this by separating intrinsic state (shared) from extrinsic state (unique) and storing the intrinsic state in flyweight objects that can be reused.

## Problem Statement

When an application needs to create a large number of similar objects, memory consumption can become prohibitively high. Creating thousands or millions of objects with mostly identical data leads to significant memory waste and performance degradation.

### Without Flyweight Pattern

Consider a text editor displaying characters:

```java
class Character {
    private char value;
    private String fontFamily;
    private int fontSize;
    private String color;
    private int positionX;
    private int positionY;
}

// Creating 1 million characters
for (int i = 0; i < 1_000_000; i++) {
    Character ch = new Character('A', "Arial", 12, "Black", x, y);
    // Each object stores ALL properties, even if many are identical
}
```

**Issues:**
- Massive memory consumption (1M objects × full state)
- Most characters share the same font, size, and color
- Only position varies per character
- Performance degradation due to memory pressure
- Redundant data storage

**Example:** In a document with 100,000 characters using only 26 letters, we create 100,000 objects when we only need 26 unique character representations.

## Solution

The Flyweight pattern separates object state into two categories:

1. **Intrinsic State**: Shared data that is independent of context (e.g., character value, font, size)
2. **Extrinsic State**: Unique data that varies with context (e.g., position, row, column)

The intrinsic state is stored in reusable flyweight objects, while the extrinsic state is passed to flyweight methods when needed.

## Architecture

```
Client → FlyweightFactory → [Flyweight Pool]
                                    ↓
                            Shared Flyweight Objects
                            (Intrinsic State)
         
Client maintains → Extrinsic State (position, context)
```

## Implementation

### Core Components

**Flyweight Interface**
```java
interface CharacterFlyweight {
    void render(int positionX, int positionY);
}
```

**Concrete Flyweight (Intrinsic State)**
```java
class ConcreteCharacter implements CharacterFlyweight {
    // Intrinsic state - shared among all instances
    private final char value;
    private final String fontFamily;
    private final int fontSize;
    private final String color;
    
    public ConcreteCharacter(char value, String fontFamily, 
                             int fontSize, String color) {
        this.value = value;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.color = color;
        System.out.println("Creating flyweight for: " + value);
    }
    
    @Override
    public void render(int positionX, int positionY) {
        // Extrinsic state is passed as parameters
        System.out.println("Rendering '" + value + "' at (" + 
                         positionX + "," + positionY + ") " +
                         "Font: " + fontFamily + " " + fontSize + "pt " +
                         "Color: " + color);
    }
}
```

**Flyweight Factory**
```java
class CharacterFactory {
    // Pool to store and reuse flyweight objects
    private final Map<String, CharacterFlyweight> flyweights = new HashMap<>();
    
    public CharacterFlyweight getCharacter(char value, String fontFamily, 
                                          int fontSize, String color) {
        // Create unique key for intrinsic state
        String key = value + "-" + fontFamily + "-" + fontSize + "-" + color;
        
        // Return existing flyweight or create new one
        if (!flyweights.containsKey(key)) {
            flyweights.put(key, new ConcreteCharacter(value, fontFamily, 
                                                      fontSize, color));
        }
        return flyweights.get(key);
    }
    
    public int getFlyweightCount() {
        return flyweights.size();
    }
}
```

**Client Code**
```java
class TextEditor {
    private final List<CharacterFlyweight> characters = new ArrayList<>();
    private final List<Integer> positionsX = new ArrayList<>();
    private final List<Integer> positionsY = new ArrayList<>();
    private final CharacterFactory factory = new CharacterFactory();
    
    public void addCharacter(char value, String font, int size, 
                           String color, int x, int y) {
        // Get shared flyweight
        CharacterFlyweight character = factory.getCharacter(value, font, size, color);
        
        // Store flyweight reference and extrinsic state separately
        characters.add(character);
        positionsX.add(x);
        positionsY.add(y);
    }
    
    public void render() {
        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).render(positionsX.get(i), positionsY.get(i));
        }
    }
    
    public void printStats() {
        System.out.println("Total characters: " + characters.size());
        System.out.println("Unique flyweights: " + factory.getFlyweightCount());
        System.out.println("Memory saved: " + 
                         (characters.size() - factory.getFlyweightCount()) + 
                         " object creations avoided");
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        
        // Adding characters - many with same formatting
        editor.addCharacter('H', "Arial", 12, "Black", 0, 0);
        editor.addCharacter('e', "Arial", 12, "Black", 10, 0);
        editor.addCharacter('l', "Arial", 12, "Black", 20, 0);
        editor.addCharacter('l', "Arial", 12, "Black", 30, 0);
        editor.addCharacter('o', "Arial", 12, "Black", 40, 0);
        editor.addCharacter(' ', "Arial", 12, "Black", 50, 0);
        editor.addCharacter('W', "Arial", 14, "Red", 60, 0);
        editor.addCharacter('o', "Arial", 14, "Red", 70, 0);
        editor.addCharacter('r', "Arial", 14, "Red", 80, 0);
        editor.addCharacter('l', "Arial", 14, "Red", 90, 0);
        editor.addCharacter('d', "Arial", 14, "Red", 100, 0);
        
        System.out.println("=== Rendering Document ===");
        editor.render();
        
        System.out.println("\n=== Statistics ===");
        editor.printStats();
    }
}
```

## Benefits

1. **Memory Optimization**: Dramatically reduces memory usage when dealing with many similar objects
2. **Performance**: Fewer object creations lead to better performance
3. **Scalability**: Can handle millions of objects efficiently
4. **Separation of Concerns**: Clear distinction between shared and unique state
5. **Centralized Management**: Factory pattern controls object creation and reuse

## Drawbacks

1. **Complexity**: Increases code complexity by separating intrinsic and extrinsic state
2. **Runtime Cost**: Factory lookups add slight overhead
3. **Immutability**: Flyweights should be immutable to ensure safe sharing
4. **Context Management**: Client must manage and pass extrinsic state

## When to Use

Apply the Flyweight pattern when:

- Application uses a large number of objects
- Most object state can be made extrinsic
- Many objects can be replaced by relatively few shared objects
- Application doesn't depend on object identity
- Memory usage is a critical concern

## Real-World Examples

1. **Text Editors**: Character rendering (as demonstrated)
2. **Game Development**: Rendering trees, bullets, particles with shared sprites
3. **String Pool**: Java's String interning mechanism
4. **Database Connections**: Connection pooling
5. **UI Frameworks**: Reusing widget configurations
6. **Graphics Systems**: Sharing textures, fonts, and graphical assets

## Class Diagram

```
┌─────────────────────┐
│ CharacterFactory    │
├─────────────────────┤
│ - flyweights: Map   │
├─────────────────────┤
│ + getCharacter()    │
│ + getFlyweightCount()│
└──────────┬──────────┘
           │ creates/manages
           ▼
┌─────────────────────┐
│ CharacterFlyweight  │ (Interface)
├─────────────────────┤
│ + render(x, y)      │
└─────────────────────┘
           △
           │ implements
           │
┌──────────┴──────────┐
│ ConcreteCharacter   │
├─────────────────────┤
│ - value: char       │ ◄── Intrinsic State
│ - fontFamily: String│
│ - fontSize: int     │
│ - color: String     │
├─────────────────────┤
│ + render(x, y)      │ ◄── Extrinsic State (params)
└─────────────────────┘
```

## Output Example

```
=== Creating Flyweights ===
Creating flyweight for: H
Creating flyweight for: e
Creating flyweight for: l
Creating flyweight for: o
Creating flyweight for:  
Creating flyweight for: W
Creating flyweight for: r
Creating flyweight for: d

=== Rendering Document ===
Rendering 'H' at (0,0) Font: Arial 12pt Color: Black
Rendering 'e' at (10,0) Font: Arial 12pt Color: Black
Rendering 'l' at (20,0) Font: Arial 12pt Color: Black
Rendering 'l' at (30,0) Font: Arial 12pt Color: Black
Rendering 'o' at (40,0) Font: Arial 12pt Color: Black
Rendering ' ' at (50,0) Font: Arial 12pt Color: Black
Rendering 'W' at (60,0) Font: Arial 14pt Color: Red
Rendering 'o' at (70,0) Font: Arial 14pt Color: Red
Rendering 'r' at (80,0) Font: Arial 14pt Color: Red
Rendering 'l' at (90,0) Font: Arial 14pt Color: Red
Rendering 'd' at (100,0) Font: Arial 14pt Color: Red

=== Statistics ===
Total characters: 11
Unique flyweights: 8
Memory saved: 3 object creations avoided

Note: 'l' appears 3 times but only 1 flyweight created
      'o' appears 2 times but only 1 flyweight created
```

## Memory Comparison

**Without Flyweight:**
```
100,000 characters × 100 bytes per object = 10 MB
```

**With Flyweight:**
```
26 unique characters × 100 bytes = 2.6 KB (intrinsic)
100,000 positions × 8 bytes = 800 KB (extrinsic)
Total ≈ 800 KB (92% memory reduction!)
```

## Key Design Considerations

### Intrinsic vs Extrinsic State

**Intrinsic (Sharable):**
- Independent of context
- Stored in flyweight
- Examples: character value, font type, color scheme

**Extrinsic (Context-specific):**
- Varies with usage
- Passed to flyweight methods
- Examples: position, coordinates, user data

### Thread Safety

If flyweights are shared across threads, ensure they are immutable or properly synchronized:

```java
public final class ThreadSafeCharacter implements CharacterFlyweight {
    private final char value; // final = immutable
    private final String font;
    
    // No setters = immutable
    @Override
    public void render(int x, int y) {
        // Thread-safe since no state modification
    }
}
```

## Comparison with Similar Patterns

| Pattern | Purpose | Key Difference |
|---------|---------|----------------|
| **Flyweight** | Memory optimization through sharing | Focuses on reducing memory by sharing intrinsic state |
| **Singleton** | Single instance | Only one instance total, not pooled instances |
| **Object Pool** | Reuse expensive objects | Reuses entire objects temporarily, not permanent sharing |
| **Prototype** | Clone objects | Creates new instances, doesn't share state |

## Conclusion

The Flyweight pattern is a powerful optimization technique for applications dealing with large numbers of similar objects. By intelligently separating shared intrinsic state from unique extrinsic state, it can achieve dramatic memory reductions and performance improvements. While it adds complexity, the benefits are substantial when memory efficiency is critical, making it an essential pattern for high-performance applications.