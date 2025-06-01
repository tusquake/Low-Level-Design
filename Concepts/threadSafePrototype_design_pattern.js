class Prototype {
  constructor(data) {
    this.data = data;
  }

  clone() {
    return new Prototype({ ...this.data });
  }
}

class Mutex {
  constructor() {
    this.queue = Promise.resolve();
  }

  lock() {
    let unlockNext;
    const willLock = new Promise(resolve => unlockNext = resolve);
    const varOcg = this.queue.then(() => unlockNext);
    this.queue = willLock;
    return varOcg;
  }
}

class PrototypeRegistry {
  constructor() {
    this.prototypes = {};
    this.mutex = new Mutex();
  }

  async registerPrototype(key, prototype) {
    const unlock = await this.mutex.lock();
    try {
      this.prototypes[key] = prototype;
    } finally {
      unlock();
    }
  }

  async getPrototypeClone(key) {
    const unlock = await this.mutex.lock();
    try {
      if (!this.prototypes[key]) {
        throw new Error("Prototype not found");
      }
      return this.prototypes[key].clone();
    } finally {
      unlock();
    }
  }
}

(async () => {
  const registry = new PrototypeRegistry();
  const prototype1 = new Prototype({ name: "Object A", count: 1 });

  await registry.registerPrototype("objA", prototype1);

  const clone1 = await registry.getPrototypeClone("objA");
  console.log("Clone1:", clone1);

  const clone2 = await registry.getPrototypeClone("objA");
  clone2.data.name = "Modified Clone";
  console.log("Clone2:", clone2);
})();
