import React from 'react';

/**
 * DRY (Don't Repeat Yourself)
 * 
 * Avoid duplication of logic or UI structures.
 * Instead, centralize common logic into hooks or utility functions.
 */

// --- VIOLATION (Repeating Logic) ---
/*
const LoginButton = () => {
  const handleClick = () => console.log('Action tracked: LOGIN');
  return <button onClick={handleClick}>Login</button>;
};

const SignupButton = () => {
  const handleClick = () => console.log('Action tracked: SIGNUP');
  return <button onClick={handleClick}>Signup</button>;
};
*/

// --- COMPLIANCE (Centralizing Logic) ---

// 1. Centralized Utility / Hook
const useTracking = () => {
  const trackAction = (actionName: string) => {
    // In a real app, this would send data to Analytics
    console.log(`[Analytics] Action tracked: ${actionName}`);
  };
  return { trackAction };
};

// 2. Generic Reusable Component
interface TrackedButtonProps {
  actionName: string;
  label: string;
  onClick?: () => void;
}

const TrackedButton: React.FC<TrackedButtonProps> = ({ actionName, label, onClick }) => {
  const { trackAction } = useTracking();

  const handleClick = () => {
    trackAction(actionName);
    if (onClick) onClick();
  };

  return <button onClick={handleClick}>{label}</button>;
};

export const DRYExample = () => {
  return (
    <div>
      <h1>DRY: Don't Repeat Yourself</h1>
      <TrackedButton actionName="LOGIN" label="Login" />
      <TrackedButton actionName="SIGNUP" label="Signup" />
    </div>
  );
};
