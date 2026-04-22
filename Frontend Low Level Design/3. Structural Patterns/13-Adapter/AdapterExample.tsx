import React from 'react';

/**
 * Adapter Pattern
 * 
 * Allows incompatible interfaces to work together.
 * In Frontend, this is extremely common when the API returns data in one format
 * but your UI components expect another.
 */

// 1. INCOMPATIBLE API RESPONSE (The "Adaptee")
interface LegacyUser {
  full_name: string;
  user_id: number;
  contact_info: {
    email_address: string;
  };
}

// 2. TARGET INTERFACE (What our UI wants)
interface UserProfileProps {
  id: string;
  name: string;
  email: string;
}

// 3. ADAPTER (The Bridge)
const userAdapter = (legacy: LegacyUser): UserProfileProps => {
  return {
    id: legacy.user_id.toString(),
    name: legacy.full_name,
    email: legacy.contact_info.email_address
  };
};

// 4. UI COMPONENT
const UserProfile: React.FC<UserProfileProps> = ({ id, name, email }) => (
  <div style={{ border: '1px solid silver', padding: '10px' }}>
    <p>ID: {id}</p>
    <p>Name: {name}</p>
    <p>Email: {email}</p>
  </div>
);

// 5. USAGE
export const AdapterExample = () => {
  const legacyData: LegacyUser = {
    full_name: 'John Doe',
    user_id: 101,
    contact_info: { email_address: 'john@example.com' }
  };

  // We adapt the data before passing it to the component
  const adaptedProps = userAdapter(legacyData);

  return (
    <div>
      <h1>Adapter Pattern</h1>
      <UserProfile {...adaptedProps} />
    </div>
  );
};
