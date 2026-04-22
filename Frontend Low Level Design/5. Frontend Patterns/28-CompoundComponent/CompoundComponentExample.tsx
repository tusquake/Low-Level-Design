import React from 'react';

/**
 * Compound Component Pattern
 * 
 * Allows several components to work together to share state and logic implicitly.
 * Provides a clean, declarative API for complex UI elements like Tabs, Selects, or Accordions.
 */

// 1. CONTEXT (To share state between components)
const AccordionContext = React.createContext<{ 
  openIndex: number | null; 
  setOpenIndex: (i: number | null) => void 
} | null>(null);

// 2. PARENT COMPONENT
const Accordion: React.FC<{ children: React.ReactNode }> & {
  Item: React.FC<{ index: number; children: React.ReactNode }>;
  Header: React.FC<{ index: number; title: string }>;
  Content: React.FC<{ index: number; children: React.ReactNode }>;
} = ({ children }) => {
  const [openIndex, setOpenIndex] = React.useState<number | null>(null);

  return (
    <AccordionContext.Provider value={{ openIndex, setOpenIndex }}>
      <div style={{ border: '1px solid gray' }}>{children}</div>
    </AccordionContext.Provider>
  );
};

// 3. CHILD COMPONENTS (Sub-components)

Accordion.Item = ({ index, children }) => (
  <div style={{ borderBottom: '1px solid #ccc' }}>{children}</div>
);

Accordion.Header = ({ index, title }) => {
  const context = React.useContext(AccordionContext);
  if (!context) throw new Error('Header must be used within Accordion');

  return (
    <div 
      style={{ padding: '10px', cursor: 'pointer', background: '#eee' }}
      onClick={() => context.setOpenIndex(context.openIndex === index ? null : index)}
    >
      {title} {context.openIndex === index ? '▲' : '▼'}
    </div>
  );
};

Accordion.Content = ({ index, children }) => {
  const context = React.useContext(AccordionContext);
  if (!context) throw new Error('Content must be used within Accordion');

  if (context.openIndex !== index) return null;
  return <div style={{ padding: '10px' }}>{children}</div>;
};

// 4. USAGE
export const CompoundComponentExample = () => {
  return (
    <div>
      <h1>Compound Component Pattern</h1>
      <p>Declarative and flexible Accordion.</p>

      <Accordion>
        <Accordion.Item index={0}>
          <Accordion.Header index={0} title="Section 1" />
          <Accordion.Content index={0}>This is content for section 1.</Accordion.Content>
        </Accordion.Item>

        <Accordion.Item index={1}>
          <Accordion.Header index={1} title="Section 2" />
          <Accordion.Content index={1}>This is content for section 2.</Accordion.Content>
        </Accordion.Item>
      </Accordion>
    </div>
  );
};
