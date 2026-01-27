# Memento Design Pattern Overview

The Memento pattern is a behavioral design pattern that allows capturing and externalizing an object's internal state without violating encapsulation, so that the object can be restored to this state later. It provides the ability to implement undo/redo functionality and state rollback mechanisms.

## Problem Statement

Many applications need to save and restore an object's state (undo/redo, checkpoints, snapshots), but directly exposing an object's internal state breaks encapsulation. Traditional approaches either violate encapsulation by making internal state public or create complex state management that tightly couples objects.

### Without Memento Pattern

Consider a text editor that needs undo functionality:

```java
class TextEditor {
    private String content;
    private int cursorPosition;
    private String fontFamily;
    
    // Expose internal state for saving
    public String getContent() { return content; }
    public int getCursorPosition() { return cursorPosition; }
    public String getFontFamily() { return fontFamily; }
    
    // Allow external modification
    public void setContent(String content) { this.content = content; }
    public void setCursorPosition(int pos) { this.cursorPosition = pos; }
    public void setFontFamily(String font) { this.fontFamily = font; }
}

// Client code managing state
class UndoManager {
    private Stack<EditorState> history = new Stack<>();
    
    public void save(TextEditor editor) {
        // Accessing internal state directly
        EditorState state = new EditorState(
            editor.getContent(),
            editor.getCursorPosition(),
            editor.getFontFamily()
        );
        history.push(state);
    }
}
```

**Issues:**
- Breaks encapsulation by exposing internal state
- Client code depends on editor's internal structure
- Adding new fields requires updating all client code
- No control over what state can be saved/restored
- Tight coupling between editor and state management

## Solution

The Memento pattern solves this by introducing three key participants:

1. **Originator**: The object whose state needs to be saved
2. **Memento**: Stores the internal state of the Originator
3. **Caretaker**: Manages mementos but never examines or modifies their contents

The Originator creates mementos containing snapshots of its state and can restore its state from mementos. The Memento is opaque to all objects except the Originator.

## Architecture

```
┌─────────────┐         creates        ┌──────────┐
│ Originator  │────────────────────────▷│ Memento  │
│             │                         │          │
│ - state     │      restores from      │ - state  │
│ + save()    │◁────────────────────────│          │
│ + restore() │                         └──────────┘
└─────────────┘                              △
                                             │
                                          stores
                                             │
                                      ┌──────┴──────┐
                                      │ Caretaker   │
                                      │             │
                                      │ - mementos  │
                                      └─────────────┘
```

## Implementation

### Core Components

**Memento Class**
```java
// Memento stores the state - usually as an inner class or with package-private access
class EditorMemento {
    private final String content;
    private final int cursorPosition;
    private final String fontFamily;
    private final long timestamp;
    
    // Package-private or private constructor - only Originator can create
    EditorMemento(String content, int cursorPosition, String fontFamily) {
        this.content = content;
        this.cursorPosition = cursorPosition;
        this.fontFamily = fontFamily;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters accessible only to Originator
    String getContent() { return content; }
    int getCursorPosition() { return cursorPosition; }
    String getFontFamily() { return fontFamily; }
    long getTimestamp() { return timestamp; }
}
```

**Originator (Text Editor)**
```java
class TextEditor {
    private String content;
    private int cursorPosition;
    private String fontFamily;
    
    public TextEditor() {
        this.content = "";
        this.cursorPosition = 0;
        this.fontFamily = "Arial";
    }
    
    // Public methods to modify state
    public void write(String text) {
        content += text;
        cursorPosition += text.length();
        System.out.println("Written: " + text);
    }
    
    public void setCursor(int position) {
        this.cursorPosition = Math.max(0, Math.min(position, content.length()));
    }
    
    public void setFont(String font) {
        this.fontFamily = font;
    }
    
    // Create memento - saves current state
    public EditorMemento save() {
        System.out.println("Saving state...");
        return new EditorMemento(content, cursorPosition, fontFamily);
    }
    
    // Restore from memento
    public void restore(EditorMemento memento) {
        this.content = memento.getContent();
        this.cursorPosition = memento.getCursorPosition();
        this.fontFamily = memento.getFontFamily();
        System.out.println("State restored to: " + content);
    }
    
    public void display() {
        System.out.println("Content: '" + content + "'");
        System.out.println("Cursor at: " + cursorPosition);
        System.out.println("Font: " + fontFamily);
    }
}
```

