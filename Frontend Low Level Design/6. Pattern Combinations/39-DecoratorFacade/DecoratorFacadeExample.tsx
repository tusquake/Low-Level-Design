import React from 'react';

/**
 * Decorator + Facade Pattern Combination
 * 
 * Facade: Provides a simple interface to a complex system.
 * Decorator: Adds extra functionality (like logging or caching) 
 * to the facade without changing its interface.
 */

// 1. THE COMPLEX SYSTEM
const RawApi = {
  fetchUser: async (id: string) => ({ id, name: 'User ' + id })
};

// 2. THE FACADE (Simple interface)
const UserFacade = {
  get: (id: string) => RawApi.fetchUser(id)
};

// 3. THE DECORATOR (Adds logging to the Facade)
const withLogging = (facade: typeof UserFacade) => {
  return {
    ...facade,
    get: async (id: string) => {
      console.log(`[FACADE LOG] Fetching user: ${id}`);
      const start = Date.now();
      const result = await facade.get(id);
      console.log(`[FACADE LOG] Took ${Date.now() - start}ms`);
      return result;
    }
  };
};

const EnhancedFacade = withLogging(UserFacade);

// 4. USAGE IN REACT
export const DecoratorFacadeExample = () => {
  const [user, setUser] = React.useState<any>(null);

  const handleLoad = async () => {
    // Component uses the simple, enhanced interface
    const data = await EnhancedFacade.get('123');
    setUser(data);
  };

  return (
    <div>
      <h1>Decorator + Facade Pattern</h1>
      <p>A simple API facade wrapped with a logging decorator.</p>
      
      <button onClick={handleLoad}>Load User via Enhanced Facade</button>
      {user && <p>Loaded: {user.name}</p>}
      <p>Check console for timing and fetch logs.</p>
    </div>
  );
};
