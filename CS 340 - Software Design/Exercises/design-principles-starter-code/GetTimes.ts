// 1. What is the biggest design principle violation in the code below.
	//DUPLICATION, WHY HAVE YOU DONE THIS
// 2. Refactor the code to improve its design.
	//OKAY

type Dictionary = {
	[index: string]: string
}

type Times = {
	interval: number;
	duration: number;
	departure: number;
};

function getTimes(props: Dictionary): Times {

	let valueString: string;
	let value: number;

	valueString = props["interval"];
	throwErrorOnFalseNumber(valueString);
	value = parseInt(valueString);
	throwErrorOnZeroValue(value);

	let interval = value;

	valueString = props["duration"];
	throwErrorOnFalseNumber(valueString);

	value = parseInt(valueString);
	throwErrorOnZeroValue(value);

	throwErrorOnNonZeroValue((value % interval))

	let duration = value;

	valueString = props["departure"];
	throwErrorOnFalseNumber(valueString);

	value = parseInt(valueString);
	throwErrorOnZeroValue(value);

	throwErrorOnNonZeroValue((value % interval));
	let departure = value;

	return { interval, duration, departure };
}

function throwErrorOnZeroValue(value){

	if(value == 0){
		throw new Error("Value cannot be 0");
	}
}

function throwErrorOnNonZeroValue(value){

	if(value != 0){
		throw new Error("Value cannot be 0");
	}
}

function throwErrorOnFalseNumber(number){

	if(!number){
		throw new Error("Number not found");
	}
}
