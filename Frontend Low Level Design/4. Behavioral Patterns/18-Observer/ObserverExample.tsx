import React from 'react';

/**
 * Observer Pattern
 * 
 * Defines a one-to-many dependency between objects so that when one object 
 * changes state, all its dependents are notified and updated automatically.
 * This is the basis for event handling and reactivity in modern UI frameworks.
 */

// 1. THE SUBJECT (Observable)
class NewsAgency {
  private observers: ((news: string) => void)[] = [];

  subscribe(observer: (news: string) => void) {
    this.observers.push(observer);
    // Return unsubscribe function
    return () => {
      this.observers = this.observers.filter(o => o !== observer);
    };
  }

  notify(news: string) {
    this.observers.forEach(o => o(news));
  }
}

// Global instance
const globalNewsAgency = new NewsAgency();

// 2. THE OBSERVER (React Component)
const NewsSubscriber = ({ name }: { name: string }) => {
  const [latestNews, setLatestNews] = React.useState('No news yet');

  React.useEffect(() => {
    // Subscribe on mount
    const unsubscribe = globalNewsAgency.subscribe((news) => {
      setLatestNews(news);
    });
    // Unsubscribe on unmount
    return unsubscribe;
  }, []);

  return (
    <div style={{ border: '1px dotted gray', margin: '5px', padding: '5px' }}>
      <strong>{name}:</strong> {latestNews}
    </div>
  );
};

// 3. USAGE
export const ObserverExample = () => {
  const [input, setInput] = React.useState('');

  const broadcastNews = () => {
    globalNewsAgency.notify(input);
    setInput('');
  };

  return (
    <div>
      <h1>Observer Pattern</h1>
      <input 
        value={input} 
        onChange={(e) => setInput(e.target.value)} 
        placeholder="Enter news..." 
      />
      <button onClick={broadcastNews}>Broadcast to Observers</button>
      
      <h3>Subscribers:</h3>
      <NewsSubscriber name="Subscriber A" />
      <NewsSubscriber name="Subscriber B" />
      <NewsSubscriber name="Subscriber C" />
    </div>
  );
};
