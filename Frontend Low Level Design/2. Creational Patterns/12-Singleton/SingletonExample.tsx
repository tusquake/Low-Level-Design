import React from 'react';

/**
 * Singleton Pattern
 * 
 * Ensures a class has only one instance and provides a global point of access to it.
 * In modern JavaScript/React, this is often achieved via ES Modules or React Context,
 * but the classic class-based implementation is still important to understand.
 */

// 1. CLASS-BASED SINGLETON
class ThemeManager {
  private static instance: ThemeManager;
  public theme: 'light' | 'dark' = 'light';

  // Private constructor prevents direct 'new' calls
  private constructor() {}

  public static getInstance(): ThemeManager {
    if (!ThemeManager.instance) {
      ThemeManager.instance = new ThemeManager();
    }
    return ThemeManager.instance;
  }

  public toggleTheme() {
    this.theme = this.theme === 'light' ? 'dark' : 'light';
    console.log('Theme changed to:', this.theme);
  }
}

// 2. MODULE-BASED SINGLETON (More common in modern JS)
// ES Modules are cached, so 'authService' will be the same across all imports.
export const authService = {
  isLoggedIn: false,
  login() { this.isLoggedIn = true; },
  logout() { this.isLoggedIn = false; }
};

// 3. USAGE IN REACT
export const SingletonExample = () => {
  const manager = ThemeManager.getInstance();

  const handleToggle = () => {
    manager.toggleTheme();
    // Force a re-render if needed (Singleton state isn't reactive by default)
    alert(`Current Theme in Singleton: ${manager.theme}`);
  };

  return (
    <div>
      <h1>Singleton Pattern</h1>
      <p>Theme Manager instance is shared globally.</p>
      <button onClick={handleToggle}>Toggle Singleton Theme</button>
      <p>Auth Service Logged In: {String(authService.isLoggedIn)}</p>
    </div>
  );
};
