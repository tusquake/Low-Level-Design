import React from 'react';

/**
 * State + Observer Pattern Combination
 * 
 * State: Manages the internal condition and behavior of an object.
 * Observer: Notifies listeners whenever that state changes.
 */

// 1. STATE INTERFACE
interface AppState {
  status: string;
  color: string;
  next: () => AppState;
}

// 2. CONCRETE STATES
const IdleState: AppState = {
  status: 'Idle',
  color: 'gray',
  next: () => LoadingState
};

const LoadingState: AppState = {
  status: 'Loading...',
  color: 'orange',
  next: () => SuccessState
};

const SuccessState: AppState = {
  status: 'Success!',
  color: 'green',
  next: () => IdleState
};

// 3. SUBJECT (The observable state manager)
class StateManager {
  private currentState: AppState = IdleState;
  private listeners: ((state: AppState) => void)[] = [];

  subscribe(l: (state: AppState) => void) {
    this.listeners.push(l);
    l(this.currentState); // Initial notification
    return () => { this.listeners = this.listeners.filter(x => x !== l); };
  }

  transition() {
    this.currentState = this.currentState.next();
    this.listeners.forEach(l => l(this.currentState));
  }
}

const globalManager = new StateManager();

// 4. OBSERVER COMPONENTS
const StateDisplay = () => {
  const [state, setState] = React.useState<AppState>(IdleState);
  React.useEffect(() => globalManager.subscribe(setState), []);

  return (
    <div style={{ 
      padding: '20px', 
      backgroundColor: state.color, 
      color: 'white',
      borderRadius: '8px',
      margin: '10px 0'
    }}>
      Current Status: {state.status}
    </div>
  );
};

// 5. USAGE
export const StateObserverExample = () => {
  return (
    <div>
      <h1>State + Observer Pattern</h1>
      <p>Clicking the button transitions the state, and all observers are notified.</p>
      
      <StateDisplay />
      
      <button onClick={() => globalManager.transition()}>Next State</button>
      
      <div style={{ marginTop: '20px', fontSize: 'small' }}>
        <em>An extra observer listening for "Success" state:</em>
        <SuccessListener />
      </div>
    </div>
  );
};

const SuccessListener = () => {
  const [isSuccess, setIsSuccess] = React.useState(false);
  React.useEffect(() => {
    return globalManager.subscribe(s => setIsSuccess(s.status === 'Success!'));
  }, []);

  return isSuccess ? <p style={{ color: 'green' }}>🎉 Congratulations! Operation Succeeded.</p> : null;
};
