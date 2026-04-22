import React from 'react';

/**
 * Slot Pattern
 * 
 * Allows a component to have multiple "slots" where children can be inserted.
 * This is an alternative to deep prop drilling and provides more flexibility
 * than just using 'children'.
 */

// 1. COMPONENT WITH SLOTS
interface LayoutProps {
  header?: React.ReactNode;
  sidebar?: React.ReactNode;
  footer?: React.ReactNode;
  children: React.ReactNode; // The "Default" slot
}

const PageLayout: React.FC<LayoutProps> = ({ header, sidebar, footer, children }) => {
  return (
    <div style={{ display: 'grid', gridTemplateAreas: '"h h" "s m" "f f"', gap: '10px' }}>
      <header style={{ gridArea: 'h', background: '#ccc', padding: '10px' }}>
        {header || <h1>Default Header</h1>}
      </header>
      
      <aside style={{ gridArea: 's', background: '#eee', padding: '10px', width: '150px' }}>
        {sidebar || <p>Default Sidebar</p>}
      </aside>
      
      <main style={{ gridArea: 'm', background: '#fff', padding: '10px', minHeight: '100px', border: '1px solid gray' }}>
        {children}
      </main>
      
      <footer style={{ gridArea: 'f', background: '#ccc', padding: '10px' }}>
        {footer || <p>Default Footer</p>}
      </footer>
    </div>
  );
};

// 2. USAGE
export const SlotExample = () => {
  return (
    <div>
      <h1>Slot Pattern</h1>
      <PageLayout 
        header={<h1 style={{ color: 'blue' }}>Custom App Header</h1>}
        sidebar={
          <nav>
            <ul>
              <li>Home</li>
              <li>Settings</li>
            </ul>
          </nav>
        }
        footer={<p>© 2026 My Awesome App</p>}
      >
        <p>This is the main content area (The default slot).</p>
        <button onClick={() => alert('Hello')}>Action!</button>
      </PageLayout>
    </div>
  );
};
