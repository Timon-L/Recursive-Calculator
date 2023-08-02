package com.fdmgroup.tdd.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Calculator implements ICalculator{
	private final String symbolRegex = "[^a-zA-Z()0-9\\.]+";
	private final String numRegex = "[()0-9\\.]+";
	private final String spaceRegex = "\\s";
	private final double precision = 0.0000000001;
	private List<Integer> leftBrackets = new ArrayList<Integer>();
	private boolean bracketsListSet = false;
	private boolean evaluatingBracket = false;
	
	public double modulus(String i, String j) {
		return Double.parseDouble(i) % Double.parseDouble(j);
	}
	
	/**
	 * 
	 * @param i is the left hand side number.
	 * @param j is the right hand side number.
	 * @return the sum of i and j.
	 */
	public double add(String i, String j) {
		return Double.parseDouble(i) + Double.parseDouble(j);
	}
	
	/**
	 * 
	 * @param i is the left hand side number.
	 * @param j is the right hand side number.
	 * @return the difference of i and j.
	 */
	public double subtract(String i, String j) {
		return Double.parseDouble(i) - Double.parseDouble(j);
	}
	
	/**
	 * 
	 * @param i is the left hand side number.
	 * @param j is the right hand side number.
	 * @return the product of i and j.
	 */
	public double multiply(String i, String j) {
		return Double.parseDouble(i) * Double.parseDouble(j);
	}
	
	/**
	 * 
	 * @param i is the left hand side number.
	 * @param j is the right hand side number.
	 * @return the quotient of i and j.
	 */
	public double divide(String i, String j) {
		return Double.parseDouble(i) / Double.parseDouble(j);
	}
	
	/**
	 * Calculate the base raised to exponent.
	 * @param base.
	 * @param exponent.
	 * @return the base raised to exponent.
	 */
	public double pow(String base, String exponent) {
		if(Double.parseDouble(exponent) == 0) {
			return 1;
		}
		if(Double.parseDouble(exponent) < 0) { /*If exponent is negative get 1 divided by base instead. */
			return (1 / Double.parseDouble(base)) * pow(base, Double.toString(Double.parseDouble(exponent) + 1));
		}
		return Double.parseDouble(base) * pow(base, Double.toString(Double.parseDouble(exponent) - 1));
	}
	
	/**
	 * 
	 * @param base.
	 * @param n is the nth root.
	 * @param approximate is a guess on the actual root of base.
	 * @return root of base to the nth degree.
	 */
	public double root(String base, String n, double approximate) {
		
		double baseDouble = Double.parseDouble(base);
		double nDoubleMinusOne = Double.parseDouble(n) - 1;
		
		double result = baseDouble / pow(Double.toString(approximate), Double.toString(nDoubleMinusOne));
		double average = (approximate * nDoubleMinusOne + result) / (nDoubleMinusOne + 1);
		double diff = result - average;
		
		if(diff < 0) {
			diff = -diff;
		}
		
		if(diff < precision) {
			return result;
		}
		
		return root(base, n, average);
	}
	
	/**
	 * Transform decimal into fraction.
	 * @param decimal is the decimal for fraction.
	 * @return a string containing the numerator and denominator as a fraction.
	 */
	public String decimalToFraction(String decimal) {
		String ten = "10";
		int decimalPlaces = decimal.length() - decimal.indexOf('.') - 1;
		
		if(decimalPlaces > 3) {
			decimalPlaces = 3;
		}
		String decimalPlacesString = Integer.toString(decimalPlaces);
		
		int numerator = (int) (Double.parseDouble(decimal) * pow(ten, decimalPlacesString));
		int denominator = (int) pow(ten, decimalPlacesString);
		int commonDivisor = getCommonDivisor(numerator, denominator);
		
		numerator /= commonDivisor;
		denominator /= commonDivisor;
		
		return numerator + "/" + denominator;
	}
	
	/**
	 * Find the greatest common divisor of numerator and denominator.
	 * @param numerator.
	 * @param denominator.
	 * @return the greatest common divisor.
	 */
	public int getCommonDivisor(int numerator, int denominator) {
		if(denominator == 0) {
			return numerator;
		}
		return getCommonDivisor(denominator, numerator % denominator);
	}
	
	/**
	 * Find the fractional exponent of base raised to the exponent.
	 * @param base.
	 * @param exponent.
	 * @return the fractional exponent of base.
	 */
	public double fractionalPow(String base, String exponent) {
		String result = "";
		String baseWithoutNegative = "";
		int sign = 1;
		int indexOfDecimal = exponent.indexOf('.');
		List<String> fractionList = new ArrayList<String>();
		
		if(Double.parseDouble(base) < 0) {
			baseWithoutNegative = base.substring(1);
			sign = -1;
		}
		else {
			baseWithoutNegative = base;
		}
		
		if(indexOfDecimal != -1) {
			String newBase = "";
			String[] fractionArray = decimalToFraction(exponent).split("/");
			fractionList.add(fractionArray[0]);
			fractionList.add(fractionArray[1]);
			newBase = Double.toString(root(baseWithoutNegative, fractionList.get(1), 1));
			result = Double.toString(sign * pow(newBase, fractionList.get(0)));
		}
		else {
			result = Double.toString(pow(base, exponent));
		}
		
		return Double.parseDouble(result);
	}
	
	/**
	 * Calculate a result based on operator and 2 numbers.
	 * @param symbol is the operator.
	 * @param left is the left hand number.
	 * @param right is the right hand number.
	 * @return the result from calculator.
	 */
	public double calResult(String symbol, String left, String right) {
		
		switch (symbol) {
		case "+":
			return add(left, right);
		case "-":
			return subtract(left, right);
		case "*":
			return multiply(left, right);
		case "/":
			return divide(left, right);
		case "**":
			return fractionalPow(left, right);
		case "^":
			return fractionalPow(left, right);
		case "%":
			return modulus(left, right);
		default:
			return 0.0;
		}
	}
	
	/**
	 * Strip and brackets from numbers, add them back if the number is not entirely enclosed in brackets.
	 * @param symbol is the operator for the calculator.
	 * @param leftNum is the left hand number.
	 * @param rightNum is the right hand number.
	 * @return a string of result with any valid bracket remaining.
	 */
	public String getResultString(String symbol, String leftNum, String rightNum) {
		double result = 0;
		String resultString = "";
		String leftWithoutBracket = "";
		String rightWithoutBracket = "";
		
		int lLeftBracket = leftNum.lastIndexOf('(');
		int lRightBracket = leftNum.indexOf(')');
		int rLeftBracket = rightNum.lastIndexOf('(');
		int rRightBracket = rightNum.indexOf(')');
		
		if(lLeftBracket != -1 && lRightBracket != -1) { /*Strip all left number bracket.*/
			leftWithoutBracket = leftNum.substring(lLeftBracket + 1, lRightBracket);
		}
		else if(lLeftBracket != -1) { /*Strip one left hand bracket. */
			leftWithoutBracket = leftNum.substring(lLeftBracket + 1);
		}
		else if(lRightBracket != -1) { /*Discard all right hand bracket. */
			leftWithoutBracket = leftNum.substring(0, lRightBracket);
		}
		else {
			leftWithoutBracket = leftNum;
		}
		
		if(rLeftBracket != -1 && rRightBracket != -1) { /*Strip all right number bracket.*/
			rightWithoutBracket = rightNum.substring(rLeftBracket + 1, rRightBracket);
		}
		else if(rLeftBracket != -1) {
			rightWithoutBracket = rightNum.substring(rLeftBracket + 1);
		}
		else if(rRightBracket != -1) {
			rightWithoutBracket = rightNum.substring(0, rRightBracket);
		}
		else {
			rightWithoutBracket = rightNum;
		}
		
		result = calResult(symbol, leftWithoutBracket, rightWithoutBracket);
		int[] bracketCount = getRemainingBracketCount(leftNum, rightNum); /*Array = lLeft, rRight */
		
		
		if(bracketCount[0] > 0) { /*Attach remaining brackets back into number */	
			resultString += leftNum.substring(0, bracketCount[0]);		
		}
		
		resultString += Double.toString(result);
		
		if(bracketCount[1] > 0) {
			resultString += rightNum.substring(rRightBracket, rRightBracket + bracketCount[1]);			
		}
		
		return resultString;
	}
	
	/**
	 * Sort the expression into a numbers array.
	 * @param expression contains numbers and symbols.
	 * @return a String array, each element contains number and may have brackets.
	 */
	public String[] getNumbers(String expression) {
		String stringWithoutSpace = expression.replaceAll(spaceRegex, "");
		
		if(stringWithoutSpace.indexOf('-') == 0) { /*Check if expression starts with a negative number*/
			String withoutFirstNegative = stringWithoutSpace.substring(1);
			
			String [] numberArray = withoutFirstNegative.split(symbolRegex);
			numberArray[0] = "-" + numberArray[0]; /*Replace the first element with the negative number*/
			return numberArray;
		}
		else if(stringWithoutSpace.indexOf('+') == 0) {
			String withoutFirstSymbols = stringWithoutSpace.substring(1);
			String [] numberArray = withoutFirstSymbols.split(symbolRegex);
			numberArray[0] = "+" + numberArray[0];
			return numberArray;
		}
		else {
			String [] numberArray = stringWithoutSpace.split(symbolRegex);	
			return numberArray;
		}
	}
	
	/**
	 * Sort the expression into a symbols array.
	 * @param expression contains numbers and symbols.
	 * @return a String array, each element contains only symbols.
	 */
	public String[] getSymbols(String expression) {
		String stringWithoutSpace = expression.replaceAll(spaceRegex, "");
		String[] numberArray = getNumbers(stringWithoutSpace);
		int indexOfFirstSymbol = numberArray[0].length(); /*Get the index of when the first symbol appears */
		String fromFirstSymbol = stringWithoutSpace.substring(indexOfFirstSymbol); /*Removes the first number*/
		String [] symbolsArray = fromFirstSymbol.split(numRegex);
		
		return symbolsArray;
	}
	
	/**
	 * Store indices of the left brackets in a list.
	 * @param numbers is the array containing all the numbers, numbers can contain brackets.
	 * @param leftBrackets is a global list to store all the index of left brackets
	 * @param fromRear is the index to start from, for left bracket start from the rear to front.
	 */
	public void getLeftBracketIndices(String[] numbers, List<Integer> leftBrackets, int fromRear){
		String number = numbers[fromRear];
		int indexOfLeftBracket = number.indexOf('(');
		
		if(indexOfLeftBracket != -1) {
			leftBrackets.add(fromRear);
		}
		if(fromRear >= 1) {
			getLeftBracketIndices(numbers, leftBrackets, fromRear - 1);			
		}
	}
	
	/**
	 * Get the index of the first right bracket after a left bracket. 
	 * @param numbers is the array containing all the numbers, numbers can contain brackets.
	 * @param startFrom is the index to start from, for right bracket start from the front to rear.
	 * @return the index of the first right bracket encountered.
	 */
	public int getRightBracketIndex(String[] numbers, int startFrom) {
		String number = numbers[startFrom];
		int indexOfRightBracket = number.indexOf(')');
		
		if(indexOfRightBracket == -1 && startFrom < numbers.length) {
			return getRightBracketIndex(numbers, startFrom + 1);
		}
		return startFrom;
	}
	
	/**
	 * Get the index of symbol base on order of operation.
	 * @param symbols array containing all the valid symbols.
	 * @return the index of the operation with the most priority.
	 */
	public int getIndexOfFirstOperation(String[] symbols) {
		int indexOfAdd = Arrays.asList(symbols).indexOf("+");
		int indexOfSub = Arrays.asList(symbols).indexOf("-");
		int indexOfMul = Arrays.asList(symbols).indexOf("*");
		int indexOfDiv = Arrays.asList(symbols).indexOf("/");
		int indexOfPow = Arrays.asList(symbols).lastIndexOf("**");
		int indexOfExp = Arrays.asList(symbols).lastIndexOf("^");
		
		if(indexOfMul < 0) {
			indexOfMul = Integer.MAX_VALUE;
		}
		if(indexOfDiv < 0) {
			indexOfDiv = Integer.MAX_VALUE;
		}
		if(indexOfAdd < 0) {
			indexOfAdd = Integer.MAX_VALUE;
		}
		if(indexOfSub < 0) {             
			indexOfSub = Integer.MAX_VALUE;
		}
		
		if(indexOfPow >= indexOfExp && indexOfPow != -1) {
			return indexOfPow;
		}
		else if(indexOfExp > indexOfPow && indexOfExp != -1) {
			return indexOfExp;
		}
		if(indexOfMul <= indexOfDiv && indexOfMul != Integer.MAX_VALUE) {
			return indexOfMul;
		}
		else if (indexOfDiv < indexOfMul) {
			return indexOfDiv;
		}
		
		if (indexOfAdd <= indexOfSub && indexOfAdd != Integer.MAX_VALUE) {
			return indexOfAdd;
		}
		else if (indexOfSub < indexOfAdd) {
			return indexOfSub;
		}
		
		return 0;
	}
	
	/**
	 * Seek index of negative numbers and place them into the lists. Check if there are any left recursively.
	 * @param symbols list containing all the valid symbols.
	 * @param numbers list containing all the valid numbers.
	 */
	public void replaceNegative(List<String> symbols, List<String> numbers) {
		int indexOfAddNeg = symbols.indexOf("+-");
		int indexOfSubNeg = symbols.indexOf("--");
		int indexOfMulNeg = symbols.indexOf("*-");
		int indexOfDivNeg = symbols.indexOf("/-");
		int indexOfPowNeg = symbols.indexOf("**-");
		int indexOfExpNeg = symbols.indexOf("^-");
 		boolean found = false;
		
		if(indexOfAddNeg != -1) {
			numbers.set(indexOfAddNeg + 1, "-"+numbers.get(indexOfAddNeg + 1));
			symbols.set(indexOfAddNeg, "+");
			found = true;
		}
		if(indexOfSubNeg != -1) {
			numbers.set(indexOfSubNeg + 1, "-"+numbers.get(indexOfSubNeg + 1));
			symbols.set(indexOfSubNeg, "-");
			found = true;
		}
		if(indexOfMulNeg != -1) {
			numbers.set(indexOfMulNeg + 1, "-"+numbers.get(indexOfMulNeg + 1));
			symbols.set(indexOfMulNeg, "*");
			found = true;
		}
		if(indexOfDivNeg != -1) {
			numbers.set(indexOfDivNeg + 1, "-"+numbers.get(indexOfDivNeg + 1));
			symbols.set(indexOfDivNeg, "/");
			found = true;
		}
		if(indexOfPowNeg != -1) {
			numbers.set(indexOfPowNeg + 1, "-"+numbers.get(indexOfPowNeg + 1));
			symbols.set(indexOfPowNeg, "**");
			found = true;
		}
		if(indexOfExpNeg != -1) {
			numbers.set(indexOfExpNeg + 1, "-"+numbers.get(indexOfExpNeg + 1));
			symbols.set(indexOfExpNeg, "^");
			found = true;
		}
		
		if(found) {
			replaceNegative(symbols, numbers);
		}
	}
	
	/**
	 * Seek index of numbers with plus sign and remove them from symbols list. Check if there are any left recursively.
	 * @param symbols list containing all the valid symbols.
	 * @param numbers list containing all the valid numbers.
	 */
	public void replacePositive(List<String> symbols, List<String> numbers) {
		int indexOfAddPov = symbols.indexOf("++");
		int indexOfSubPov = symbols.indexOf("-+");
		int indexOfMulPov = symbols.indexOf("*+");
		int indexOfDivPov = symbols.indexOf("/+");
		int indexOfPowPov = symbols.indexOf("**+");
		int indexOfExpPov = symbols.indexOf("^+");
 		boolean found = false;
		
		if(indexOfAddPov != -1) {
			symbols.set(indexOfAddPov, "+");
			found = true;
		}
		if(indexOfSubPov != -1) {
			symbols.set(indexOfSubPov, "-");
			found = true;
		}
		if(indexOfMulPov  != -1) {
			symbols.set(indexOfMulPov , "*");
			found = true;
		}
		if(indexOfDivPov != -1) {
			symbols.set(indexOfDivPov, "/");
			found = true;
		}
		if(indexOfPowPov != -1) {
			symbols.set(indexOfPowPov, "**");
			found = true;
		}
		if(indexOfExpPov != -1) {
			symbols.set(indexOfExpPov, "^");
			found = true;
		}
		
		if(found) {
			replacePositive(symbols, numbers);
		}
	}	
	
	/**
	 * Append any brackets inside numbers without a number to the next closest neighbor. Attach symbols in-between the brackets and number.
	 * @param symbols is the list containing symbols.
	 * @param numbers is the list containing numbers.
	 * @param index is the index of the current number.
	 */
	public void sortBracketsAndSymbols(List<String> symbols, List<String> numbers, int index) {
		boolean hasNumber = numbers.get(index).matches(".*[0-9].*");
		
		if(!hasNumber) {
			numbers.set(index, numbers.get(index) + symbols.get(index) + numbers.get(index + 1));
			numbers.remove(index + 1);
			symbols.remove(index);
		}
		if(index + 1 < numbers.size()) {
			sortBracketsAndSymbols(symbols, numbers, index + 1);
		}
	}
	
	/**
	 * Remove pair of brackets that are closed within the current number term itself.
	 * @param numbers is the list containing all the numbers.
	 * @param index is the index of the current number.
	 */
	public void cleanBrackets(List<String> numbers, int index) {
		String number = numbers.get(index);
		int indexOfFirstLeftBracket = number.indexOf('(');
		int indexOfLastLeftBracket = number.lastIndexOf('(');
		int indexOfRightBracket = number.indexOf(')');
		
		String numberAfterBracket = "";
		String leftBrackets = "";
		String rightBrackets = "";
		int signBeforeBracket = 1;
		String numberAfterCleaning = "";
		
		if(indexOfFirstLeftBracket != -1) {
			if(indexOfFirstLeftBracket != 0 && number.charAt(0) == '-') {	
				signBeforeBracket = -1;
			}
			if(indexOfLastLeftBracket != -1) {
				leftBrackets = number.substring(indexOfFirstLeftBracket, indexOfLastLeftBracket + 1);
			}
			if(indexOfRightBracket != -1) {
				numberAfterBracket = number.substring(indexOfLastLeftBracket + 1, indexOfRightBracket);
			}
			else {
				numberAfterBracket = number.substring(indexOfLastLeftBracket + 1);
			}
			
			double temp = Double.parseDouble(numberAfterBracket);
			temp *= signBeforeBracket;
			numberAfterBracket = Double.toString(temp);
		}
		else {
			if(indexOfRightBracket != -1) {
				numberAfterBracket = number.substring(0, indexOfRightBracket);
			}
			else {
				numberAfterBracket = number;
			}
		}
		
		if(indexOfRightBracket != -1) {
			rightBrackets = number.substring(indexOfRightBracket);
		}
		
		int LeftBracketCount = leftBrackets.length() - rightBrackets.length();
		int rightBracketCount = rightBrackets.length() - leftBrackets.length();
		
		if(LeftBracketCount > 0) {
			numberAfterCleaning += leftBrackets.substring(0, LeftBracketCount);
		}
		
		numberAfterCleaning += numberAfterBracket;
		
		if(rightBracketCount > 0) {
			numberAfterCleaning += rightBrackets.substring(0, rightBracketCount);
		}
  		
		numbers.set(index, numberAfterCleaning);
		
		if(index + 1 < numbers.size()) {
			cleanBrackets(numbers, index + 1);
		}
	}
	
	/**
	 * Count the brackets that are left after removing closed brackets within the leftNum to the rightNum.
	 * @param leftNum is the left number.
	 * @param rightNum is the right number.
	 * @return the count of brackets left in left number and right number, int[] brackets = {left number remaining bracket, right number remaining bracket}.
	 */
	public int[] getRemainingBracketCount(String leftNum, String rightNum) {
		int lLeftBracketIndex = leftNum.lastIndexOf('(');
		int lRightBracketIndex = leftNum.indexOf(')');
		int rLeftBracketIndex = rightNum.lastIndexOf('(');
		int rRightBracketIndex = rightNum.indexOf(')');
		
		int lLeftBracketCount = 0;
		int lRightBracketCount = 0;
		int rLeftBracketCount = 0;
		int rRightBracketCount = 0;
		
		int lLeftFinalCount = 0;
		int rRightFinalCount = 0;
		
		if(lLeftBracketIndex != -1) {
			lLeftBracketCount = leftNum.substring(0, lLeftBracketIndex + 1).length();
		}
		if(lRightBracketIndex != -1) {
			lRightBracketCount = leftNum.substring(lRightBracketIndex).length();
		}
		if(rLeftBracketIndex != -1) {
			rLeftBracketCount = rightNum.substring(0, rLeftBracketIndex + 1).length();
		}
		if(rRightBracketIndex != -1) {
			rRightBracketCount = rightNum.substring(rRightBracketIndex).length();
		}
		
		if(rRightBracketCount - rLeftBracketCount >= 0) {
			lLeftFinalCount = lLeftBracketCount - lRightBracketCount - (rRightBracketCount - rLeftBracketCount);			
		}
		else {
			lLeftFinalCount = lLeftBracketCount - lRightBracketCount;
		}
		
		if(lLeftBracketCount - rLeftBracketCount >= 0) {
			rRightFinalCount = rRightBracketCount - rLeftBracketCount - (lLeftBracketCount - lRightBracketCount);			
		}
		else {
			rRightFinalCount = rRightBracketCount - rLeftBracketCount;
		}
		
		int[] bracketCounts = {lLeftFinalCount, rRightFinalCount};
		
		return bracketCounts;
	}
	
	/**
	 * 
	 * @param symbols array containing all the valid symbols.
	 * @param indexOfOperation index of the last executed operation.
	 * @return the new symbols array without the last executed operation.
	 */
	public String[] reconstructSymbolsArray(String[] symbols, int indexOfOperation, int indexOfLastOperation) {
		String[] left = Arrays.copyOfRange(symbols, 0, indexOfOperation);
		String[] right = Arrays.copyOfRange(symbols, indexOfLastOperation + 1, symbols.length);
		String[] newArray = new String[left.length + right.length];
		
		System.arraycopy(left, 0, newArray, 0, left.length);
		System.arraycopy(right, 0, newArray, left.length, right.length);
		
		return newArray;
	}
	
	/**
	 * 
	 * @param numbers array containing all the valid numbers.
	 * @param indexOfOperation index of the last executed operation.
	 * @param numberToInsert number to insert into the new numbers array
	 * @return the new numbers array without the numbers on the left and right side of the operation.
	 */
	public String[] reconstructNumbersArray(String[] numbers, int indexOfOperation, int indexOfLastNumber ,String numberToInsert) {
		
		if(indexOfOperation + 1 >= numbers.length) { /*If the index of the number to insert is the right most position */
			String[] left = Arrays.copyOfRange(numbers, 0, indexOfOperation);
			String[] newArray = new String[left.length + 1];
			
			System.arraycopy(left, 0, newArray, 0, left.length);
			
			newArray[left.length] = numberToInsert;
			
			return newArray;
		}
		
		String[] left = Arrays.copyOfRange(numbers, 0, indexOfOperation);
		String[] right = Arrays.copyOfRange(numbers, indexOfLastNumber + 1, numbers.length);
		String[] newArray = new String[left.length + right.length + 1];
		
		System.arraycopy(left, 0, newArray, 0, left.length);
		System.arraycopy(right, 0, newArray, left.length + 1, right.length);
		
		newArray[left.length] = numberToInsert;
		
		return newArray;
	}
	
	/**
	 * Recursively form the new expression.
	 * @param symbols array containing all the valid symbols.
	 * @param number array containing all the valid numbers.
	 * @return expression containing all the symbols and numbers in the arrays.
	 */
	public String reconstructExpression(String[] symbols, String[] number) {
		
		if(number.length == 1 && symbols.length == 0) {
			return number[0];
		}
		else if(symbols.length == 1 && number.length == 1) {
			return symbols[0] + number[0];
		}
		return number[0] +
				symbols[0] +
				reconstructExpression(
						Arrays.copyOfRange(symbols, 1, symbols.length), 
						Arrays.copyOfRange(number, 1, number.length)
				);
	}
	
	/**
	 * @param expression contains numbers and symbols.
	 * @return result of all the executed operations.
	 */
	@Override
	public double evaluate(String expression) {
		//System.out.println(expression);
		double result = 0;
		String resultString = "";
		int indexOfFirstOperation = 0;
		int indexOfLastOperation = 0;
		int indexOfLastNumber = 0;
		String symbol = "";
		String leftNum = "";
		String rightNum = "";
		
		String[] nums = getNumbers(expression);
		String[] symbols = getSymbols(expression);
		List<String> numsList = new LinkedList<String> (Arrays.asList(nums));
		List<String> symbolsList = new LinkedList<String> (Arrays.asList(symbols));
		replaceNegative(symbolsList, numsList);
		replacePositive(symbolsList, numsList);
		sortBracketsAndSymbols(symbolsList, numsList, 0);
		cleanBrackets(numsList, 0);
		String[] cleanedNums = new String[numsList.size()];
		String[] cleanedSymbols = new String[symbolsList.size()];
		
		numsList.toArray(cleanedNums);
		symbolsList.toArray(cleanedSymbols);
		
		if(cleanedNums.length <= 1 || cleanedSymbols.length <= 0) {
			return Double.parseDouble(cleanedNums[0]);
		}	
		
		if(!bracketsListSet) {
			getLeftBracketIndices(cleanedNums, leftBrackets, cleanedNums.length - 1);
			bracketsListSet = true;
		}
		
		if(leftBrackets.size() != 0 && !evaluatingBracket) {
			
			indexOfFirstOperation = leftBrackets.get(0);
			indexOfLastNumber = getRightBracketIndex(cleanedNums, indexOfFirstOperation + 1);
			indexOfLastOperation = indexOfLastNumber - 1;
			
			if(indexOfFirstOperation == indexOfLastOperation) { /*If there is only one operator. */
				symbol = cleanedSymbols[indexOfFirstOperation];
				leftNum = cleanedNums[indexOfFirstOperation];
				rightNum = cleanedNums[indexOfLastNumber];
				indexOfLastOperation = indexOfFirstOperation;
				leftBrackets.remove(0);
			}
			else { /*Evaluate multiple numbers and operator enclosed inside the brackets. */
				evaluatingBracket = true;
				String[] numbersInsideBracket = Arrays.copyOfRange(cleanedNums, indexOfFirstOperation, indexOfLastNumber + 1);
				String[] symbolsInsideBracket = Arrays.copyOfRange(cleanedSymbols, indexOfFirstOperation, indexOfLastOperation + 1);
				
				String expressionInsideBracket = reconstructExpression(symbolsInsideBracket, numbersInsideBracket);
				result = evaluate(expressionInsideBracket);
				resultString = Double.toString(result);
				symbol = "";
				evaluatingBracket = false;
				
				leftBrackets.remove(0);
			}
		}
		else {			
			indexOfFirstOperation = getIndexOfFirstOperation(cleanedSymbols);
			indexOfLastOperation = indexOfFirstOperation;
			indexOfLastNumber = indexOfFirstOperation + 1;
			symbol = cleanedSymbols[indexOfFirstOperation];
			leftNum = cleanedNums[indexOfFirstOperation];
			rightNum = cleanedNums[indexOfLastNumber];
		}
		
		if(!symbol.equals("")) {
			resultString = getResultString(symbol, leftNum, rightNum);
		}
		
		String[] numbersArrayRemoveUsed = reconstructNumbersArray(cleanedNums, indexOfFirstOperation, indexOfLastNumber, resultString);
		String[] symbolsArrayRemoveUsed = reconstructSymbolsArray(cleanedSymbols, indexOfFirstOperation, indexOfLastOperation);

		return evaluate( 
				reconstructExpression(symbolsArrayRemoveUsed, numbersArrayRemoveUsed)
				);
	}
	
}