import React from 'react';

/**
 * Controlled vs Uncontrolled Components
 * 
 * Controlled: State is handled by React (via value/onChange).
 * Uncontrolled: State is handled by the DOM (via refs).
 */

// 1. CONTROLLED COMPONENT
const ControlledInput = () => {
  const [val, setVal] = React.useState('');

  return (
    <div style={{ border: '1px solid blue', padding: '10px', marginBottom: '10px' }}>
      <h3>Controlled</h3>
      <input value={val} onChange={(e) => setVal(e.target.value)} />
      <p>Current State: {val}</p>
    </div>
  );
};

// 2. UNCONTROLLED COMPONENT
const UncontrolledInput = () => {
  const inputRef = React.useRef<HTMLInputElement>(null);

  const handleSubmit = () => {
    alert(`Value from DOM ref: ${inputRef.current?.value}`);
  };

  return (
    <div style={{ border: '1px solid green', padding: '10px' }}>
      <h3>Uncontrolled</h3>
      <input ref={inputRef} defaultValue="Default Value" />
      <button onClick={handleSubmit}>Get Value</button>
    </div>
  );
};

// 3. USAGE
export const ControlledUncontrolledExample = () => {
  return (
    <div>
      <h1>Controlled vs Uncontrolled</h1>
      <ControlledInput />
      <UncontrolledInput />
    </div>
  );
};
