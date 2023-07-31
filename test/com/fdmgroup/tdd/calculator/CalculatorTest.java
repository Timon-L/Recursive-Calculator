package com.fdmgroup.tdd.calculator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorTest {
	private final double precision = 0.0000000001;
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
		String expression = "(1+(1*(1/(1++(1+)--)+1)**1)+1)";
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
	void evaluate_ResultIs40_10Times_LBKT_2Plus2_RBKT() {
		String expression = "10*(2+2)";
		double expected = 40;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs30_2Times_LBKT_1Plus3Times4_RBKT_Plus4() {
		String expression = "2*(1+3*4)+4";
		double expected = 30;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs56_LBKT_2Plus2_RBKT_Plus_LBKT_2Times2_RBKT_Times_LBKT_1Plus3Times4_RBKT() {
		String expression = "(2+2)+(2*2)*(1+3*4)";
		double expected = 56;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs256_LBKT_2Times_LBKT_2Plus2_RBKT_Times2_RBKT_Pow2() {
		String expression = "(2*(2+2)*2)**2";
		double expected = 256;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs32_LBKT_2Times_LBKT_2Plus_LBKT_3TimesTwo_RBKT_RBKTTimes_LBKT_10Div5_RBKT_RBKT() {
		String expression = "(2*(2+(3*2))*(10/5))";
		double expected = 32;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs32_LBKT_2Times_LBKT_2Plus_LBKT_LBKT_3_RBKT_TimesTwo_RBKT_RBKT_Times_LBKT_10Div5_RBKT_RBKT() {
		String expression = "(2*(2+((3)*2))*(10/5))";
		double expected = 32;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs7_LBKT_LBKT_LBKT_3_RBKT_Times2_RBKT_Plus1_RBKT() {
		String expression = "(((3)*2)+1)";
		double expected = 7;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs7_LBKT_1Plus_LBKT_3Times_LBKT_2_RBKT_RBKT_RBKT() {
		String expression = "(1+(3*(2)))";
		double expected = 7;
		assertEquals(expected, calc.evaluate(expression));
	}
	

	@Test
	void evaluate_ResultIs7_LBKT_1Plus_LBKT_LBKT_3Plus2_RBKT_Times_LBKT_2_RBKT_RBKT_RBKT() {
		String expression = "(1+((3+2)*(2)))";
		double expected = 11;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs7_LBKT_LBKT_LBKT_1_RBKT_RBKT_Plus_LBKT_LBKT_3Plus2_RBKT_Times_LBKT_2_RBKT_RBKT_RBKT() {
		String expression = "(((1))+((3+2)*(2)))";
		double expected = 11;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void getCommonDivisor_ResultIs10_Numerator10_Denominator1000() {
		long numerator = 10;
		long denominator = 1000;
		long expected = 10;
		assertEquals(expected, calc.getCommonDivisor(numerator, denominator));
	}
	
	@Test
	void getCommonDivisor_ResultIs6_Numerator54_Denominator24() {
		long numerator = 54;
		long denominator = 24;
		long expected = 6;
		assertEquals(expected, calc.getCommonDivisor(numerator, denominator));
	}
	
	@Test
	void decimalToFraction_ResultIs13Over8_1Point625() {
		String decimal = "1.625";
		String expected = "13/8";
		assertEquals(expected, calc.decimalToFraction(decimal));
	}
	
	@Test
	void decimalToFraction_ResultIs4Over5_0Point8() {
		String decimal = "0.8";
		String expected = "4/5";
		assertEquals(expected, calc.decimalToFraction(decimal));
	}
	
	@Test
	void decimalToFraction_ResultIsNeg4Over5_Neg0Point8() {
		String decimal = "-0.8";
		String expected = "-4/5";
		assertEquals(expected, calc.decimalToFraction(decimal));
	}

	@Test
	void evaluate_ResultIsNeg16_LBKT_Neg2Times_LBKT_Pov2Plus_LBKT_LBKT_Neg3_RBKT_TimesPov2_RBKT_RBKT_Times_LBKT_Neg10DivPov5_RBKT_RBKT() {
		String expression = "(-2*(+2+((-3)*+2))*(-10/+5))";
		double expected = -16;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void getRemainingBracketCount_ResultIs_0_Neg3_Neg3_0_LBKT_LBKT_LBKT_LBKT_1_RBKT_LBKT_2_RBKT_RBKT_RBKT_RBKT() {
		String leftNum = "((((1)";
		String rightNum = "(2))))";
		int[] expected = {0, 0};
		assertArrayEquals(expected, calc.getRemainingBracketCount(leftNum, rightNum));
	}
	
	@Test
	void getRemainingBracketCount_ResultIs_Neg3_3_3_Neg3_LBKT_1_RBKT_RBKT_RBKT_RBKT_LBKT_LBKT_LBKT_LBKT_2_RBKT() {
		String leftNum = "(1))))";
		String rightNum = "((((2)";
		int[] expected = {-3, -3};
		assertArrayEquals(expected, calc.getRemainingBracketCount(leftNum, rightNum));
	}
	
	@Test
	void sortBracketsAndSymbols_NumberIndex0Is_LBKT_NEG10__NumberArrayIs_LBKT_10_Five() {
		List<String> numbers = new ArrayList<String>(); 
		List<String> symbols = new ArrayList<String>();
		numbers.add("(");
		numbers.add("10");
		numbers.add("5");
		symbols.add("-");
		symbols.add("/");
		calc.sortBracketsAndSymbols(symbols, numbers, 0);
		String[] expectedNumber = {"(-10", "5"};
		assertArrayEquals(expectedNumber, numbers.toArray(new String[0]));
 	}
	
	@Test
	void sortBracketsAndSymbols_NumberSizeIs5__NumbersArray() {
		List<String> numbers = new ArrayList<String>(); 
		List<String> symbols = new ArrayList<String>();
		//Expression = 10+2*(-10+(-5+(1))/1)+(+0)
		//Numbers array = {10, 2, (, 10, (, 5, (1)), 1), (, 0)}
		//Symbols array = {+, *, -, +, -, +, /, +, +
		numbers.add("10");
		numbers.add("2");
		numbers.add("(");
		numbers.add("10");
		numbers.add("(");
		numbers.add("5");
		numbers.add("(1))");
		numbers.add("1)");
		numbers.add("(");
		numbers.add("0");
		
		symbols.add("+");
		symbols.add("*");
		symbols.add("-");
		symbols.add("+");
		symbols.add("-");
		symbols.add("+");
		symbols.add("/");
		symbols.add("+");
		symbols.add("+");
		
		calc.sortBracketsAndSymbols(symbols, numbers, 0);
		String[] expectedNumber = {"10", "2", "(-10", "(-5", "(1))", "1)", "(+0"};
		assertArrayEquals(expectedNumber, numbers.toArray(new String[0]));
 	}
	
	@Test
	void evaluate_ResultIs10_Neg_LBKT_Neg10_RBKT() {
		String expression = "-(-10)";
		double expected = 10;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void evaluate_ResultIs20_Neg_LBKT_Neg10_RBKT_Times2() {
		String expression = "-(-10)*2";
		double expected = 20;
		assertEquals(expected, calc.evaluate(expression));
	}
	
	@Test
	void cleanBrackets_EqualsExpected_whenNumberListIsPassedIn() {
		List<String> numbers = new ArrayList<String>();
		numbers.add("-(-10)");
		numbers.add("2");
		numbers.add("+(((-10))");
		numbers.add("+(-10))");
		String[] expected = {"10.0", "2", "(-10.0", "-10.0)"};
		calc.cleanBrackets(numbers, 0);
		assertArrayEquals(expected, numbers.toArray(new String[0]));
	}
	
	@Test
	void frationalPow_Equals2Point19_baseIs3ExponentIs5Over7() {
		String exponent = "0.71428571428";
		String base = "3";
		double expected = 2.19179986693;
		assertEquals(expected, calc.fractionalPow(base, exponent), 0.001);
	}
}
