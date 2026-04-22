import React from 'react';

/**
 * Render Props Pattern
 * 
 * A technique for sharing code between React components using a prop 
 * whose value is a function. The component uses this function to 
 * determine what to render.
 */

// 1. THE DATA PROVIDER (Handles logic)
interface MouseProviderProps {
  render: (pos: { x: number; y: number }) => React.ReactNode;
}

const MouseProvider: React.FC<MouseProviderProps> = ({ render }) => {
  const [position, setPosition] = React.useState({ x: 0, y: 0 });

  const handleMouseMove = (e: React.MouseEvent) => {
    setPosition({ x: e.clientX, y: e.clientY });
  };

  return (
    <div style={{ height: '200px', border: '1px solid gray' }} onMouseMove={handleMouseMove}>
      {/* Call the render function with the state */}
      {render(position)}
    </div>
  );
};

// 2. USAGE IN REACT
export const RenderPropsExample = () => {
  return (
    <div>
      <h1>Render Props Pattern</h1>
      <p>The MouseProvider handles the logic, but the consumer decides how to display the data.</p>

      <MouseProvider render={({ x, y }) => (
        <div style={{ padding: '20px' }}>
          <h2>Mouse Position:</h2>
          <p>X: {x}, Y: {y}</p>
        </div>
      )} />

      <hr />

      <MouseProvider render={({ x, y }) => (
        <div style={{ width: '20px', height: '20px', background: 'red', borderRadius: '50%', position: 'relative', left: x - 20, top: y - 20 }} />
      )} />
    </div>
  );
};
