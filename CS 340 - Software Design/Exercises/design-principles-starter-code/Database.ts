
// 1. What design principle(s) does this code violate?
	//Depend on abstractions, not concretions. There are static methods, but this class also has a constructor
	//Isolated change principle, the instance stores specific data as well
	//Single responsibility, it both has AND accesses data
// 2. Explain how you would refactor this code to improve its design.
	//If these methods should not be instantiable, they should inherit from an abstract class
	//Don't have an instance of a classs that updates a database AND stores data

export class Course {

	name: string;
	credits: number;

	constructor(name: string, credits: number) {
		this.name = name;
		this.credits = credits;
	}

	static async create(name: string, credits: number): Promise<Course> {

		// ... Code to insert a new Course object into the database ...

	}

	static async find(name: string): Promise<Course | undefined> {

		// ... Code to find a Course object in the database ...

	}

	async update(): Promise<void> {

		// ... Code to update a Course object in the database ...

	}

}
