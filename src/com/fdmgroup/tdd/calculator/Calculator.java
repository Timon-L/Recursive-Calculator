package com.fdmgroup.tdd.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator implements ICalculator{
	private final String symbolRegex = "[^()0-9\\.]+";
	private final String numRegex = "[()0-9\\.]+";
	private final String spaceRegex = "\\s";
	
	public Double add(String i, String j) {
		return Double.parseDouble(i) + Double.parseDouble(j);
	}

	public Double subtract(String i, String j) {
		return Double.parseDouble(i) - Double.parseDouble(j);
	}

	public Double multiply(String i, String j) {
		return Double.parseDouble(i) * Double.parseDouble(j);
	}

	public Double divide(String i, String j) {
		return Double.parseDouble(i) / Double.parseDouble(j);
	}
	
	public String[] getNumbers(String expression) {
		String stringWithoutSpace = expression.replaceAll(spaceRegex, "");
		
		if(stringWithoutSpace.indexOf('-') == 0) {
			String firstNegative = "-";
			String withoutFirstNegative = stringWithoutSpace.substring(1);
			
			String [] numberArray = withoutFirstNegative.split(symbolRegex);
			numberArray[0] = firstNegative + numberArray[0];
			
			return numberArray;
		}
		else {
			String [] numberArray = stringWithoutSpace.split(symbolRegex);	
			return numberArray;
		}
	}
	
	public String[] getSymbols(String expression) {
		String stringWithoutSpace = expression.replaceAll(spaceRegex, "");
		String[] numberArray = getNumbers(stringWithoutSpace);
		int indexOfFirstSymbol = numberArray[0].length();
		String fromFirstSymbol = stringWithoutSpace.substring(indexOfFirstSymbol);
		String [] symbolsArray = fromFirstSymbol.split(numRegex);
		
		return symbolsArray;
	}
	
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
	
	public int getIndexOfFirstOperation(String[] symbols) {
		int indexOfAdd = Arrays.asList(symbols).indexOf("+");
		int indexOfSub = Arrays.asList(symbols).indexOf("-");
		int indexOfMul = Arrays.asList(symbols).indexOf("*");
		int indexOfDiv = Arrays.asList(symbols).indexOf("/");
		
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
	
	public String[] reconstructSymbolsArray(String[] symbols, int indexOfOperation) {
		String[] left = Arrays.copyOfRange(symbols, 0, indexOfOperation);
		String[] right = Arrays.copyOfRange(symbols, indexOfOperation + 1, symbols.length);
		String[] newArray = new String[left.length + right.length];
		
		System.arraycopy(left, 0, newArray, 0, left.length);
		System.arraycopy(right, 0, newArray, left.length, right.length);
		
		return newArray;
	}
	
	public String[] reconstructNumbersArray(String[] numbers, int indexOfOperation, String numberToInsert) {
		
		if(indexOfOperation + 1 >= numbers.length) {
			String[] left = Arrays.copyOfRange(numbers, 0, indexOfOperation);
			String[] newArray = new String[left.length + 1];
			
			System.arraycopy(left, 0, newArray, 0, left.length);
			
			newArray[left.length] = numberToInsert;
			
			return newArray;
		}
		
		String[] left = Arrays.copyOfRange(numbers, 0, indexOfOperation);
		String[] right = Arrays.copyOfRange(numbers, indexOfOperation + 2, numbers.length);
		String[] newArray = new String[left.length + right.length + 1];
		
		System.arraycopy(left, 0, newArray, 0, left.length);
		System.arraycopy(right, 0, newArray, left.length + 1, right.length);
		
		newArray[left.length] = numberToInsert;
		
		return newArray;
	}
	
	@Override
	public double evaluate(String expression) {
		double left = 0;
		//System.out.println(expression);
		String[] nums = getNumbers(expression);
		String[] symbols = getSymbols(expression);
		
		List<String> numsList = Arrays.asList(nums);
		List<String> symbolsList = Arrays.asList(symbols);
		replaceNegative(symbolsList, numsList );
		
		numsList.toArray(nums);
		symbolsList.toArray(symbols);
		
		//for(String num: numsList) {
			//System.out.print(num + " ");
		//}
		//System.out.print("\n");
		//for(String sym: symbolsList) {
			//System.out.print(sym + " ");
		//}
		
		int indexOfFirstOperation = getIndexOfFirstOperation(symbols);
		
		String symb = symbols[indexOfFirstOperation];
		String leftNum = nums[indexOfFirstOperation];
		String rightNum = nums[indexOfFirstOperation + 1];
		
		switch (symb) {
		case "+":
			left = add(leftNum, rightNum);
			break;
		case "-":
			left = subtract(leftNum, rightNum);
			break;
		case "*":
			left = multiply(leftNum, rightNum);
			break;
		case "/":
			left = divide(leftNum, rightNum);
			break;
		}
		
		if(nums.length <= 1 || symbols.length <= 1) {
			return left;
		}	
		
		String[] numbersArrayRemoveUsed = reconstructNumbersArray(nums, indexOfFirstOperation, Double.toString(left));
		String[] symbolsArrayRemoveUsed = reconstructSymbolsArray(symbols, indexOfFirstOperation);
		
		return evaluate( 
				reconstructExpression(symbolsArrayRemoveUsed, numbersArrayRemoveUsed)
				);
	}
	
}
