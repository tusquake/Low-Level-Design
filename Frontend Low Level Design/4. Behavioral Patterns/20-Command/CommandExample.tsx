import React from 'react';

/**
 * Command Pattern
 * 
 * Encapsulates a request as an object, thereby letting you parameterize 
 * clients with different requests, queue or log requests, and support 
 * undoable operations.
 */

// 1. COMMAND INTERFACE
interface Command {
  execute: () => void;
  undo: () => void;
}

// 2. CONCRETE COMMANDS
class IncrementCommand implements Command {
  constructor(private state: { value: number }, private amount: number) {}
  execute() { this.state.value += this.amount; }
  undo() { this.state.value -= this.amount; }
}

class DecrementCommand implements Command {
  constructor(private state: { value: number }, private amount: number) {}
  execute() { this.state.value -= this.amount; }
  undo() { this.state.value += this.amount; }
}

// 3. USAGE IN REACT
export const CommandExample = () => {
  const [count, setCount] = React.useState(0);
  const [history, setHistory] = React.useState<Command[]>([]);

  // We use a ref to hold the mutable state for the commands to act upon
  // before we sync it back to React state.
  const stateRef = React.useRef({ value: 0 });

  const runCommand = (cmd: Command) => {
    cmd.execute();
    stateRef.current.value = stateRef.current.value; // trigger sync
    setCount(stateRef.current.value);
    setHistory(prev => [...prev, cmd]);
  };

  const handleUndo = () => {
    const lastCmd = history[history.length - 1];
    if (lastCmd) {
      lastCmd.undo();
      setCount(stateRef.current.value);
      setHistory(prev => prev.slice(0, -1));
    }
  };

  return (
    <div>
      <h1>Command Pattern</h1>
      <p>Count: {count}</p>
      <button onClick={() => runCommand(new IncrementCommand(stateRef.current, 1))}>+1</button>
      <button onClick={() => runCommand(new DecrementCommand(stateRef.current, 1))}>-1</button>
      <button onClick={handleUndo} disabled={history.length === 0}>Undo Last Action</button>
      
      <div style={{ marginTop: '10px' }}>
        <strong>History Size:</strong> {history.length}
      </div>
    </div>
  );
};
