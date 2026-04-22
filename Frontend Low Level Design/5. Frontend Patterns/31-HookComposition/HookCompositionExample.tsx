import React from 'react';

/**
 * Hook Composition Pattern
 * 
 * Building complex hooks by combining simpler, smaller hooks.
 * This follows the "Atomic" design principle for logic.
 */

// 1. SIMPLE ATOMIC HOOKS
const useCounter = (initial: number = 0) => {
  const [count, setCount] = React.useState(initial);
  const increment = () => setCount(c => c + 1);
  return { count, increment };
};

const useLogger = (val: any) => {
  React.useEffect(() => {
    console.log('[LOG]: Value changed to', val);
  }, [val]);
};

// 2. COMPOSED HOOK (Combines simpler hooks)
const useTrackedCounter = (name: string) => {
  const { count, increment } = useCounter(0);
  
  // Compose with logger
  useLogger(`${name}: ${count}`);

  return { count, increment };
};

// 3. USAGE IN REACT
export const HookCompositionExample = () => {
  const { count, increment } = useTrackedCounter('MainCounter');

  return (
    <div>
      <h1>Hook Composition Pattern</h1>
      <p>A complex hook built from atomic building blocks.</p>
      
      <button onClick={increment}>Count is {count}</button>
      <p>Check the console to see the effects of the composed logger hook.</p>
    </div>
  );
};
