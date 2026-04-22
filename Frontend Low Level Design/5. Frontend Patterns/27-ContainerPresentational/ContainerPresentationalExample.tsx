import React from 'react';

/**
 * Container / Presentational Pattern
 * 
 * Separates "Logic" (how things work) from "Presentation" (how things look).
 * Container: Fetches data, manages state, handles business logic.
 * Presentational: Receives props, renders UI, is usually a pure function.
 */

// 1. PRESENTATIONAL COMPONENT (Pure UI)
interface UserListProps {
  users: string[];
  loading: boolean;
}

const UserListUI: React.FC<UserListProps> = ({ users, loading }) => {
  if (loading) return <div>Loading...</div>;
  if (users.length === 0) return <div>No users found.</div>;

  return (
    <ul>
      {users.map((u, i) => <li key={i}>{u}</li>)}
    </ul>
  );
};

// 2. CONTAINER COMPONENT (Logic & Data)
export const UserListContainer = () => {
  const [users, setUsers] = React.useState<string[]>([]);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    // Simulate API fetch
    setTimeout(() => {
      setUsers(['Alice', 'Bob', 'Charlie']);
      setLoading(false);
    }, 1000);
  }, []);

  // The Container renders the Presentational component
  return (
    <div>
      <h1>Container / Presentational Pattern</h1>
      <UserListUI users={users} loading={loading} />
    </div>
  );
};
