// singleton.js
// __define-ocg__ JavaScript Singleton (Browser Safe)
class Singleton {
  constructor() {
    if (Singleton._instance) {
      return Singleton._instance;
    }

    this.message = "I am the only instance!";
    Singleton._instance = this;
  }

  logMessage() {
    console.log(this.message);
  }
}

// Expose globally (for browser testing)
window.Singleton = Singleton;

// Example usage
const obj1 = new Singleton();
const obj2 = new Singleton();

console.log(obj1 === obj2); // true
obj1.logMessage();          // "I am the only instance!"
