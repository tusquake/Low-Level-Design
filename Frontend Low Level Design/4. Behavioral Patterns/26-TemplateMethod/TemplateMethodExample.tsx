import React from 'react';

/**
 * Template Method Pattern
 * 
 * Defines the skeleton of an algorithm in a method, 
 * deferring some steps to subclasses.
 */

// 1. ABSTRACT CLASS (The Template)
abstract class DataFetcher {
  // The Template Method
  public async execute() {
    this.log('Starting fetch...');
    const data = await this.fetchData();
    const processed = this.processData(data);
    this.log('Fetch complete.');
    return processed;
  }

  // Hook for subclasses to override
  protected abstract fetchData(): Promise<any>;

  // Default implementation that can be overridden
  protected processData(data: any) {
    return data;
  }

  private log(msg: string) {
    console.log(`[TEMPLATE LOG]: ${msg}`);
  }
}

// 2. CONCRETE IMPLEMENTATIONS
class UserFetcher extends DataFetcher {
  protected async fetchData() {
    return { name: 'Alice', type: 'admin' };
  }
  protected processData(data: any) {
    return { ...data, processedAt: new Date().toLocaleTimeString() };
  }
}

class ProductFetcher extends DataFetcher {
  protected async fetchData() {
    return { id: 1, title: 'Laptop' };
  }
  // Uses default processData
}

// 3. USAGE IN REACT
export const TemplateMethodExample = () => {
  const [result, setResult] = React.useState<any>(null);

  const loadUser = async () => {
    const fetcher = new UserFetcher();
    const data = await fetcher.execute();
    setResult(data);
  };

  const loadProduct = async () => {
    const fetcher = new ProductFetcher();
    const data = await fetcher.execute();
    setResult(data);
  };

  return (
    <div>
      <h1>Template Method Pattern</h1>
      <p>The sequence (log -> fetch -> process -> log) is defined in the base class.</p>
      
      <button onClick={loadUser}>Load User (Custom Processing)</button>
      <button onClick={loadProduct}>Load Product (Default Processing)</button>
      
      {result && (
        <pre style={{ background: '#f4f4f4', padding: '10px' }}>
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
};
