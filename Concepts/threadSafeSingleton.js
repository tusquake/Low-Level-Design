// threadSafeSingleton.js
// __define-ocg__ Thread-Safe Singleton (Node.js)
const { Mutex } = require("async-mutex");

class SafeSingleton {
  constructor() {
    this.message = "Thread-safe Singleton instance!";
  }

  static async getInstance() {
    if (!SafeSingleton._mutex) {
      SafeSingleton._mutex = new Mutex();
    }

    const release = await SafeSingleton._mutex.acquire();
    try {
      if (!SafeSingleton._instance) {
        SafeSingleton._instance = new SafeSingleton();
      }
      return SafeSingleton._instance;
    } finally {
      release();
    }
  }

  logMessage() {
    console.log(this.message);
  }
}

// Example usage
(async () => {
  const inst1 = await SafeSingleton.getInstance();
  const inst2 = await SafeSingleton.getInstance();

  console.log(inst1 === inst2); // true
  inst1.logMessage();           // "Thread-safe Singleton instance!"
})();

module.exports = SafeSingleton;
