class Prototype {
  clone() {
    throw new Error("Clone method must be implemented.");
  }
}

class Car extends Prototype {
  constructor(model, engineType) {
    super();
    this.model = model;
    this.engineType = engineType;
  }

  getModel() {
    return this.model;
  }

  getEngineType() {
    return this.engineType;
  }

  clone() {
    return new Car(this.model, this.engineType);
  }

  toString() {
    return `Model: ${this.model}, Engine Type: ${this.engineType}`;
  }
}

const originalCar = new Car("Tesla Model S", "Electric");
const clonedCar = originalCar.clone();

console.log("Original Car:", originalCar.toString());
console.log("Cloned Car:  ", clonedCar.toString());

console.log("Are both cars same object?", originalCar === clonedCar); // false
