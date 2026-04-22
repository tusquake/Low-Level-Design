import React from 'react';

/**
 * Observer + Command Pattern Combination
 * 
 * Command: Encapsulates an action.
 * Observer: Notifies listeners when a command is executed.
 */

// 1. COMMAND
interface Command {
  execute: () => void;
}

// 2. OBSERVABLE COMMAND BUS (Subject)
class CommandBus {
  private listeners: ((cmd: Command) => void)[] = [];

  subscribe(listener: (cmd: Command) => void) {
    this.listeners.push(listener);
    return () => { this.listeners = this.listeners.filter(l => l !== listener); };
  }

  dispatch(cmd: Command) {
    cmd.execute();
    this.listeners.forEach(l => l(cmd));
  }
}

const globalBus = new CommandBus();

// 3. CONCRETE COMMAND
class AlertCommand implements Command {
  constructor(public message: string) {}
  execute() { console.log('Executing:', this.message); }
}

// 4. USAGE IN REACT
export const ObserverCommandExample = () => {
  const [history, setHistory] = React.useState<string[]>([]);

  React.useEffect(() => {
    // Observer: Listen for any command dispatched in the app
    return globalBus.subscribe((cmd) => {
      if (cmd instanceof AlertCommand) {
        setHistory(prev => [...prev, cmd.message]);
      }
    });
  }, []);

  const handleClick = () => {
    const cmd = new AlertCommand(`Action at ${new Date().toLocaleTimeString()}`);
    globalBus.dispatch(cmd);
  };

  return (
    <div>
      <h1>Observer + Command Pattern</h1>
      <button onClick={handleClick}>Dispatch Alert Command</button>
      
      <h3>Command History (Observed):</h3>
      <ul>
        {history.map((h, i) => <li key={i}>{h}</li>)}
      </ul>
    </div>
  );
};
