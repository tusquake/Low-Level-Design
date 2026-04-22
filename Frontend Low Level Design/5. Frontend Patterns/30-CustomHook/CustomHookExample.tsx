import React from 'react';

/**
 * Custom Hook Pattern
 * 
 * Allows you to extract component logic into reusable functions.
 * Hooks allow you to share stateful logic without changing your 
 * component hierarchy.
 */

// 1. THE CUSTOM HOOK
const useWindowWidth = () => {
  const [width, setWidth] = React.useState(window.innerWidth);

  React.useEffect(() => {
    const handleResize = () => setWidth(window.innerWidth);
    window.addEventListener('resize', handleResize);
    
    // Cleanup
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return width;
};

// 2. USAGE IN REACT
export const CustomHookExample = () => {
  const width = useWindowWidth();

  return (
    <div>
      <h1>Custom Hook Pattern</h1>
      <p>The logic to track window width is encapsulated in <code>useWindowWidth</code>.</p>
      
      <div style={{ 
        padding: '20px', 
        background: width > 800 ? 'lightgreen' : 'lightcoral',
        transition: 'background 0.3s'
      }}>
        Current Width: <strong>{width}px</strong>
        <p>{width > 800 ? 'Desktop View' : 'Mobile View'}</p>
      </div>
    </div>
  );
};
