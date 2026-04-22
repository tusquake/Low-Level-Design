import React from 'react';

/**
 * Factory Pattern
 * 
 * Provides an interface for creating objects without specifying their exact classes.
 * In React, this is often used to create components dynamically based on configuration or types.
 */

// 1. PRODUCTS (Different types of components)
const PrimaryButton = () => <button style={{ background: 'blue', color: 'white' }}>Primary</button>;
const DangerButton = () => <button style={{ background: 'red', color: 'white' }}>Danger</button>;
const GhostButton = () => <button style={{ border: '1px solid gray' }}>Ghost</button>;

// 2. FACTORY (The logic to decide which component to return)
const ButtonFactory = ({ type }: { type: 'primary' | 'danger' | 'ghost' }) => {
  switch (type) {
    case 'primary': return <PrimaryButton />;
    case 'danger': return <DangerButton />;
    case 'ghost': return <GhostButton />;
    default: return <span>Unknown Button Type</span>;
  }
};

// 3. USAGE
export const FactoryExample = () => {
  return (
    <div>
      <h1>Factory Pattern</h1>
      <div style={{ display: 'flex', gap: '10px' }}>
        <ButtonFactory type="primary" />
        <ButtonFactory type="danger" />
        <ButtonFactory type="ghost" />
      </div>
    </div>
  );
};
