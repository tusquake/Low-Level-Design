import React from 'react';

/**
 * Chain of Responsibility + Strategy Pattern Combination
 * 
 * Chain: Decides "WHO" should handle the request.
 * Strategy: Decides "HOW" to handle the request.
 */

// 1. STRATEGIES (The "How")
interface ImageProcessor {
  process: (file: string) => string;
}

const PngProcessor: ImageProcessor = { process: (f) => `Processing PNG: ${f}` };
const JpgProcessor: ImageProcessor = { process: (f) => `Processing JPG: ${f}` };

// 2. CHAIN (The "Who")
abstract class FileHandler {
  protected next?: FileHandler;
  setNext(h: FileHandler) { this.next = h; return h; }
  
  abstract handle(fileName: string): string;
}

class PngHandler extends FileHandler {
  handle(fileName: string): string {
    if (fileName.endsWith('.png')) return PngProcessor.process(fileName);
    return this.next ? this.next.handle(fileName) : 'Unsupported format';
  }
}

class JpgHandler extends FileHandler {
  handle(fileName: string): string {
    if (fileName.endsWith('.jpg')) return JpgProcessor.process(fileName);
    return this.next ? this.next.handle(fileName) : 'Unsupported format';
  }
}

// 3. USAGE IN REACT
export const ChainStrategyExample = () => {
  const [file, setFile] = React.useState('');
  const [result, setResult] = React.useState('');

  const handleUpload = () => {
    // Setup the chain
    const chain = new PngHandler();
    chain.setNext(new JpgHandler());

    const msg = chain.handle(file);
    setResult(msg);
  };

  return (
    <div>
      <h1>Chain + Strategy Pattern</h1>
      <p>The chain finds the right handler, and the handler uses a strategy to process.</p>
      
      <input 
        value={file} 
        onChange={(e) => setFile(e.target.value)} 
        placeholder="test.png or image.jpg" 
      />
      <button onClick={handleUpload}>Process File</button>
      {result && <p>Result: {result}</p>}
    </div>
  );
};
