import React, { useEffect, useState } from 'react';

/**
 * DIP (Dependency Inversion Principle)
 * 
 * High-level components should not depend on low-level implementation details (like fetch/axios).
 * Instead, they should depend on abstractions (interfaces/functions passed as props or context).
 */

// 1. ABSTRACTION (Interface for Data Fetching)
interface DataService<T> {
  fetchData: () => Promise<T[]>;
}

// 2. LOW-LEVEL IMPLEMENTATION (Concrete Fetch implementation)
const apiService: DataService<{ id: number; name: string }> = {
  fetchData: async () => {
    const response = await fetch('https://jsonplaceholder.typicode.com/users');
    return response.json();
  }
};

// 3. HIGH-LEVEL COMPONENT (Depends on Abstraction via Props)
interface UserListProps {
  service: DataService<{ id: number; name: string }>;
}

const UserList: React.FC<UserListProps> = ({ service }) => {
  const [users, setUsers] = useState<{ id: number; name: string }[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    service.fetchData()
      .then(setUsers)
      .finally(() => setLoading(false));
  }, [service]);

  if (loading) return <div>Loading...</div>;

  return (
    <ul>
      {users.map(user => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
};

// 4. USAGE (Inverting Dependency)
// We can swap 'apiService' with a 'mockService' without changing UserList.
export const DIPExample = () => {
  return (
    <div>
      <h1>DIP: Dependency Inversion</h1>
      <UserList service={apiService} />
    </div>
  );
};
