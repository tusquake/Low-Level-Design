import React from 'react';

/**
 * Module Federation (Conceptual Example)
 * 
 * Allows a JavaScript application to dynamically load code from another application.
 * This is the foundation of Micro-frontends.
 */

// 1. CONCEPTUAL REMOTE LOADING
// In a real app, this would be handled by Webpack/Vite config and 'import()'
const remoteLoader = async (appName: string, moduleName: string) => {
  console.log(`[FEDERATION] Loading ${moduleName} from ${appName}...`);
  // Simulate network delay
  await new Promise(r => setTimeout(r, 1000));
  
  // Return a mock component
  return () => (
    <div style={{ border: '2px dashed orange', padding: '10px' }}>
      <h3>Remote Component from {appName}</h3>
      <p>I am a Micro-frontend!</p>
    </div>
  );
};

// 2. USAGE IN REACT (Simulated with Lazy/Suspense logic)
export const ModuleFederationExample = () => {
  const [RemoteComponent, setRemoteComponent] = React.useState<React.FC | null>(null);

  const handleLoad = async () => {
    const Comp = await remoteLoader('BillingApp', 'InvoiceTable');
    setRemoteComponent(() => Comp);
  };

  return (
    <div>
      <h1>Module Federation Pattern</h1>
      <p>Decouple your application into independent micro-apps that share code at runtime.</p>
      
      <button onClick={handleLoad}>Load Remote Micro-frontend</button>
      
      <div style={{ marginTop: '20px' }}>
        {RemoteComponent ? <RemoteComponent /> : <p>No remote loaded yet.</p>}
      </div>
    </div>
  );
};
