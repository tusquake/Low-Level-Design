import React from 'react';

/**
 * Proxy Pattern
 * 
 * Provides a placeholder for another object to control access, 
 * add logging, or implement "lazy loading."
 * In JavaScript, the 'Proxy' object is a first-class citizen of the language.
 */

// 1. ORIGINAL OBJECT
interface UserData {
  id: number;
  name: string;
}

const realUser: UserData = { id: 1, name: 'Alice' };

// 2. PROXY (Using JS Proxy API)
const userProxy = new Proxy(realUser, {
  get: (target, prop: string) => {
    console.log(`[PROXY] Accessing property: ${prop}`);
    // Example: Add a restriction or transform
    if (prop === 'name') return target[prop].toUpperCase();
    return target[prop as keyof UserData];
  },
  set: (target, prop: string, value) => {
    console.log(`[PROXY] Setting ${prop} to ${value}`);
    // Example: Validation
    if (prop === 'id' && value < 0) {
      console.error('Invalid ID');
      return false;
    }
    (target as any)[prop] = value;
    return true;
  }
});

// 3. USAGE IN REACT
export const ProxyExample = () => {
  const [log, setLog] = React.useState<string[]>([]);

  const testProxy = () => {
    const msg = `Name from Proxy: ${userProxy.name}`;
    setLog(prev => [...prev, msg]);
    
    // Try to set invalid ID
    userProxy.id = -10; 
  };

  return (
    <div>
      <h1>Proxy Pattern</h1>
      <button onClick={testProxy}>Interactions with Proxy</button>
      <div style={{ background: '#eee', padding: '10px', marginTop: '10px' }}>
        <h3>Logs:</h3>
        {log.map((l, i) => <p key={i}>{l}</p>)}
        <p>Check console for SET/GET logs!</p>
      </div>
    </div>
  );
};
