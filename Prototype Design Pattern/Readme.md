# Prototype Design Pattern - Short Guide

## What is Prototype Pattern?

Prototype pattern creates new objects by **cloning existing objects** instead of creating from scratch. It's like using a photocopy machine.

**Think of it as:** Making copies of a document instead of rewriting it each time.

---

## Real-World Analogy: Document Templates

**Without Prototype (Expensive):**
```
Need new document:
1. Open blank page
2. Set font: Arial, 12pt
3. Set margins: 1 inch
4. Add company logo
5. Add header/footer
6. Set page layout
Total time: 5 minutes per document
```

**With Prototype (Fast):**
```
Need new document:
1. Copy template document
2. Modify content
Total time: 30 seconds
```

---

## Problem Without Prototype

```java
// Creating game characters is expensive
public class Game {
    public static void main(String[] args) {
        // Create 100 similar enemies
        for (int i = 0; i < 100; i++) {
            Enemy enemy = new Enemy();
            enemy.setHealth(100);
            enemy.setSpeed(50);
            enemy.setWeapon("Sword");
            enemy.loadGraphics();     // Expensive: Load texture from disk
            enemy.loadSounds();       // Expensive: Load audio files
            enemy.initializeAI();     // Expensive: Complex setup
            // Takes 2 seconds per enemy = 200 seconds total!
        }
    }
}
```

**Problem:** Creating objects is slow and repetitive!

---

## Solution With Prototype

```java
// Clone instead of create
Enemy template = createEnemyTemplate(); // Create once

for (int i = 0; i < 100; i++) {
    Enemy enemy = template.clone(); // Fast copy!
    // Takes 0.01 seconds per enemy = 1 second total!
}
```

---

## Implementation

### 1. Prototype Interface

```java
public interface Prototype {
    Prototype clone();
}
```

### 2. Concrete Prototype

```java
public class Enemy implements Prototype {
    private String name;
    private int health;
    private int speed;
    private String weapon;
    private byte[] graphics; // Large data
    
    public Enemy(String name, int health, int speed, String weapon) {
        this.name = name;
        this.health = health;
        this.speed = speed;
        this.weapon = weapon;
        this.graphics = loadGraphics(); // Expensive operation
    }
    
    // Clone method - creates copy
    @Override
    public Enemy clone() {
        try {
            // Shallow copy (shares graphics reference)
            return (Enemy) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Enemy(name, health, speed, weapon);
        }
    }
    
    // Manual deep clone if needed
    public Enemy deepClone() {
        Enemy cloned = this.clone();
        cloned.graphics = this.graphics.clone(); // Copy graphics data
        return cloned;
    }
    
    private byte[] loadGraphics() {
        // Simulate expensive operation
        System.out.println("Loading graphics...");
        return new byte[1000];
    }
    
    // Getters and setters
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    
    @Override
    public String toString() {
        return "Enemy{name='" + name + "', health=" + health + 
               ", speed=" + speed + ", weapon='" + weapon + "'}";
    }
}
```

### 3. Client Code

```java
public class Game {
    public static void main(String[] args) {
        
        // Create prototype (template)
        System.out.println("Creating prototype...");
        Enemy prototype = new Enemy("Goblin", 100, 50, "Sword");
        
        System.out.println("\nCloning enemies...");
        
        // Clone multiple enemies quickly
        Enemy enemy1 = prototype.clone();
        enemy1.setName("Goblin-1");
        
        Enemy enemy2 = prototype.clone();
        enemy2.setName("Goblin-2");
        
        Enemy enemy3 = prototype.clone();
        enemy3.setName("Goblin-3");
        
        System.out.println(enemy1);
        System.out.println(enemy2);
        System.out.println(enemy3);
    }
}
```

**Output:**
```
Creating prototype...
Loading graphics...

Cloning enemies...
Enemy{name='Goblin-1', health=100, speed=50, weapon='Sword'}
Enemy{name='Goblin-2', health=100, speed=50, weapon='Sword'}
Enemy{name='Goblin-3', health=100, speed=50, weapon='Sword'}
```

**Notice:** Graphics loaded only ONCE (for prototype), then reused!

---

## Shallow vs Deep Copy

### Shallow Copy (Default)

```java
public class Document implements Cloneable {
    private String title;
    private List<String> pages; // Reference type
    
    @Override
    public Document clone() throws CloneNotSupportedException {
        return (Document) super.clone(); // Shallow copy
    }
}

// Usage
Document original = new Document();
original.pages.add("Page 1");

Document copy = original.clone();
copy.pages.add("Page 2"); // Modifies original's pages too! ❌
```

**Problem:** Both objects share the same `pages` list!

---

### Deep Copy (Manual)

```java
public class Document implements Cloneable {
    private String title;
    private List<String> pages;
    
    @Override
    public Document clone() throws CloneNotSupportedException {
        Document cloned = (Document) super.clone();
        cloned.pages = new ArrayList<>(this.pages); // Deep copy
        return cloned;
    }
}

// Usage
Document original = new Document();
original.pages.add("Page 1");

Document copy = original.clone();
copy.pages.add("Page 2"); // Only modifies copy ✅
```

