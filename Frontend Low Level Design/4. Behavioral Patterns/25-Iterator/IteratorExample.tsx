import React from 'react';

/**
 * Iterator Pattern
 * 
 * Provides a way to access the elements of an aggregate object 
 * sequentially without exposing its underlying representation.
 * In Modern JS, this is implemented via Symbol.iterator.
 */

// 1. CUSTOM COLLECTION
class UserCollection {
  private users: string[] = [];

  add(user: string) { this.users.push(user); }

  // Implementing the Iterator Protocol
  [Symbol.iterator]() {
    let index = 0;
    const users = this.users;

    return {
      next: () => {
        if (index < users.length) {
          return { value: users[index++], done: false };
        } else {
          return { value: null, done: true };
        }
      }
    };
  }
}

// 2. USAGE IN REACT
export const IteratorExample = () => {
  const [userList, setUserList] = React.useState<string[]>([]);
  
  const processCollection = () => {
    const myUsers = new UserCollection();
    myUsers.add('Alice');
    myUsers.add('Bob');
    myUsers.add('Charlie');

    // Because it implements Symbol.iterator, we can use for...of
    const results: string[] = [];
    for (const user of myUsers) {
      results.push(`Processed: ${user}`);
    }
    setUserList(results);
  };

  return (
    <div>
      <h1>Iterator Pattern</h1>
      <p>Using the native JS Iterator protocol to traverse a custom collection.</p>
      
      <button onClick={processCollection}>Process Collection</button>
      
      <ul>
        {userList.map((u, i) => <li key={i}>{u}</li>)}
      </ul>
    </div>
  );
};
