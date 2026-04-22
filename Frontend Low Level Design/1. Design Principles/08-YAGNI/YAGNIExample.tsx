import React from 'react';

/**
 * YAGNI (You Ain't Gonna Need It)
 * 
 * Don't implement features or abstractions until they are actually needed.
 * Predictive coding leads to wasted time and unnecessary complexity.
 */

// --- VIOLATION (Building for a hypothetical future) ---
/*
interface BaseUserProps {
  id: string;
  name: string;
  // Adding fields we MIGHT need in 6 months...
  bio?: string;
  preferences?: string[];
  socialLinks?: Record<string, string>;
}

const UserCard = ({ id, name, bio, preferences, socialLinks }: BaseUserProps) => {
  // Complex logic to handle data we don't even fetch yet
  return (
    <div>
      <h3>{name}</h3>
      {socialLinks && <div className="social">...</div>}
    </div>
  );
};
*/

// --- COMPLIANCE (Building only what is requested) ---

interface UserCardProps {
  name: string;
}

const UserCard: React.FC<UserCardProps> = ({ name }) => {
  // Only handle what is actually being used today
  return (
    <div className="card">
      <h3>{name}</h3>
    </div>
  );
};

export const YAGNIExample = () => {
  return (
    <div>
      <h1>YAGNI: You Ain't Gonna Need It</h1>
      <p>Building only the features requested right now.</p>
      <UserCard name="Tushar Seth" />
    </div>
  );
};
