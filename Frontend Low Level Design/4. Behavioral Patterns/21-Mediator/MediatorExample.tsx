import React from 'react';

/**
 * Mediator Pattern
 * 
 * Defines an object that encapsulates how a set of objects interact.
 * Mediator promotes loose coupling by keeping objects from referring 
 * to each other explicitly.
 */

// 1. THE MEDIATOR (A central coordinator)
// In React, this is often a Parent Component or a Context Provider.
const ChatMediator = () => {
  const [messages, setMessages] = React.useState<string[]>([]);

  const sendMessage = (from: string, msg: string) => {
    setMessages(prev => [...prev, `${from}: ${msg}`]);
  };

  return (
    <div style={{ border: '2px solid blue', padding: '20px' }}>
      <h2>Chat Room (Mediator)</h2>
      <div style={{ marginBottom: '20px' }}>
        {messages.map((m, i) => <p key={i}>{m}</p>)}
      </div>
      
      <div style={{ display: 'flex', gap: '20px' }}>
        {/* The components don't talk to each other, only to the mediator */}
        <UserComponent name="Alice" onSend={sendMessage} />
        <UserComponent name="Bob" onSend={sendMessage} />
      </div>
    </div>
  );
};

// 2. THE COLLEAGUES (Components interacting via the Mediator)
const UserComponent = ({ name, onSend }: { name: string, onSend: (n: string, m: string) => void }) => {
  const [text, setText] = React.useState('');

  return (
    <div style={{ border: '1px gray solid', padding: '10px' }}>
      <h3>{name}</h3>
      <input value={text} onChange={(e) => setText(e.target.value)} />
      <button onClick={() => { onSend(name, text); setText(''); }}>Send</button>
    </div>
  );
};

// 3. USAGE
export const MediatorExample = () => {
  return (
    <div>
      <h1>Mediator Pattern</h1>
      <ChatMediator />
    </div>
  );
};
