import React from 'react';

/**
 * Prototype Pattern
 * 
 * Creates new objects by cloning an existing one (the prototype).
 * In JavaScript, this is built into the language via prototypes, but the pattern
 * specifically refers to using a '.clone()' method to duplicate stateful objects.
 */

// 1. PROTOTYPE OBJECT
interface TaskPrototype {
  title: string;
  status: 'todo' | 'done';
  clone: () => TaskPrototype;
}

class Task implements TaskPrototype {
  constructor(public title: string, public status: 'todo' | 'done') {}

  clone(): Task {
    // Deep clone logic would go here if there were nested objects
    return new Task(this.title, this.status);
  }
}

// 2. USAGE IN REACT
export const PrototypeExample = () => {
  const [tasks, setTasks] = React.useState<Task[]>([]);

  const addNewTask = () => {
    // We use a "Default Task" as a prototype
    const defaultTask = new Task('New Task', 'todo');
    
    // Instead of creating from scratch, we clone the prototype
    const clonedTask = defaultTask.clone();
    clonedTask.title = `Task ${tasks.length + 1}`;
    
    setTasks([...tasks, clonedTask]);
  };

  return (
    <div>
      <h1>Prototype Pattern</h1>
      <button onClick={addNewTask}>Clone from Prototype</button>
      <ul>
        {tasks.map((t, i) => (
          <li key={i}>{t.title} - {t.status}</li>
        ))}
      </ul>
    </div>
  );
};
