import React from 'react';

/**
 * Islands Architecture (Conceptual Simulation)
 * 
 * Encourages small, self-contained pockets of interactivity (islands) 
 * within an otherwise static HTML document.
 * This minimizes the amount of JavaScript sent to the client.
 */

// 1. STATIC CONTENT (Zero JS in production)
const StaticHeader = () => (
  <header style={{ background: '#333', color: '#fff', padding: '10px' }}>
    <h1>My Static Blog (Server Rendered)</h1>
  </header>
);

const StaticFooter = () => (
  <footer style={{ marginTop: '20px', borderTop: '1px solid #ccc' }}>
    <p>© 2026 Static Site Generator</p>
  </footer>
);

// 2. INTERACTIVE ISLANDS (Hydrated with JS)
const NewsletterIsland = () => {
  const [email, setEmail] = React.useState('');
  return (
    <div style={{ border: '2px solid blue', padding: '15px', margin: '10px 0' }}>
      <h4>Island 1: Newsletter (Interactive)</h4>
      <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email..." />
      <button onClick={() => alert('Subscribed!')}>Join</button>
    </div>
  );
};

const LikeButtonIsland = () => {
  const [likes, setLikes] = React.useState(0);
  return (
    <button onClick={() => setLikes(likes + 1)} style={{ background: 'pink' }}>
      Island 2: Like ({likes})
    </button>
  );
};

// 3. USAGE
export const IslandsArchitectureExample = () => {
  return (
    <div>
      <h1>Islands Architecture</h1>
      <StaticHeader />
      
      <main>
        <p>This paragraph is static text. It needs no JavaScript to be visible or accessible.</p>
        
        {/* These are the 'Islands' */}
        <NewsletterIsland />
        
        <p>More static content between interactive areas.</p>
        
        <LikeButtonIsland />
      </main>

      <StaticFooter />
    </div>
  );
};
