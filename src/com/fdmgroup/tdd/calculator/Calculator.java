package com.fdmgroup.tdd.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculator implements ICalculator{
	private final String symbolRegex = "[^()0-9\\.]+";
	private final String numRegex = "[()0-9\\.]+";
	private final String spaceRegex = "\\s";
	private final double precision = 0.0000000000001;
	private List<Integer> leftBrackets = new ArrayList<Integer>();
	private boolean bracketsListSet = false;
	private boolean evaluatingBracket = false;
	
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
	 * 
	 * @param base
	 * @param exponent
	 * @return
	 */
	public double pow(String base, String exponent) {
		if(Double.parseDouble(exponent) == 0) {
			return 1;
		}
		if(Double.parseDouble(exponent) < 0) {
			return (1 / Double.parseDouble(base)) * pow(base, Double.toString(Double.parseDouble(exponent) + 1));
		}
		return Double.parseDouble(base) * pow(base, Double.toString(Double.parseDouble(exponent) - 1));
	}
	
	/**
	 * 
	 * @param base
	 * @param n
	 * @param approximate
	 * @return
	 */
	public double root(String base, String n, double approximate) {
		
		double baseDouble = Double.parseDouble(base);
		double nDouble = Double.parseDouble(n);
		
		double result = baseDouble / pow(Double.toString(approximate), Double.toString( nDouble - 1));
		double average = (approximate * (nDouble - 1) + result) / nDouble;
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
	 * 
	 * @param expression contains numbers and symbols.
	 * @return a String array, each element contains number and may have brackets.
	 */
	public String[] getNumbers(String expression) {
		String stringWithoutSpace = expression.replaceAll(spaceRegex, "");
		
		if(stringWithoutSpace.indexOf('-') == 0) { /*Check if expression starts with a negative number*/
			String firstNegative = "-";
			String withoutFirstNegative = stringWithoutSpace.substring(1);
			
			String [] numberArray = withoutFirstNegative.split(symbolRegex);
			numberArray[0] = firstNegative + numberArray[0]; /*Replace the first element with the negative number*/
			return numberArray;
		}
		else if(stringWithoutSpace.indexOf('+') == 0) {
			String withoutFirstSymbols = stringWithoutSpace.substring(1);
			
			String [] numberArray = withoutFirstSymbols.split(symbolRegex);
			return numberArray;
		}
		else {
			String [] numberArray = stringWithoutSpace.split(symbolRegex);	
			return numberArray;
		}
	}
	
	/**
	 * 
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
	 * 
	 * @param numbers
	 * @param leftBrackets
	 * @param fromRear
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
	 * 
	 * @param numbers
	 * @param startFrom
	 * @return
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
	 * Get the index of symbol base on order of operation.
	 * @param symbols array containing all the valid symbols.
	 * @return the index of the operation with the most priority.
	 */
	public int getIndexOfFirstOperation(String[] symbols) {
		int indexOfAdd = Arrays.asList(symbols).indexOf("+");
		int indexOfSub = Arrays.asList(symbols).indexOf("-");
		int indexOfMul = Arrays.asList(symbols).indexOf("*");
		int indexOfDiv = Arrays.asList(symbols).indexOf("/");
		int indexOfPow = Arrays.asList(symbols).indexOf("**");
		int indexOfExp = Arrays.asList(symbols).indexOf("^");
		
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
		if(indexOfPow < 0) {
			indexOfPow = Integer.MAX_VALUE;
		}
		if(indexOfExp < 0) {
			indexOfExp = Integer.MAX_VALUE;
		}
		
		if(indexOfPow <= indexOfExp && indexOfPow != Integer.MAX_VALUE) {
			return indexOfPow;
		}
		else if(indexOfExp < indexOfPow) {
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
	 * 
	 * @param symbol
	 * @param left
	 * @param right
	 * @return
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
			return  pow(left, right);
		case "^":
			return pow(left, right);
		default:
			return 0.0;
		}
	}
	
	/**
	 * Strip and brackets from numbers, add them back if the number is not entirely enclosed in brackets.
	 * @param symbol
	 * @param leftNum
	 * @param rightNum
	 * @return
	 */
	public String getResultString(String symbol, String leftNum, String rightNum) {
	
		double result = 0;
		String resultString = "";
		String left = "";
		String right = "";
		String leftLeftBracketLeftOver = "";
		String rightRightBracketLeftOver = "";
		
		int leftLeftBracket = leftNum.lastIndexOf('(');
		int leftRightBracket = leftNum.indexOf(')');
		int rightLeftBracket = rightNum.lastIndexOf('(');
		int rightRightBracket = rightNum.indexOf(')');
		
		if(leftLeftBracket != -1 && leftRightBracket != -1) {
			left = leftNum.substring(leftLeftBracket + 1, leftRightBracket);
			leftLeftBracketLeftOver = leftNum.substring(0, leftLeftBracket - 1);
		}
		else if(leftLeftBracket != -1) {
			left = leftNum.substring(leftLeftBracket + 1);
			leftLeftBracketLeftOver = leftNum.substring(0, leftLeftBracket);
		}
		else if(leftRightBracket != -1) {
			left = leftNum.substring(0, leftRightBracket);
		}
		else {
			left = leftNum;
		}
		
		if(rightLeftBracket != -1 && rightRightBracket != -1) {
			right = rightNum.substring(rightLeftBracket + 1, rightRightBracket);
			rightRightBracketLeftOver = rightNum.substring(rightRightBracket + 2);
		}
		else if(rightLeftBracket != -1) {
			right = rightNum.substring(rightLeftBracket + 1);
		}
		else if(rightRightBracket != -1) {
			right = rightNum.substring(0, rightRightBracket);
			rightRightBracketLeftOver = rightNum.substring(rightRightBracket + 1);
		}
		else {
			right = rightNum;
		}
		
		result = calResult(symbol, left, right);
		
		if(leftLeftBracketLeftOver.length() != 0) {
			int bracketDifference = leftLeftBracketLeftOver.length() - rightRightBracketLeftOver.length();
			if(bracketDifference > 0) {
				resultString += leftLeftBracketLeftOver.substring(0, bracketDifference);				
			}
		}
		
		resultString += Double.toString(result);
		
		if(rightRightBracketLeftOver.length() != 0) {
			int bracketDifference = rightRightBracketLeftOver.length() - leftLeftBracketLeftOver.length();
			if(bracketDifference > 0) {
				resultString += rightRightBracketLeftOver;				
			}
		}
		
		return resultString;
	}
	
	/**
	 * @param expression contains numbers and symbols.
	 * @return result of all the executed operations.
	 */
	@Override
	public double evaluate(String expression) {
		System.out.println(expression);
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
		List<String> numsList = Arrays.asList(nums);
		List<String> symbolsList = Arrays.asList(symbols);
		replaceNegative(symbolsList, numsList );
		
		numsList.toArray(nums);
		symbolsList.toArray(symbols);
		
		if(!bracketsListSet) {
			getLeftBracketIndices(nums, leftBrackets, nums.length - 1);
			bracketsListSet = true;
		}
		
		if(leftBrackets.size() != 0 && !evaluatingBracket) {
			
			indexOfFirstOperation = leftBrackets.get(0);
			if(indexOfFirstOperation + 1 >= nums.length) {
				leftBrackets.remove(0);
				indexOfFirstOperation = leftBrackets.get(0);
			}
			indexOfLastNumber = getRightBracketIndex(nums, indexOfFirstOperation + 1);
			indexOfLastOperation = indexOfLastNumber - 1;
			
			if(indexOfFirstOperation == indexOfLastOperation) {
				symbol = symbols[indexOfFirstOperation];
				leftNum = nums[indexOfFirstOperation];
				rightNum = nums[indexOfLastNumber];
				indexOfLastOperation = indexOfFirstOperation;
				leftBrackets.remove(0);
			}
			else {
				evaluatingBracket = true;
				String[] numbersInsideBracket = Arrays.copyOfRange(nums, indexOfFirstOperation, indexOfLastNumber + 1);
				String[] symbolsInsideBracket = Arrays.copyOfRange(symbols, indexOfFirstOperation, indexOfLastOperation + 1);
				
				String expressionInsideBracket = reconstructExpression(symbolsInsideBracket, numbersInsideBracket);
				result = evaluate(expressionInsideBracket);
				resultString = Double.toString(result);
				symbol = "";
				evaluatingBracket = false;
				
				leftBrackets.remove(0);
			}
		}
		else {			
			indexOfFirstOperation = getIndexOfFirstOperation(symbols);
			indexOfLastOperation = indexOfFirstOperation;
			indexOfLastNumber = indexOfFirstOperation + 1;
			symbol = symbols[indexOfFirstOperation];
			leftNum = nums[indexOfFirstOperation];
			rightNum = nums[indexOfLastNumber];
		}
		
		if(!symbol.equals("")) {
			resultString = getResultString(symbol, leftNum, rightNum);
		}
		
		String[] numbersArrayRemoveUsed = reconstructNumbersArray(nums, indexOfFirstOperation, indexOfLastNumber, resultString);
		String[] symbolsArrayRemoveUsed = reconstructSymbolsArray(symbols, indexOfFirstOperation, indexOfLastOperation);
		
		if(numbersArrayRemoveUsed.length <= 1 || symbolsArrayRemoveUsed.length <= 0) {
			return Double.parseDouble(resultString);
		}	
		
		return evaluate( 
				reconstructExpression(symbolsArrayRemoveUsed, numbersArrayRemoveUsed)
				);
	}
	
}
