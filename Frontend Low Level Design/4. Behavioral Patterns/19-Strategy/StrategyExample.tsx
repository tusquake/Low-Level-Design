import React from 'react';

/**
 * Strategy Pattern
 * 
 * Defines a family of algorithms, encapsulates each one, and makes them interchangeable.
 * Strategy lets the algorithm vary independently from clients that use it.
 */

// 1. STRATEGY INTERFACE
interface PaymentStrategy {
  pay: (amount: number) => string;
}

// 2. CONCRETE STRATEGIES
const CreditCardStrategy: PaymentStrategy = {
  pay: (amount) => `Paid $${amount} using Credit Card.`
};

const PayPalStrategy: PaymentStrategy = {
  pay: (amount) => `Paid $${amount} using PayPal.`
};

const BitcoinStrategy: PaymentStrategy = {
  pay: (amount) => `Paid $${amount} using Bitcoin.`
};

// 3. CONTEXT (The UI that uses a strategy)
const Checkout = ({ amount, strategy }: { amount: number, strategy: PaymentStrategy }) => {
  const [message, setMessage] = React.useState('');

  const handlePayment = () => {
    const result = strategy.pay(amount);
    setMessage(result);
  };

  return (
    <div style={{ border: '1px solid gray', padding: '10px', marginTop: '10px' }}>
      <p>Total: ${amount}</p>
      <button onClick={handlePayment}>Pay Now</button>
      {message && <p style={{ color: 'green' }}>{message}</p>}
    </div>
  );
};

// 4. USAGE
export const StrategyExample = () => {
  const [method, setMethod] = React.useState<'cc' | 'pp' | 'bc'>('cc');

  const strategies = {
    cc: CreditCardStrategy,
    pp: PayPalStrategy,
    bc: BitcoinStrategy
  };

  return (
    <div>
      <h1>Strategy Pattern</h1>
      <label>Select Payment Method: </label>
      <select onChange={(e) => setMethod(e.target.value as any)}>
        <option value="cc">Credit Card</option>
        <option value="pp">PayPal</option>
        <option value="bc">Bitcoin</option>
      </select>

      <Checkout amount={100} strategy={strategies[method]} />
    </div>
  );
};
