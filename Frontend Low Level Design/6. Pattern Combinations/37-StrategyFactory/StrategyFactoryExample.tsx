import React from 'react';

/**
 * Strategy + Factory Pattern Combination
 * 
 * Factory: Creates the strategy object based on a type.
 * Strategy: Encapsulates the specific algorithm.
 */

// 1. STRATEGIES
interface ExportStrategy {
  export: (data: any[]) => string;
}

const JsonExport: ExportStrategy = {
  export: (data) => JSON.stringify(data, null, 2)
};

const CsvExport: ExportStrategy = {
  export: (data) => data.map(row => Object.values(row).join(',')).join('\n')
};

// 2. FACTORY
const ExportFactory = (type: 'json' | 'csv'): ExportStrategy => {
  if (type === 'json') return JsonExport;
  return CsvExport;
};

// 3. USAGE IN REACT
export const StrategyFactoryExample = () => {
  const [data] = React.useState([{ id: 1, name: 'Alice' }, { id: 2, name: 'Bob' }]);
  const [format, setFormat] = React.useState<'json' | 'csv'>('json');
  const [output, setOutput] = React.useState('');

  const handleExport = () => {
    // 1. Use Factory to get Strategy
    const strategy = ExportFactory(format);
    // 2. Use Strategy to perform work
    const result = strategy.export(data);
    setOutput(result);
  };

  return (
    <div>
      <h1>Strategy + Factory Pattern</h1>
      <select onChange={(e) => setFormat(e.target.value as any)}>
        <option value="json">JSON Format</option>
        <option value="csv">CSV Format</option>
      </select>
      <button onClick={handleExport}>Export Data</button>
      
      <pre style={{ background: '#eee', padding: '10px', marginTop: '10px' }}>
        {output || 'No export yet'}
      </pre>
    </div>
  );
};
