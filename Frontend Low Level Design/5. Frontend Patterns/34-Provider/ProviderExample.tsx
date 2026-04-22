import React from 'react';

/**
 * Provider Pattern
 * 
 * Uses React Context to share data across the entire component tree 
 * without prop drilling.
 */

// 1. CONTEXT DEFINITION
interface ThemeContextType {
  theme: 'light' | 'dark';
  toggleTheme: () => void;
}

const ThemeContext = React.createContext<ThemeContextType | null>(null);

// 2. PROVIDER COMPONENT
const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [theme, setTheme] = React.useState<'light' | 'dark'>('light');
  
  const toggleTheme = () => setTheme(prev => prev === 'light' ? 'dark' : 'light');

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      <div style={{ 
        background: theme === 'light' ? '#fff' : '#333', 
        color: theme === 'light' ? '#000' : '#fff',
        padding: '20px',
        minHeight: '200px',
        transition: 'all 0.3s'
      }}>
        {children}
      </div>
    </ThemeContext.Provider>
  );
};

// 3. CONSUMER COMPONENTS (Deep in the tree)
const Header = () => {
  const context = React.useContext(ThemeContext);
  return <h3>Current Theme: {context?.theme.toUpperCase()}</h3>;
};

const ThemeButton = () => {
  const context = React.useContext(ThemeContext);
  return <button onClick={context?.toggleTheme}>Toggle Theme</button>;
};

// 4. USAGE
export const ProviderExample = () => {
  return (
    <ThemeProvider>
      <h1>Provider Pattern</h1>
      <p>Data is shared from the top of the tree to any child that needs it.</p>
      <Header />
      <div style={{ marginTop: '50px' }}>
        <p>This component is deeply nested...</p>
        <ThemeButton />
      </div>
    </ThemeProvider>
  );
};
