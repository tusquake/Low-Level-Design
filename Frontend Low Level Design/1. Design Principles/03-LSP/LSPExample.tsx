import React from 'react';

// ==========================================
// THE CONTRACT: A Search Input
// ==========================================
interface SearchInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  onSearch: (value: string) => void;
}

const SearchInput = ({ onSearch, ...props }: SearchInputProps) => {
  return (
    <input
      type="text"
      onChange={(e) => onSearch(e.target.value)}
      {...props} // Spreading props ensures we honor the HTML Input contract
    />
  );
};

// ==========================================
// COMPLIANCE: Specialized Search Input
// ==========================================
// This component adds behavior (an icon) but honors the original contract.
// You can swap SearchInput with IconSearchInput anywhere.
export const IconSearchInput = ({ onSearch, ...props }: SearchInputProps) => {
  return (
    <div className="search-wrapper">
      <span>🔍</span>
      <SearchInput onSearch={onSearch} {...props} />
    </div>
  );
};

// ==========================================
// VIOLATION: Secure Search Input
// ==========================================
// This component violates LSP because it CHANGES the behavior of onSearch
// to return an encrypted hash instead of a string.
// Anyone using this as a "SearchInput" will get a hash and their code will break.
export const BadSecureSearchInput = ({ onSearch, ...props }: SearchInputProps) => {
  const encrypt = (val: string) => `hash_${val}`;

  return (
    <input
      {...props}
      onChange={(e) => {
        // BREAKS CONTRACT: Returns hash instead of raw string
        onSearch(encrypt(e.target.value));
      }}
    />
  );
};

// ==========================================
// DEMO OF THE PROBLEM
// ==========================================
export const ParentApp = () => {
  const handleSearch = (query: string) => {
    // This logic expects a raw string to perform filtering
    console.log(`Filtering list by: ${query.toLowerCase()}`);
  };

  return (
    <div>
      {/* Works fine */}
      <IconSearchInput onSearch={handleSearch} placeholder="Search names..." />

      {/* CRASHES or FAILS: .toLowerCase() on a hash or unexpected format */}
      <BadSecureSearchInput onSearch={handleSearch} placeholder="Secure search..." />
    </div>
  );
};
