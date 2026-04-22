import React from 'react';

/**
 * Decorator Pattern
 * 
 * Attaches additional responsibilities to an object dynamically.
 * In React, this is historically achieved via Higher-Order Components (HOCs)
 * or more recently via Component Composition.
 */

// 1. BASE COMPONENT
interface ButtonProps {
  label: string;
  onClick: () => void;
}

const SimpleButton: React.FC<ButtonProps> = ({ label, onClick }) => (
  <button onClick={onClick}>{label}</button>
);

// 2. DECORATORS (HOCs)

// Adds Logging capability
function withLogging<T extends ButtonProps>(Component: React.ComponentType<T>) {
  return (props: T) => {
    const handleClick = () => {
      console.log(`[LOG] Button "${props.label}" clicked.`);
      props.onClick();
    };
    return <Component {...props} onClick={handleClick} />;
  };
}

// Adds "Border" styling
function withBorder<T extends ButtonProps>(Component: React.ComponentType<T>) {
  return (props: T) => (
    <div style={{ border: '2px solid red', padding: '5px', display: 'inline-block' }}>
      <Component {...props} />
    </div>
  );
}

// 3. ENHANCED COMPONENTS
const LoggedButton = withLogging(SimpleButton);
const FancyLoggedButton = withBorder(LoggedButton);

// 4. USAGE
export const DecoratorExample = () => {
  return (
    <div>
      <h1>Decorator Pattern (via HOCs)</h1>
      <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <SimpleButton label="Simple Button" onClick={() => {}} />
        <LoggedButton label="Logged Button" onClick={() => {}} />
        <FancyLoggedButton label="Fancy Logged Button" onClick={() => {}} />
      </div>
    </div>
  );
};
