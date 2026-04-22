import React, { useState, useEffect } from 'react';

// ==========================================
// VIOLATION: God Component (Does everything)
// ==========================================
export const BadUserList = () => {
  const [users, setUsers] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('https://jsonplaceholder.typicode.com/users')
      .then((res) => res.json())
      .then((data) => {
        setUsers(data);
        setLoading(false);
      });
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <ul>
      {users.map((user) => (
        <li key={user.id}>
          {user.name.toUpperCase()} - {user.email.toLowerCase()}
        </li>
      ))}
    </ul>
  );
};

// ==========================================
// COMPLIANCE: SRP (Separated Responsibilities)
// ==========================================

// 1. Data Fetching Responsibility (Hook)
const useUsers = () => {
  const [users, setUsers] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('https://jsonplaceholder.typicode.com/users')
      .then((res) => res.json())
      .then((data) => {
        setUsers(data);
        setLoading(false);
      });
  }, []);

  return { users, loading };
};

// 2. Logic/Formatting Responsibility (Utility)
const formatUser = (user: any) => ({
  name: user.name.toUpperCase(),
  email: user.email.toLowerCase(),
});

// 3. UI/Rendering Responsibility (Component)
export const GoodUserList = () => {
  const { users, loading } = useUsers();

  if (loading) return <div>Loading...</div>;

  return (
    <ul>
      {users.map((user) => {
        const formatted = formatUser(user);
        return (
          <li key={user.id}>
            {formatted.name} - {formatted.email}
          </li>
        );
      })}
    </ul>
  );
};