**Caretaker (History Manager)**
```java
class History {
    private final Stack<EditorMemento> undoStack = new Stack<>();
    private final Stack<EditorMemento> redoStack = new Stack<>();
    private final int maxHistorySize;
    
    public History(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
    }
    
    public void save(EditorMemento memento) {
        if (undoStack.size() >= maxHistorySize) {
            undoStack.remove(0); // Remove oldest
        }
        undoStack.push(memento);
        redoStack.clear(); // Clear redo stack on new save
        System.out.println("State saved to history (total: " + undoStack.size() + ")");
    }
    
    public EditorMemento undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo");
            return null;
        }
        EditorMemento memento = undoStack.pop();
        redoStack.push(memento);
        
        if (undoStack.isEmpty()) {
            return null;
        }
        return undoStack.peek();
    }
    
    public EditorMemento redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo");
            return null;
        }
        EditorMemento memento = redoStack.pop();
        undoStack.push(memento);
        return memento;
    }
    
    public boolean canUndo() {
        return undoStack.size() > 1;
    }
    
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        History history = new History(10);
        
        // Initial state
        history.save(editor.save());
        
        System.out.println("\n=== Writing 'Hello' ===");
        editor.write("Hello");
        editor.display();
        history.save(editor.save());
        
        System.out.println("\n=== Writing ' World' ===");
        editor.write(" World");
        editor.display();
        history.save(editor.save());
        
        System.out.println("\n=== Changing font ===");
        editor.setFont("Times New Roman");
        editor.display();
        history.save(editor.save());
        
        System.out.println("\n=== Undo Operation ===");
        EditorMemento memento = history.undo();
        if (memento != null) {
            editor.restore(memento);
            editor.display();
        }
        
        System.out.println("\n=== Undo Operation ===");
        memento = history.undo();
        if (memento != null) {
            editor.restore(memento);
            editor.display();
        }
        
        System.out.println("\n=== Redo Operation ===");
        memento = history.redo();
        if (memento != null) {
            editor.restore(memento);
            editor.display();
        }
    }
}
```

## Benefits

1. **Encapsulation**: Preserves object encapsulation boundaries
2. **Simplifies Originator**: Originator doesn't need to manage its history
3. **Undo/Redo**: Easy implementation of undo/redo functionality
4. **Snapshots**: Ability to create state snapshots at any time
5. **State Independence**: Memento is independent of the Originator

## Drawbacks

1. **Memory Overhead**: Storing many mementos can consume significant memory
2. **Cost of Creation**: Creating mementos for complex objects can be expensive
3. **Caretaker Management**: Caretaker must track memento lifecycle
4. **Serialization Complexity**: Deep copying complex objects can be challenging
5. **History Size**: Need to limit history size to prevent memory issues

## When to Use

Apply the Memento pattern when:

- You need to save and restore an object's state
- You want to implement undo/redo functionality
- Direct access to object state would violate encapsulation
- You need to create snapshots or checkpoints
- You want to implement transactional behavior
- You need to preserve object history

## Real-World Examples

1. **Text Editors**: Undo/redo functionality (Microsoft Word, VS Code)
2. **Graphics Applications**: State snapshots (Photoshop history)
3. **Database Transactions**: Savepoints and rollback
4. **Game Development**: Save game states, checkpoints
5. **Version Control**: State tracking (Git commits)
6. **Form Wizards**: Multi-step forms with back navigation
7. **Configuration Management**: Backup and restore settings

## Class Diagram

```
┌─────────────────────┐
│   TextEditor        │
│   (Originator)      │
├─────────────────────┤
│ - content: String   │
│ - cursorPos: int    │
│ - fontFamily: String│
├─────────────────────┤
│ + write()           │
│ + save(): Memento   │◁─────────┐
│ + restore(Memento)  │          │
└─────────────────────┘          │
                                 │
                            creates/uses
                                 │
┌─────────────────────┐          │
│  EditorMemento      │◁─────────┘
│  (Memento)          │
├─────────────────────┤
│ - content: String   │
│ - cursorPos: int    │
│ - fontFamily: String│
│ - timestamp: long   │
├─────────────────────┤
│ + getContent()      │
│ + getCursorPos()    │
│ + getFontFamily()   │
└─────────────────────┘
         △
         │
      stores
         │
┌────────┴────────────┐
│   History           │
│   (Caretaker)       │
├─────────────────────┤
│ - undoStack: Stack  │
│ - redoStack: Stack  │
│ - maxSize: int      │
├─────────────────────┤
│ + save(Memento)     │
│ + undo(): Memento   │
│ + redo(): Memento   │
│ + canUndo(): bool   │
│ + canRedo(): bool   │
└─────────────────────┘
```