**Solution:** Each object has its own `pages` list!

---

## Real-World Example: Game Characters

```java
public class GameCharacter implements Cloneable {
    private String type;
    private int level;
    private int health;
    private int mana;
    private String[] skills;
    
    public GameCharacter(String type) {
        this.type = type;
        this.level = 1;
        
        // Expensive initialization based on type
        if (type.equals("Warrior")) {
            this.health = 200;
            this.mana = 50;
            this.skills = new String[]{"Slash", "Block", "Rage"};
        } else if (type.equals("Mage")) {
            this.health = 100;
            this.mana = 200;
            this.skills = new String[]{"Fireball", "Ice Blast", "Teleport"};
        }
        
        System.out.println("Created " + type + " (expensive operation)");
    }
    
    @Override
    public GameCharacter clone() {
        try {
            GameCharacter cloned = (GameCharacter) super.clone();
            cloned.skills = this.skills.clone(); // Deep copy array
            return cloned;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    @Override
    public String toString() {
        return type + " (Level " + level + ", HP:" + health + ", Mana:" + mana + ")";
    }
}

// Usage
public class GameDemo {
    public static void main(String[] args) {
        // Create prototypes (templates)
        System.out.println("=== Creating Prototypes ===");
        GameCharacter warriorTemplate = new GameCharacter("Warrior");
        GameCharacter mageTemplate = new GameCharacter("Mage");
        
        // Clone characters for different players
        System.out.println("\n=== Cloning Characters ===");
        
        GameCharacter player1 = warriorTemplate.clone();
        player1.setLevel(5);
        
        GameCharacter player2 = warriorTemplate.clone();
        player2.setLevel(10);
        
        GameCharacter player3 = mageTemplate.clone();
        player3.setLevel(7);
        
        System.out.println("\n=== Characters ===");
        System.out.println("Player 1: " + player1);
        System.out.println("Player 2: " + player2);
        System.out.println("Player 3: " + player3);
    }
}
```

**Output:**
```
=== Creating Prototypes ===
Created Warrior (expensive operation)
Created Mage (expensive operation)

=== Cloning Characters ===

=== Characters ===
Player 1: Warrior (Level 5, HP:200, Mana:50)
Player 2: Warrior (Level 10, HP:200, Mana:50)
Player 3: Mage (Level 7, HP:100, Mana:200)
```

**Benefit:** Only 2 expensive creations, rest are fast clones!

---

## When to Use Prototype Pattern

✅ **Use when:**
- Object creation is expensive (loads files, network calls, complex setup)
- Need many similar objects with slight variations
- Want to avoid subclasses for each variation
- System should be independent of how objects are created

**Examples:**
- Game enemies/characters (load graphics once)
- Database connection configuration
- UI widgets with default settings
- Document templates

❌ **Don't use when:**
- Object creation is cheap
- Objects have few variations
- Deep copying is complex or expensive

---

## Advantages

1. **Performance:** Cloning is faster than creating from scratch
2. **Reduced Initialization:** Expensive setup done once
3. **Flexibility:** Easy to create variations
4. **Less Code:** No need for multiple constructors

---

## Disadvantages

1. **Cloning Complexity:** Deep copy can be tricky with complex objects
2. **Circular References:** Hard to clone objects with circular references
3. **Clone Method:** Must implement clone() carefully

---

## Prototype Registry Pattern

Store common prototypes in a registry for easy access:

```java
public class PrototypeRegistry {
    private Map<String, GameCharacter> prototypes = new HashMap<>();
    
    public PrototypeRegistry() {
        // Register common prototypes
        prototypes.put("warrior", new GameCharacter("Warrior"));
        prototypes.put("mage", new GameCharacter("Mage"));
        prototypes.put("archer", new GameCharacter("Archer"));
    }
    
    public GameCharacter getPrototype(String type) {
        return prototypes.get(type).clone();
    }
}

// Usage
PrototypeRegistry registry = new PrototypeRegistry();

GameCharacter char1 = registry.getPrototype("warrior");
GameCharacter char2 = registry.getPrototype("mage");
GameCharacter char3 = registry.getPrototype("warrior"); // Reuse prototype
```

---

## Quick Summary

| Concept | Description |
|---------|-------------|
| **Purpose** | Create objects by cloning existing ones |
| **Analogy** | Photocopy machine / Document template |
| **Key Method** | clone() |
| **Shallow Copy** | Copies primitives, shares references |
| **Deep Copy** | Copies everything including referenced objects |
| **Use Case** | Expensive object creation, many similar objects |
| **Benefit** | Fast object creation, reduced initialization cost |

---

## Memory Trick

**Prototype = Photocopy**
- Create template once (expensive)
- Make copies (cheap)
- Modify copies as needed

**Remember:** Don't rebuild from scratch, just clone and customize!