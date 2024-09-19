export abstract class Animal<T> {
  name: string;
  trainingPriority: number;

  constructor(name: string, trainingPriority: number) {
    this.trainingPriority = trainingPriority;
    this.name = name;
  }

  static getAnimalSorted<T extends Animal<T>>(animalList: T[]) {
    return animalList.sort((animal1, animal2) =>
    animal1.trainingPriority < animal2.trainingPriority ? -1 : 1
    );
  }
  
  static getAnimalsTrainingPriorityList<T extends Animal<T>>(animalList: T[]): string {
    return animalList
      .map(
        (animal) =>
          animal.name + "'s training priority: " + animal.trainingPriority + "\n"
      )
      .join("");
  }
}


