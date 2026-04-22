import React from 'react';

/**
 * Chain of Responsibility Pattern
 * 
 * Avoids coupling the sender of a request to its receiver by giving more 
 * than one object a chance to handle the request.
 */

// 1. HANDLER INTERFACE
abstract class ValidationHandler {
  protected nextHandler?: ValidationHandler;

  setNext(handler: ValidationHandler): ValidationHandler {
    this.nextHandler = handler;
    return handler;
  }

  abstract handle(value: string): string | null;
}

// 2. CONCRETE HANDLERS
class RequiredHandler extends ValidationHandler {
  handle(value: string) {
    if (!value) return 'Value is required';
    return this.nextHandler ? this.nextHandler.handle(value) : null;
  }
}

class EmailHandler extends ValidationHandler {
  handle(value: string) {
    if (!value.includes('@')) return 'Invalid email format';
    return this.nextHandler ? this.nextHandler.handle(value) : null;
  }
}

class MinLengthHandler extends ValidationHandler {
  constructor(private min: number) { super(); }
  handle(value: string) {
    if (value.length < this.min) return `Minimum length is ${this.min}`;
    return this.nextHandler ? this.nextHandler.handle(value) : null;
  }
}

// 3. USAGE IN REACT
export const ChainOfResponsibilityExample = () => {
  const [email, setEmail] = React.useState('');
  const [error, setError] = React.useState<string | null>(null);

  const validate = () => {
    // Setup the chain
    const required = new RequiredHandler();
    const isEmail = new EmailHandler();
    const minLength = new MinLengthHandler(5);

    required.setNext(isEmail).setNext(minLength);

    // Start the request through the chain
    const result = required.handle(email);
    setError(result);
    if (!result) alert('Validation Passed!');
  };

  return (
    <div>
      <h1>Chain of Responsibility</h1>
      <p>The request (email string) passes through multiple handlers until one fails or all pass.</p>
      
      <input 
        value={email} 
        onChange={(e) => setEmail(e.target.value)} 
        placeholder="Enter email..." 
      />
      <button onClick={validate}>Validate Email</button>
      
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}
    </div>
  );
};
