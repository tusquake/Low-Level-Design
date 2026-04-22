import React from 'react';

/**
 * Memento Pattern
 * 
 * Without violating encapsulation, captures and externalizes an object's 
 * internal state so that the object can be restored to this state later.
 * Essential for "Undo/Redo" or "Savepoints."
 */

// 1. THE MEMENTO (A snapshot of state)
interface Memento {
  getState: () => string;
}

class EditorMemento implements Memento {
  constructor(private state: string) {}
  getState() { return this.state; }
}

// 2. THE ORIGINATOR (The object whose state we want to save)
class Editor {
  private content: string = '';

  setContent(text: string) { this.content = text; }
  getContent() { return this.content; }

  save(): Memento {
    return new EditorMemento(this.content);
  }

  restore(memento: Memento) {
    this.content = memento.getState();
  }
}

// 3. USAGE IN REACT
export const MementoExample = () => {
  const [text, setText] = React.useState('');
  const [history, setHistory] = React.useState<Memento[]>([]);
  
  // Instance of the Originator
  const editorRef = React.useRef(new Editor());

  const handleSave = () => {
    editorRef.current.setContent(text);
    setHistory(prev => [...prev, editorRef.current.save()]);
  };

  const handleUndo = () => {
    const lastMemento = history[history.length - 2]; // Get previous one
    if (lastMemento) {
      editorRef.current.restore(lastMemento);
      const restoredText = editorRef.current.getContent();
      setText(restoredText);
      setHistory(prev => prev.slice(0, -1));
    }
  };

  return (
    <div>
      <h1>Memento Pattern</h1>
      <textarea 
        value={text} 
        onChange={(e) => setText(e.target.value)} 
        rows={4} 
        cols={30}
      />
      <br />
      <button onClick={handleSave}>Save Snapshot</button>
      <button onClick={handleUndo} disabled={history.length < 2}>Undo</button>
      
      <p>Snapshots in memory: {history.length}</p>
    </div>
  );
};
