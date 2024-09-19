// 1. What design principles does this code violate?
	//Simplicity. Tough to look at and understand
// 2. Refactor the code to improve its design.
	//Done

function isLowRiskClient(score: number, income: number, authorized: boolean): boolean {
	isHighScore: boolean = score > 700;
	isDecentScore: boolean = score > 500;
	isHighIncome: boolean = income > 100000;
	isMiddleIncome: boolean = (40000 <= income) && (income <= 100000);
	
	if (isHighScore 
			|| (isMiddleIncome && authorized && isDecentScore) 
			|| isHighIncome) {

		return true;
	}
	else {
		return false;
	}
}
