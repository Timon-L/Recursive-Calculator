package com.fdmgroup.tdd.calculator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorTest {
	private final double precision = 0.0000000000001;
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
		assertArrayEquals(expected, calc.reconstructSymbolsArray(symbols, index, index));
	}
	
	@Test
	void reconstructSymbolsArray_SymbolIsLastIndex_IndexEqualsArrayLengthMinusOne() {
		String[] symbols = {"-","+","-","-","-","*","/"};
		String[] expected = {"-","+","-","-","-","*"};
		int index = symbols.length - 1;
		assertArrayEquals(expected, calc.reconstructSymbolsArray(symbols, index, index));
	}
	
	@Test
	void reconstructSymbolsArray_SymbolIsFirstIndex_IndexEqualsZero() {
		String[] symbols = {"-","+","-","-","-","*","/"};
		String[] expected = {"+","-","-","-","*","/"};
		int index = 0;
		assertArrayEquals(expected, calc.reconstructSymbolsArray(symbols, index, index));
	}
	
	@Test
	void reconstructNumbersArray_NumbersIsMiddle_IndexEqualsThree() {
		String[] symbols = {"(2","2.0)","1","2","3","6"};
		String[] expected = {"(2","2.0)","1","5","6"};
		int index = 3;
		String numberToInsert = "5";
		assertArrayEquals(expected, calc.reconstructNumbersArray(symbols, index, index + 1, numberToInsert));
	}
	
	@Test
	void reconstructNumbersArray_NumbersIsFirstIndex_IndexEqualsZero() {
		String[] symbols = {"(2","2.0)","1","2","3","6"};
		String[] expected = {"4.0","1","2","3","6"};
		int index = 0;
		String numberToInsert = "4.0";
		assertArrayEquals(expected, calc.reconstructNumbersArray(symbols, index, index + 1,numberToInsert));
	}
	
	@Test
	void reconstructNumbersArray_NumbersIsLastIndex_IndexEqualsArrayLengthMinusTwo() {
		String[] symbols = {"(2","2.0)","1","2","3","6"};
		String[] expected = {"(2","2.0)","1","2","3","-1"};
		int index = symbols.length - 1;
		String numberToInsert = "-1";
		assertArrayEquals(expected, calc.reconstructNumbersArray(symbols, index, index + 1, numberToInsert));
	}
	
	@Test
	void reconstructNumbersArray_NumbersIsSecondLastIndex_IndexEqualsArrayLengthMinusTwo() {
		String[] symbols = {"(2","2.0)","1","2","3","6"};
		String[] expected = {"(2","2.0)","1","2","9"};
		int index = symbols.length - 2;
		String numberToInsert = "9";
		assertArrayEquals(expected, calc.reconstructNumbersArray(symbols, index, index + 1, numberToInsert));
	}
	
	@Test
	void evaluation_ResultIsPointTwo_3DivideBy15() {
		String expression = "3/15";
		double expected = 0.2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsOne_3DivideBy15Times5() {
		String expression = "3/15*5";
		double expected = 1;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsPointOOTwo_3DivideBy100() {
		String expression = "3/15/100";
		double expected = 0.002;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsTwo_3DivideBy100Time1000() {
		String expression = "3/15/100*1000";
		double expected = 2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsNegTwo_3DivideBy100Time1000Minus4() {
		String expression = "3/15/100*1000-4";
		double expected = -2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsTwo_4Minus3DivideBy100Time1000() {
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
	void evaluation_ResultIsNegTwo_Neg3DivideBy100Time1000() {
		String expression = "-3/15/100*1000";
		double expected = -2;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsTwenty_TenMinusNegTen() {
		String expression = "10--10";
		double expected = 20;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsZero_TenMinusNegTenPlusNegTwenty() {
		String expression = "10--10+-20";
		double expected = 0;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIs120_TenMinusNegTenPlusNeg10TimesNeg10() {
		String expression = "10--10+-10*-10";
		double expected = 120;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsNeg30_TenMinusNegTenPlusNeg10TimesNeg10DivNeg2() {
		String expression = "10--10+-10*-10/-2";
		double expected = -30;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsNeg50_NegTenMinusNegTenPlusNeg10TimesNeg10DivNeg2() {
		String expression = "-10--10+-10*-10/-2";
		double expected = -50;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIs50_TenDivNegTwoTimesNegTenPlusNeg10MinusNeg10() {
		String expression = "10/-2*-10+-10--10";
		double expected = 50;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluation_ResultIsNeg50_NegTenDivNegTwoTimesNegTenPlusNeg10MinusNeg10() {
		String expression = "-10/-2*-10+-10--10";
		double expected = -50;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void pow_ResultIs8_2Pow3() {
		String base = "2";
		String exponent = "3";
		double expected = 8;
		assertEquals(expected, calc.pow(base, exponent));
	}
	
	@Test
	void pow_ResultIs2_2Pow1() {
		String base = "2";
		String exponent = "1";
		double expected = 2;
		assertEquals(expected, calc.pow(base, exponent));
	}
	
	@Test
	void pow_ResultIs1_2Pow0() {
		String base = "2";
		String exponent = "0";
		double expected = 1;
		assertEquals(expected, calc.pow(base, exponent));
	}
	
	@Test
	void pow_ResultIs1073741824_2Pow30() {
		String base = "2";
		String exponent = "30";
		double expected = 1073741824;
		assertEquals(expected, calc.pow(base, exponent));
	}
	
	@Test
	void pow_ResultIs0Point03125_2PowNeg5() {
		String base = "2";
		String exponent = "-5";
		double expected = 0.03125;
		assertEquals(expected, calc.pow(base, exponent), precision);
	}
	
	@Test
	void evaluate_ResultIs1073741824_2Pow30() {
		String expression = "2**30";
		double expected = 1073741824;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs1073741824_2Exp30() {
		String expression = "2^30";
		double expected = 1073741824;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs8_2Pow2Times2() {
		String expression = "2**2*2";
		double expected = 8;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs8_2Exp2Times2() {
		String expression = "2^2*2";
		double expected = 8;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs16_2Times2Pow3() {
		String expression = "2*2**3";
		double expected = 16;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIsNeg4_4Minus2Div2Times2Pow3() {
		String expression = "4-2/2*2**3";
		double expected = -4;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void root_ResultIs5Point1961524227066_SquareRoot27() {
		String base = "27";
		String n = "2";
		double expected = 5.1961524227066;
		assertEquals(expected, calc.root(base, n, 1), precision);
	}
	
	@Test
	void root_ResultIs1Point4028505520067_8thRoot15() {
		String base = "15";
		String n = "8";
		double expected = 1.4028505520067;
		assertEquals(expected, calc.root(base, n, 1), precision);
	}
	
	@Test
	void getBracketIndices_LeftIs4_3_2_1_0_Expression() {
		String expression = "(1+(1o(1o(1oP(1+)Po)+1)o1)o1)";
		int[] expectedLeft = {4,3,2,1,0};
		List<Integer> leftBrackets = new ArrayList<Integer>();
		
		String[] numbers = calc.getNumbers(expression);
		System.out.print("\n");
		calc.getLeftBracketIndices(numbers, leftBrackets, numbers.length - 1);
		int[] leftBracketsArray = leftBrackets.stream().mapToInt(i->i).toArray();
	
		assertArrayEquals(expectedLeft, leftBracketsArray);
	}
	
	@Test
	void getBracketIndices_LeftIs2_1_0_Expression() {
		String expression = "(1+(1*(1)*1)+1)";
		int[] expectedLeft = {2,1,0};
		List<Integer> leftBrackets = new ArrayList<Integer>();
		
		String[] numbers = calc.getNumbers(expression);
		calc.getLeftBracketIndices(numbers, leftBrackets, numbers.length - 1);
		int[] leftBracketsArray = leftBrackets.stream().mapToInt(i->i).toArray();

		assertArrayEquals(expectedLeft, leftBracketsArray);
	}
	
	@Test
	void getBracketIndices_LeftIs6_4_2_0_Expression() {
		String expression = "(1+1)+(2-2)-(3*3)*(4/4)";
		int[] expectedLeft = {6,4,2,0};
		List<Integer> leftBrackets = new ArrayList<Integer>();
		
		String[] numbers = calc.getNumbers(expression);
		calc.getLeftBracketIndices(numbers, leftBrackets, numbers.length - 1);
		int[] leftBracketsArray = leftBrackets.stream().mapToInt(i->i).toArray();
		
		assertArrayEquals(expectedLeft, leftBracketsArray);
	}
	
	@Test
	void getBracketIndices_LeftIs5_4_1_0_Expression() {
		String expression = "(1+(2-2)*1)/(3**(4^4)+3)";
		int[] expectedLeft = {5,4,1,0};
		List<Integer> leftBrackets = new ArrayList<Integer>();
		
		String[] numbers = calc.getNumbers(expression);
		calc.getLeftBracketIndices(numbers, leftBrackets, numbers.length - 1);
		int[] leftBracketsArray = leftBrackets.stream().mapToInt(i->i).toArray();
		assertArrayEquals(expectedLeft, leftBracketsArray);
	}
	
	@Test
	void evaluate_ResultIs40_10TimesBracket2Plus2() {
		String expression = "10*(2+2)";
		double expected = 40;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs30_2TimesBracket1Plus3Times4BracketPlus4() {
		String expression = "2*(1+3*4)+4";
		double expected = 30;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs56_Bracket2Plus2BracketPlusBracket2Times2BracketTimesBracket1Plus3Times4() {
		String expression = "(2+2)+(2*2)*(1+3*4)";
		double expected = 56;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs256_Bracket2TimesBracket2Plus2BracketTimes2BracketPow2() {
		String expression = "(2*(2+2)*2)**2";
		double expected = 256;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs32_Bracket2TimesBracket2PlusBracket3TimesTwoBracketBracketTimesBracket10Div5BracketBracket() {
		String expression = "(2*(2+(3*2))*(10/5))";
		double expected = 32;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs32_Bracket2TimesBracket2PlusBracketBracket3BracketTimesTwoBracketBracketTimesBracket10Div5BracketBracket() {
		String expression = "(2*(2+((3)*2))*(10/5))";
		double expected = 32;
		assertEquals(expected, calc.evaluate(expression));
	}
}
