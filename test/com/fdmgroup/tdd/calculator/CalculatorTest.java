package com.fdmgroup.tdd.calculator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorTest {
	Calculator calc;
	@BeforeEach
	public void setup() {
		calc = new Calculator();
	}
	
	@Test
	void add_Equals8_3_5() {
		assertEquals(8.0, calc.add("3","5"));
	}
	
	@Test 
	void subtract_EqualsNeg2_3_5(){
		assertEquals(-2, calc.subtract("3","5"));
	}
	
	@Test 
	void product_Equals15_3_5(){
		assertEquals(15.0, calc.multiply("3","5"));
	}
	
	@Test 
	void division_EqualsPoint6_15_3(){
		assertEquals(0.6, calc.divide("3","5"));
	}
	
	@Test 
	void getNumbers_Equals4Symbols_Expression(){
		String[] expectedSym = {"+", "**", "/", "-"};
		String expression = "(2+2.0)**1/2-3";
		assertArrayEquals(expectedSym, calc.getSymbols(expression));
	}
	
	@Test 
	void getNumbers_Equals5Num_Expression(){
		String[] expectedNum = {"(2","2.0)","1","2","3"};
		String expression = "(2+2.0)**1/2-3";
		assertArrayEquals(expectedNum, calc.getNumbers(expression));
	}
	
	@Test
	void getNumbers_Equals5SymbolsNoSpace_Expression() {
		String[] expectedSym = {"+", "**", "/", "-", "^"};
		String expression = "(2 + 2.0) ** 1 / 2 - 3 ^ 6";
		assertArrayEquals(expectedSym, calc.getSymbols(expression));
	}
	
	@Test
	void getNumbers_Equals5NumNoSpace_Expression() {
		String[] expectedNum = {"(2","2.0)","1","2","3"};
		String expression = "( 2 + 2.0 ) ** 1 / 2 - 3";
		assertArrayEquals(expectedNum, calc.getNumbers(expression));
	}
	
	@Test
	void reconstruct_AdditionWithoutSpace_Expression() {
		String[] nums = {"2","2.0"};
		String[] symbols = {"+"};
		String expected = "2+2.0";
		assertEquals(expected, nums[0] + calc.reconstructExpression(symbols, Arrays.copyOfRange(nums, 1, nums.length)));
	}
	
	@Test
	void reconstruct_MultipleOperationWithoutSpace_Expression() {
		String[] nums = {"(2","2.0)","1","2","3","6"};
		String[] symbols = {"+", "**", "/", "-", "^"};
		String expected = "(2+2.0)**1/2-3^6";
		assertEquals(expected, calc.reconstructExpression(symbols, nums));
	}
	
	@Test
	void evaluate_Equals4_TwoPlusTwo() {
		String expression = "2+2";
		double expected = 4;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_Equals2_TwoPlusTwoMinusTwo() {
		String expression = "2+2-2";
		double expected = 2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_EqualsZero_TwoPlusTwoMinusTwoTimesTwo() {
		String expression = "2+2-2*2";
		double expected = 0;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_Equals2_TwoPlusTwoMinusTwoTimesTwoDivideByTwo() {
		String expression = "2+2-2*2/2";
		double expected = 2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_Equals22Point2_TwentyTwoPoint2PlusTwentyTwoPoint2MinusTwentyTwoPoint2TimesTwentyTwoPoint2DivideByTwentyTwoPoint2() {
		String expression = "22.2+22.2-22.2*22.2/22.2";
		double expected = 22.2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void getIndexOfFirstOperation_indexOfAdd_ArrayOfSymbol() {
		String[] symbols = {"+"};
		int expected = 0;
		assertEquals(expected, calc.getIndexOfFirstOperation(symbols));
	}
	
	@Test
	void getIndexOfFirstOperation_indexOfMinus_ArrayOfSymbol() {
		String[] symbols = {"-","+","-","+","-"};
		int expected = 0;
		assertEquals(expected, calc.getIndexOfFirstOperation(symbols));
	}
	
	@Test
	void getIndexOfFirstOperation_indexOfMultiplication_ArrayOfSymbol() {
		String[] symbols = {"+","-","*","/"};
		int expected = 2;
		assertEquals(expected, calc.getIndexOfFirstOperation(symbols));
	}
	
	@Test
	void getIndexOfFirstOperation_indexOfDivision_ArrayOfSymbol() {
		String[] symbols = {"+","/","*","/","-"};
		int expected = 1;
		assertEquals(expected, calc.getIndexOfFirstOperation(symbols));
	}
	
	@Test
	void reconstructSymbolsArray_SymbolIsMiddle_IndexEqualsThree() {
		String[] symbols = {"-","+","-","-","-","*","/"};
		String[] expected = {"-","+","-","-","*","/"};
		int index = 3;
		assertArrayEquals(expected, calc.reconstructSymbolsArray(symbols, index));
	}
	
	@Test
	void reconstructSymbolsArray_SymbolIsLastIndex_IndexEqualsArrayLengthMinusOne() {
		String[] symbols = {"-","+","-","-","-","*","/"};
		String[] expected = {"-","+","-","-","-","*"};
		int index = symbols.length - 1;
		assertArrayEquals(expected, calc.reconstructSymbolsArray(symbols, index));
	}
	
	@Test
	void reconstructSymbolsArray_SymbolIsFirstIndex_IndexEqualsZero() {
		String[] symbols = {"-","+","-","-","-","*","/"};
		String[] expected = {"+","-","-","-","*","/"};
		int index = 0;
		assertArrayEquals(expected, calc.reconstructSymbolsArray(symbols, index));
	}
	
	@Test
	void reconstructNumbersArray_NumbersIsMiddle_IndexEqualsThree() {
		String[] symbols = {"(2","2.0)","1","2","3","6"};
		String[] expected = {"(2","2.0)","1","5","6"};
		int index = 3;
		String numberToInsert = "5";
		assertArrayEquals(expected, calc.reconstructNumbersArray(symbols, index, numberToInsert));
	}
	
	@Test
	void reconstructNumbersArray_NumbersIsFirstIndex_IndexEqualsZero() {
		String[] symbols = {"(2","2.0)","1","2","3","6"};
		String[] expected = {"4.0","1","2","3","6"};
		int index = 0;
		String numberToInsert = "4.0";
		assertArrayEquals(expected, calc.reconstructNumbersArray(symbols, index, numberToInsert));
	}
	
	@Test
	void reconstructNumbersArray_NumbersIsLastIndex_IndexEqualsArrayLengthMinusTwo() {
		String[] symbols = {"(2","2.0)","1","2","3","6"};
		String[] expected = {"(2","2.0)","1","2","3","-1"};
		int index = symbols.length - 1;
		String numberToInsert = "-1";
		assertArrayEquals(expected, calc.reconstructNumbersArray(symbols, index, numberToInsert));
	}
	
	@Test
	void reconstructNumbersArray_NumbersIsSecondLastIndex_IndexEqualsArrayLengthMinusTwo() {
		String[] symbols = {"(2","2.0)","1","2","3","6"};
		String[] expected = {"(2","2.0)","1","2","9"};
		int index = symbols.length - 2;
		String numberToInsert = "9";
		assertArrayEquals(expected, calc.reconstructNumbersArray(symbols, index, numberToInsert));
	}
	
	@Test
	void evaluation_NumberIsPointTwo_3DivideBy15() {
		String expression = "3/15";
		double expected = 0.2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsOne_3DivideBy15Times5() {
		String expression = "3/15*5";
		double expected = 1;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsPointOOTwo_3DivideBy100() {
		String expression = "3/15/100";
		double expected = 0.002;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsTwo_3DivideBy100Time1000() {
		String expression = "3/15/100*1000";
		double expected = 2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsNegTwo_3DivideBy100Time1000Minus4() {
		String expression = "3/15/100*1000-4";
		double expected = -2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsTwo_4Minus3DivideBy100Time1000() {
		String expression = "4-3/15/100*1000";
		double expected = 2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void getNumbers_Equals8NumNoSpace_Expression() {
		String[] expectedNum = {"-3","(2","2.0)","1","2","3","3"};
		String expression = "-3 + ( 2 + 2.0 ) ** 1 / 2 - 3 --3";
		assertArrayEquals(expectedNum, calc.getNumbers(expression));
	}
	
	@Test
	void getSymbols_Equals7SymbolNoSpace_Expression() {
		String[] expectedNum = {"+","+","**","/","-","--"};
		String expression = "-3 + ( 2 + 2.0 ) ** 1 / 2 - 3 --3";
		assertArrayEquals(expectedNum, calc.getSymbols(expression));
	}
	
	@Test
	void evaluation_NumberIsNegTwo_Neg3DivideBy100Time1000() {
		String expression = "-3/15/100*1000";
		double expected = -2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsTwenty_TenMinusNegTen() {
		String expression = "10--10";
		double expected = 20;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsZero_TenMinusNegTenPlusNegTwenty() {
		String expression = "10--10+-20";
		double expected = 0;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIs120_TenMinusNegTenPlusNeg10TimesNeg10() {
		String expression = "10--10+-10*-10";
		double expected = 120;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsNeg30_TenMinusNegTenPlusNeg10TimesNeg10DivNeg2() {
		String expression = "10--10+-10*-10/-2";
		double expected = -30;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsNeg50_NegTenMinusNegTenPlusNeg10TimesNeg10DivNeg2() {
		String expression = "-10--10+-10*-10/-2";
		double expected = -50;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIs50_TenDivNegTwoTimesNegTenPlusNeg10MinusNeg10() {
		String expression = "10/-2*-10+-10--10";
		double expected = 50;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_NumberIsNeg50_NegTenDivNegTwoTimesNegTenPlusNeg10MinusNeg10() {
		String expression = "-10/-2*-10+-10--10";
		double expected = -50;
		assertEquals(expected, calc.evaluate(expression));
	}
}
