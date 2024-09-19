
// 1. Explain how this program violates the High-Quality Abstraction principle.
	//The name "Employee" infers more attributes than start and end date
	//The name "RetirementCalculator" does not adequately describe the different functions that it can accomplish
// 2. Explain how you would refactor the code to improve its design.
 //I would change Employee to something more descriptive, like "EmployeeTimeWorked" and change RetirementCalculator to "EmployeeTimeCalculator"

class Employee {
	public employmentStartDate: Date;
	public employmentEndDate: Date;
}

class RetirementCalculator {
	private employee: Employee;

	public constructor(emp: Employee) {
		this.employee = emp;
	}

	public calculateRetirement(payPeriodStart: Date, payPeriodEnd: Date): number { … }

	private getTotalYearsOfService(startDate: Date, endDate: Date): number { … }

	private getMonthsInLastPosition(startDate: Date, endDate: Date): number { … }
	
    ...
}
