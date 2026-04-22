import React from 'react';

/**
 * Composite Pattern
 * 
 * Allows you to treat individual objects and compositions of objects uniformly.
 * This is the fundamental pattern of the React Component Tree.
 */

// 1. THE "COMPONENT" INTERFACE
interface FileSystemItem {
  name: string;
  size: number;
}

// 2. LEAF (Individual Object)
const File: React.FC<FileSystemItem> = ({ name, size }) => (
  <div style={{ marginLeft: '20px' }}>📄 {name} ({size}kb)</div>
);

// 3. COMPOSITE (Collection of objects, including other composites)
interface FolderProps extends FileSystemItem {
  children: React.ReactNode;
}

const Folder: React.FC<FolderProps> = ({ name, children }) => {
  const [isOpen, setIsOpen] = React.useState(true);

  return (
    <div style={{ marginLeft: '20px' }}>
      <div 
        onClick={() => setIsOpen(!isOpen)} 
        style={{ cursor: 'pointer', fontWeight: 'bold' }}
      >
        {isOpen ? '📂' : '📁'} {name}
      </div>
      {isOpen && <div>{children}</div>}
    </div>
  );
};

// 4. USAGE (Uniform treatment of files and folders)
export const CompositeExample = () => {
  return (
    <div>
      <h1>Composite Pattern</h1>
      <p>A recursive tree structure where folders can contain files OR other folders.</p>
      
      <Folder name="Project Root" size={1000}>
        <Folder name="src" size={500}>
          <File name="App.tsx" size={10} />
          <File name="index.css" size={5} />
          <Folder name="components" size={200}>
            <File name="Button.tsx" size={2} />
          </Folder>
        </Folder>
        <File name="package.json" size={1} />
      </Folder>
    </div>
  );
};
