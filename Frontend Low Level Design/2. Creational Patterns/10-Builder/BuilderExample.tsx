import React from 'react';

/**
 * Builder Pattern
 * 
 * Separates the construction of a complex object from its representation.
 * Allows step-by-step construction. In Frontend, this is useful for complex
 * configurations or multi-step form data.
 */

// 1. DATA MODEL
interface Notification {
  title: string;
  message: string;
  type: 'info' | 'success' | 'error';
  duration: number;
  onClose?: () => void;
}

// 2. BUILDER CLASS
class NotificationBuilder {
  private notification: Notification;

  constructor() {
    // Default values
    this.notification = {
      title: 'Default Title',
      message: '',
      type: 'info',
      duration: 3000,
    };
  }

  setTitle(title: string) {
    this.notification.title = title;
    return this; // Return 'this' for chaining
  }

  setMessage(message: string) {
    this.notification.message = message;
    return this;
  }

  setType(type: 'info' | 'success' | 'error') {
    this.notification.type = type;
    return this;
  }

  setDuration(ms: number) {
    this.notification.duration = ms;
    return this;
  }

  build(): Notification {
    return this.notification;
  }
}

// 3. USAGE IN REACT
export const BuilderExample = () => {
  const showNotification = () => {
    // Fluent API for building complex objects
    const myNote = new NotificationBuilder()
      .setTitle('Success!')
      .setMessage('Operation completed successfully.')
      .setType('success')
      .setDuration(5000)
      .build();

    console.log('Constructed Notification:', myNote);
    alert(`Notification Created: ${myNote.title} - ${myNote.message}`);
  };

  return (
    <div>
      <h1>Builder Pattern</h1>
      <button onClick={showNotification}>Build and Log Notification</button>
    </div>
  );
};
