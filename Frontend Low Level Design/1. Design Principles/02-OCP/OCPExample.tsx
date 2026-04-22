import React from 'react';

// ==========================================
// VIOLATION: Hardcoded variants (Closed for extension)
// ==========================================
export const BadButton = ({ type, text }: { type: 'primary' | 'secondary'; text: string }) => {
  // Fix: Explicitly typing the styles object to avoid index errors
  const styles: Record<'primary' | 'secondary', React.CSSProperties> = {
    primary: { backgroundColor: 'blue', color: 'white' },
    secondary: { backgroundColor: 'grey', color: 'black' },
  };

  return <button style={styles[type]}>{text}</button>;
};

// ==========================================
// COMPLIANCE: OCP (Open for extension)
// ==========================================

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
  customStyle?: React.CSSProperties;
}

export const GoodButton = ({ children, customStyle, style, ...props }: ButtonProps) => {
  const baseStyle: React.CSSProperties = {
    padding: '10px 20px',
    borderRadius: '4px',
    border: 'none',
    cursor: 'pointer',
    ...baseStyles, // Internal defaults
    ...customStyle, // Custom extension prop
    ...style,       // Standard React style prop (Fix: Merging instead of overwriting)
  };

  return (
    <button style={baseStyle} {...props}>
      {children}
    </button>
  );
};

const baseStyles: React.CSSProperties = {
  fontSize: '16px',
};

// ==========================================
// EXTENSIONS: Fixed to ensure base styles are preserved
// ==========================================

export const PrimaryButton = ({ customStyle, ...props }: any) => (
  <GoodButton 
    customStyle={{ backgroundColor: 'blue', color: 'white', ...customStyle }} 
    {...props} 
  />
);