## Output Example

```
Saving state...
State saved to history (total: 1)

=== Writing 'Hello' ===
Written: Hello
Content: 'Hello'
Cursor at: 5
Font: Arial
Saving state...
State saved to history (total: 2)

=== Writing ' World' ===
Written:  World
Content: 'Hello World'
Cursor at: 11
Font: Arial
Saving state...
State saved to history (total: 3)

=== Changing font ===
Content: 'Hello World'
Cursor at: 11
Font: Times New Roman
Saving state...
State saved to history (total: 4)

=== Undo Operation ===
State restored to: Hello World
Content: 'Hello World'
Cursor at: 11
Font: Arial

=== Undo Operation ===
State restored to: Hello
Content: 'Hello'
Cursor at: 5
Font: Arial

=== Redo Operation ===
State restored to: Hello World
Content: 'Hello World'
Cursor at: 11
Font: Arial
```

## Advanced Implementations

### Incremental Memento (Delta-based)

Instead of storing full state, store only changes:

```java
class IncrementalMemento {
    private final String operation;
    private final Object[] parameters;
    private final long timestamp;
    
    public IncrementalMemento(String operation, Object... params) {
        this.operation = operation;
        this.parameters = params;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Undo by applying reverse operation
    public void undo(TextEditor editor) {
        switch (operation) {
            case "write":
                editor.delete((String) parameters[0]);
                break;
            case "delete":
                editor.write((String) parameters[0]);
                break;
        }
    }
}
```

### Serializable Memento

For persistent storage:

```java
class SerializableMemento implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String content;
    private final int cursorPosition;
    private final String fontFamily;
    
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }
    
    public static SerializableMemento loadFromFile(String filename) 
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (SerializableMemento) ois.readObject();
        }
    }
}
```

### Compressed Memento

For memory efficiency:

```java
class CompressedMemento {
    private final byte[] compressedState;
    
    public CompressedMemento(String state) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(state.getBytes());
        }
        this.compressedState = baos.toByteArray();
    }
    
    public String decompress() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedState);
        try (GZIPInputStream gzip = new GZIPInputStream(bais)) {
            return new String(gzip.readAllBytes());
        }
    }
}
```

## Memory Management Strategies

### Limited History Size

```java
class LimitedHistory {
    private final int maxSize;
    private final LinkedList<Memento> mementos = new LinkedList<>();
    
    public void save(Memento memento) {
        if (mementos.size() >= maxSize) {
            mementos.removeFirst(); // Remove oldest
        }
        mementos.addLast(memento);
    }
}
```

### Time-based Expiration

```java
class ExpiringHistory {
    private final Map<Long, Memento> mementos = new HashMap<>();
    private final long expirationMs;
    
    public void cleanup() {
        long now = System.currentTimeMillis();
        mementos.entrySet().removeIf(entry -> 
            now - entry.getKey() > expirationMs
        );
    }
}
```

## Comparison with Similar Patterns

| Pattern | Purpose | Key Difference |
|---------|---------|----------------|
| **Memento** | Save/restore object state | Preserves encapsulation, opaque to caretaker |
| **Command** | Encapsulate actions | Focuses on operations, not state |
| **Prototype** | Clone objects | Creates new instances, not state snapshots |
| **Serialization** | Persist objects | Language-specific, less control over state |

## Best Practices

1. **Immutable Mementos**: Make mementos immutable to prevent accidental modification
2. **Limit History**: Implement history size limits to prevent memory overflow
3. **Lazy Creation**: Create mementos only when needed
4. **Compression**: Consider compressing large state data
5. **Timestamps**: Include timestamps for history tracking
6. **Deep Copy**: Ensure deep copying of mutable objects in state
7. **Resource Cleanup**: Clean up old mementos to prevent memory leaks

## Conclusion

The Memento pattern provides an elegant solution for implementing undo/redo functionality and state management while preserving encapsulation. By separating the responsibility of state storage from the object itself, it enables powerful features like history tracking, snapshots, and rollback mechanisms without compromising object design principles. While it requires careful memory management, especially for applications with large state or long histories, the pattern's benefits in terms of maintainability and functionality make it essential for applications requiring state restoration capabilities.