import React from 'react';

// ==========================================
// DATA CONTRACTS
// ==========================================
interface User {
  id: string;
  name: string;
  email: string;
  avatarUrl: string;
  preferences: {
    theme: 'light' | 'dark';
    notifications: boolean;
  };
  lastLogin: Date;
}

// ==========================================
// VIOLATION: Prop Junkie (Depends on huge object)
// ==========================================
// This component depends on the whole User object.
// If lastLogin or preferences change, this component might re-render unnecessarily.
// It's also harder to use this component for a "Guest" who doesn't have a full User object.
export const BadAvatar = ({ user }: { user: User }) => {
  return <img src={user.avatarUrl} alt={user.name} />;
};

// ==========================================
// COMPLIANCE: ISP (Segregated Interface)
// ==========================================

// We define an interface that ONLY contains what we need.
interface AvatarProps {
  url: string;
  name: string;
}

// This component is decoupled from the 'User' data structure.
// It can be used for Users, Bots, or anything with a name and image.
export const GoodAvatar = ({ url, name }: AvatarProps) => {
  return <img src={url} alt={name} />;
};

// ==========================================
// USAGE IN PARENT
// ==========================================
export const ProfilePage = ({ user }: { user: User }) => {
  return (
    <div>
      {/* Container (Smart Component) handles the mapping/segregation */}
      <GoodAvatar url={user.avatarUrl} name={user.name} />
      
      {/* We can also reuse it for non-user entities! */}
      <GoodAvatar url="/bot-icon.png" name="System Bot" />
    </div>
  );
};
