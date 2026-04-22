import React from 'react';

/**
 * KISS (Keep It Simple, Stupid)
 * 
 * Avoid over-engineering. Write the simplest code that solves the problem.
 * Simple code is easier to read, maintain, and debug.
 */

// --- OVER-ENGINEERED (Violating KISS) ---
/*
const useComplexDateFormatter = (date: Date) => {
  // Unnecessarily complex logic for a simple format
  return new Intl.DateTimeFormat('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  }).format(date);
};
*/

// --- SIMPLE (Following KISS) ---

const SimpleDate = ({ date }: { date: Date }) => {
  // Simple, readable, and standard
  const formattedDate = date.toLocaleDateString();
  
  return <span>{formattedDate}</span>;
};

// Example: Simple Toggle instead of a complex state machine for a basic UI switch
const Toggle = () => {
  const [isOn, setIsOn] = React.useState(false);

  return (
    <button 
      onClick={() => setIsOn(!isOn)}
      style={{ background: isOn ? 'green' : 'gray' }}
    >
      {isOn ? 'ON' : 'OFF'}
    </button>
  );
};

export const KISSExample = () => {
  return (
    <div>
      <h1>KISS: Keep It Simple</h1>
      <p>Simple Date: <SimpleDate date={new Date()} /></p>
      <p>Simple Toggle: <Toggle /></p>
    </div>
  );
};
