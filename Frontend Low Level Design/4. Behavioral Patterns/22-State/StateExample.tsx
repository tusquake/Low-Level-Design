import React from 'react';

/**
 * State Pattern
 * 
 * Allows an object to alter its behavior when its internal state changes.
 * The object will appear to change its class.
 */

// 1. STATE INTERFACE
interface TrafficLightState {
  color: string;
  next: () => TrafficLightState;
}

// 2. CONCRETE STATES
const RedState: TrafficLightState = {
  color: 'red',
  next: () => GreenState
};

const GreenState: TrafficLightState = {
  color: 'green',
  next: () => YellowState
};

const YellowState: TrafficLightState = {
  color: 'yellow',
  next: () => RedState
};

// 3. USAGE IN REACT
export const StateExample = () => {
  // We store the current "State Object" in React state
  const [currentState, setCurrentState] = React.useState<TrafficLightState>(RedState);

  const handleNext = () => {
    // The behavior (what happens next) is encapsulated in the state object itself
    setCurrentState(currentState.next());
  };

  return (
    <div>
      <h1>State Pattern</h1>
      <p>The transition logic is inside the state objects, not the component.</p>
      
      <div style={{ 
        width: '50px', 
        height: '50px', 
        borderRadius: '50%', 
        backgroundColor: currentState.color,
        border: '2px solid black',
        margin: '10px 0'
      }} />
      
      <button onClick={handleNext}>Change Light</button>
      <p>Current Color: {currentState.color.toUpperCase()}</p>
    </div>
  );
};
