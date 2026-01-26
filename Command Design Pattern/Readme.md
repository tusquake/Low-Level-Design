# Command Design Pattern Overview

The Command pattern is a behavioral design pattern that encapsulates a request as an object, thereby allowing you to parameterize clients with different requests, queue or log requests, and support undoable operations. It decouples the object that invokes the operation from the one that knows how to perform it.

## Problem Statement

In traditional approaches, when you need to execute operations, the caller directly invokes methods on objects. This creates tight coupling and makes it difficult to implement features like undo/redo, queuing, logging, or parameterizing objects with operations.

### Without Command Pattern

Consider a remote control system for home devices:

```java
if (button == 1) {
    television.turnOn();
} else if (button == 2) {
    television.turnOff();
} else if (button == 3) {
    television.volumeUp();
} else if (button == 4) {
    stereo.turnOn();
}
```

**Issues:**
- Remote control is tightly coupled to specific devices
- Cannot implement undo/redo functionality
- Difficult to queue or log operations
- Cannot parameterize buttons with different actions
- Adding new commands requires modifying remote control code

## Solution

The Command pattern turns requests into standalone objects that contain all information about the request. This transformation allows you to pass requests as method arguments, delay or queue a request's execution, and support undoable operations.

## Architecture

```
Client → Invoker → Command Interface → ConcreteCommand → Receiver
                      (execute())           ↓
                                        receiver.action()
```

## Implementation

### Core Components

**Command Interface**
```java
interface Command {
    void execute();
    void undo();
}
```

**Receiver Classes**
```java
class Television {
    private boolean isOn = false;
    private int volume = 10;
    
    public void turnOn() {
        isOn = true;
        System.out.println("Television is ON");
    }
    
    public void turnOff() {
        isOn = false;
        System.out.println("Television is OFF");
    }
    
    public void volumeUp() {
        if (isOn) {
            volume++;
            System.out.println("Volume increased to " + volume);
        }
    }
    
    public void volumeDown() {
        if (isOn && volume > 0) {
            volume--;
            System.out.println("Volume decreased to " + volume);
        }
    }
}
```

**Concrete Commands**
```java
class TurnOnCommand implements Command {
    private Television television;
    
    public TurnOnCommand(Television television) {
        this.television = television;
    }
    
    @Override
    public void execute() {
        television.turnOn();
    }
    
    @Override
    public void undo() {
        television.turnOff();
    }
}

class VolumeUpCommand implements Command {
    private Television television;
    
    public VolumeUpCommand(Television television) {
        this.television = television;
    }
    
    @Override
    public void execute() {
        television.volumeUp();
    }
    
    @Override
    public void undo() {
        television.volumeDown();
    }
}
```

**Invoker (Remote Control)**
```java
class RemoteControl {
    private Command[] buttons;
    private Stack<Command> commandHistory;
    
    public RemoteControl(int numberOfButtons) {
        buttons = new Command[numberOfButtons];
        commandHistory = new Stack<>();
        
        // Set default "no operation" command for all buttons
        Command noCommand = new NoCommand();
        for (int i = 0; i < numberOfButtons; i++) {
            buttons[i] = noCommand;
        }
    }
    
    public void setCommand(int slot, Command command) {
        buttons[slot] = command;
    }
    
    public void pressButton(int slot) {
        if (slot >= 0 && slot < buttons.length) {
            buttons[slot].execute();
            commandHistory.push(buttons[slot]);
        }
    }
    
    public void pressUndo() {
        if (!commandHistory.isEmpty()) {
            Command command = commandHistory.pop();
            command.undo();
            System.out.println("Undo executed");
        } else {
            System.out.println("No commands to undo");
        }
    }
}
```

**Null Object Pattern (No Command)**
```java
class NoCommand implements Command {
    @Override
    public void execute() {
        System.out.println("No command assigned");
    }
    
    @Override
    public void undo() {
        // Do nothing
    }
}
```

### Usage

```java
public class CommandPatternDemo {
    public static void main(String[] args) {
        // Create receivers
        Television tv = new Television();
        
        // Create commands
        Command turnOn = new TurnOnCommand(tv);
        Command volumeUp = new VolumeUpCommand(tv);
        
        // Create invoker
        RemoteControl remote = new RemoteControl(5);
        
        // Configure buttons
        remote.setCommand(0, turnOn);
        remote.setCommand(1, volumeUp);
        
        // Use remote control
        System.out.println("--- Pressing button 0 ---");
        remote.pressButton(0);  // Turn on TV
        
        System.out.println("\n--- Pressing button 1 ---");
        remote.pressButton(1);  // Volume up
        
        System.out.println("\n--- Pressing undo ---");
        remote.pressUndo();     // Undo volume up
        
        System.out.println("\n--- Pressing undo ---");
        remote.pressUndo();     // Undo turn on
    }
}
```

## Benefits

1. **Decoupling**: Separates objects that invoke operations from objects that perform operations
2. **Extensibility**: Easy to add new commands without changing existing code
3. **Undo/Redo**: Simple to implement reversible operations
4. **Macro Commands**: Can compose commands to create complex operations
5. **Queuing**: Commands can be queued for delayed execution
6. **Logging**: All operations can be logged for audit trails
7. **Transactional Behavior**: Can implement rollback functionality

