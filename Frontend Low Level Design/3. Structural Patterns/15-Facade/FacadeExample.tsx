import React from 'react';

/**
 * Facade Pattern
 * 
 * Provides a simplified interface to a complex subsystem.
 * In Frontend, this is most commonly used to hide complex API calls,
 * library configurations, or state management logic.
 */

// --- COMPLEX SUBSYSTEM (Direct interaction is messy) ---
const complexNetworkLib = {
  initialize: () => console.log('Init Network...'),
  setHeaders: (h: any) => console.log('Set Headers:', h),
  setRetry: (n: number) => console.log('Retry count:', n),
  get: (url: string) => Promise.resolve({ data: `Data from ${url}` }),
};

// --- FACADE (Simplified Interface) ---
const apiFacade = {
  async getUserData(userId: string) {
    // Hide all the setup complexity from the UI
    complexNetworkLib.initialize();
    complexNetworkLib.setHeaders({ Authorization: 'Bearer token' });
    complexNetworkLib.setRetry(3);
    
    const response = await complexNetworkLib.get(`/users/${userId}`);
    return response.data;
  }
};

// --- USAGE IN REACT ---
export const FacadeExample = () => {
  const [data, setData] = React.useState<string>('');

  const loadData = async () => {
    // The component only sees a simple, clean method
    const result = await apiFacade.getUserData('123');
    setData(result);
  };

  return (
    <div>
      <h1>Facade Pattern</h1>
      <button onClick={loadData}>Load User Data via Facade</button>
      {data && <p>Result: {data}</p>}
    </div>
  );
};