## Drawbacks

1. **Increased Complexity**: More classes to manage (one per command)
2. **Memory Overhead**: Storing command history for undo can consume memory
3. **Boilerplate Code**: Each command requires its own class

## When to Use

Apply the Command pattern when:

- You want to parameterize objects with operations
- You want to queue operations, schedule their execution, or execute them remotely
- You want to implement reversible operations (undo/redo)
- You want to log changes for crash recovery or audit purposes
- You want to structure a system around high-level operations built on primitive operations
- You need to decouple the object that invokes the operation from the object that performs it

## Real-World Examples

1. **GUI Buttons and Menu Items** - Each button/menu item executes a command
2. **Text Editors** - Undo/redo functionality (type, delete, format commands)
3. **Transaction Systems** - Database transactions with commit/rollback
4. **Job Queues** - Background job processing systems
5. **Macro Recording** - Recording and replaying user actions
6. **Remote Controls** - Universal remote controls for various devices
7. **Wizard/Multi-step Forms** - Navigation with back/forward functionality

## Class Diagram

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ creates
       ▼
┌─────────────┐       ┌──────────────┐
│   Invoker   │──────>│   Command    │◁─────────┐
│             │ uses  │ «interface»  │          │
│ - command   │       ├──────────────┤          │
│ + execute() │       │ + execute()  │          │
└─────────────┘       │ + undo()     │          │
                      └──────────────┘          │
                             △                  │
                             │ implements       │
                    ┌────────┴────────┐         │
                    │                 │         │
            ┌───────┴────────┐ ┌─────┴──────┐  │
            │ ConcreteCommand│ │ ConcreteCmd│  │
            │       A        │ │      B     │  │
            ├────────────────┤ └────────────┘  │
            │ - receiver     │                 │
            │ + execute()    │─────────────────┘
            │ + undo()       │      uses
            └────────────────┘
                    │
                    ▼
            ┌────────────────┐
            │   Receiver     │
            ├────────────────┤
            │ + action()     │
            └────────────────┘
```

## Output Example

```
--- Pressing button 0 ---
Television is ON

--- Pressing button 1 ---
Volume increased to 11

--- Pressing undo ---
Volume decreased to 10
Undo executed

--- Pressing undo ---
Television is OFF
Undo executed

--- Pressing undo ---
No commands to undo
```

## Variations

### Macro Command

Combines multiple commands into a single command:

```java
class MacroCommand implements Command {
    private List<Command> commands;
    
    public MacroCommand(List<Command> commands) {
        this.commands = commands;
    }
    
    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }
    
    @Override
    public void undo() {
        // Undo in reverse order
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }
}
```

### Parameterized Command

Commands that accept parameters at execution time:

```java
interface ParameterizedCommand<T> {
    void execute(T parameter);
}
```

### Asynchronous Command

Commands that execute asynchronously:

```java
class AsyncCommand implements Command {
    private Receiver receiver;
    
    @Override
    public void execute() {
        CompletableFuture.runAsync(() -> receiver.action());
    }
}
```

## Advanced Features

### Command Queue

```java
class CommandQueue {
    private Queue<Command> queue = new LinkedList<>();
    
    public void addCommand(Command command) {
        queue.offer(command);
    }
    
    public void processCommands() {
        while (!queue.isEmpty()) {
            Command command = queue.poll();
            command.execute();
        }
    }
}
```

### Command Logger

```java
class CommandLogger {
    private List<Command> executedCommands = new ArrayList<>();
    
    public void log(Command command) {
        executedCommands.add(command);
        System.out.println("Logged: " + command.getClass().getSimpleName());
    }
    
    public void replay() {
        for (Command command : executedCommands) {
            command.execute();
        }
    }
}
```

## Comparison with Other Patterns

| Pattern | Purpose | Key Difference |
|---------|---------|----------------|
| **Command** | Encapsulate request as object | Focuses on parameterizing and queuing operations |
| **Strategy** | Encapsulate algorithm | Focuses on interchangeable algorithms |
| **Chain of Responsibility** | Pass request along chain | Focuses on handler selection |
| **Observer** | Notify multiple objects | Focuses on event notification |

## Best Practices

1. **Keep Commands Simple**: Each command should do one thing well
2. **Implement Undo Carefully**: Ensure undo operations truly reverse the execute operation
3. **Use Null Object**: Implement a NoCommand to avoid null checks
4. **Consider Memory**: For undo, limit history size or implement cleanup
5. **Thread Safety**: Make commands thread-safe if used in concurrent environments
6. **Immutable State**: Store necessary state in command objects for reliable undo

## Conclusion

The Command pattern provides a powerful way to encapsulate requests as objects, enabling flexible operation handling, undo/redo functionality, and decoupling between invokers and receivers. It's particularly valuable in scenarios requiring operation queuing, logging, or reversible actions. While it introduces additional classes, the benefits of flexibility, extensibility, and maintainability often outweigh the increased complexity.