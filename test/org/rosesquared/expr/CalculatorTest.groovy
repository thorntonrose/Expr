package org.rosesquared.expr

/**
 * Calculator tests.
 *
 * @author ethorro
 */
class CalculatorTest extends GroovyTestCase {
   def calculator

   protected void setUp() {
      super.setUp()
      calculator = new Calculator()
   }

   def calculate(input) {
      println "> '$input' ..."
      double value = calculator.calculate(input)
      println "> $value"
      return value
   }

   void assertValue(expectedValue, input) {
      def value = calculate(input)
      assertEquals(expectedValue, value)
   }

   //------------------------------------------------------------------------------------------------------------------

   void testCalculate_EmptyInput() {
      println "${name}..."
      assertValue(0.0, "")
   }

   void testCalculate_SingleNumbers() {
      println "${name}..."
      assertValue(0.0, "0")
      assertValue(0.1, ".1")
      assertValue(1.0, "1.0")
   }

   void testCalculate_UnaryNegation() {
      println "${name}..."
      assertValue(-1.0, "-1")
   }

   void testCalculate_Parentheses() {
      println "${name}..."
      assertValue(1.0, "(1)")
   }

   void testCalculate_Power() {
      println "${name}..."
      assertValue(8.0, "2^3")
      assertValue(8.0, "2^(1 + 2)")
      assertValue(8.0, "(1 + 1)^3")
      assertValue(8.0, "(1 + 1)^(1 + 2)")
   }

   void testCalculate_Multiply() {
      println "${name}..."
      assertValue(2.0, "1*2")
      assertValue(-2.0, "1 * -2")
   }

   void testCalculate_Divide() {
      println "${name}..."
      assertValue(1.0, "2 / 2")
      assertValue(-1.0, "-2 / 2")
   }

   void testCalculate_Modulo() {
      println "${name}..."
      assertValue(0.0, "2 % 2")
      assertValue(1.0, "3 % 2")
   }

   void testCalculate_Add() {
      println "${name}..."
      assertValue(2.0, "1 + 1")
   }

   void testCalculate_Subtract() {
      println "${name}..."
      assertValue(0.0, "1 - 1")
   }

   void testCalculate_Precedence() {
      println "${name}..."
      assertValue(9.0, "6 * 3 / 2")
      assertValue(4.0, "6 / 3 * 2")
      assertValue(8.0, "5 + 4 - 1")
      assertValue(2.0, "5 - 4 + 1")
      assertValue(10.0, "5 + 4 * 3 / 2 - 1")
      assertValue(10.0, "4 * 3 / 2 + 5 - 1")
      assertValue(8.0, "5 + 6 / 3 * 2 - 1")
      assertValue(8.0, "5 + 6 / 3 * 2 - 1")
      assertValue(5.0, "(5 + (6 / (3 * 2))) - 1")
      assertValue(9.0, "2^3 + 1")
      assertValue(9.0, "1 + 2^3")
      assertValue(0.0, "1 * 2 % 2")
      assertValue(2.0, "1 % 2 * 2")
   }

   void testCalculate_CurrentValue() {
      println "${name}..."
      calculator.calculate("1 + 1");
      assertValue(3.0, "@ + 1");
      assertValue(4.0, "1 + @");
   }

   void testCalculate_UnbalancedParentheses() {
      println "${name}..."

      try {
         calculate("(1 + 1")
         fail("ArithmeticException not thrown")
      } catch(ArithmeticException e) {
         println "> ${e.message}"
      }
   }

   void testCalculate_InvalidNumber() {
      println "${name}..."

      try {
         calculate("1..")
         fail("ArithmeticException not thrown")
      } catch(ArithmeticException e) {
         println "> ${e.message}"
         assertTrue(e.getMessage().startsWith("invalid number"))
      }

      try {
         calculate("1a")
         fail("'1a': ArithmeticException not thrown")
      } catch(ArithmeticException e) {
         println "> ${e.message}"
      }
   }

   void testCalculate_UnexpectedToken() {
      println "${name}..."

      try {
         calculate("1)")
         fail("ArithmeticException not thrown")
      } catch(ArithmeticException e) {
         println "> ${e.message}"
         assertTrue(e.getMessage().startsWith("expected END"))
      }

      try {
         calculate("?")
         fail("ArithmeticException not thrown")
      } catch(ArithmeticException e) {
         println "> ${e.message}"
         assertTrue(e.getMessage().startsWith("expected NUMBER, '-', or '('"))
      }

      try {
         calculate("5++")
         fail("ArithmeticException not thrown")
      } catch(ArithmeticException e) {
         println "> ${e.message}"
         assertTrue(e.getMessage().startsWith("expected NUMBER, '-', or '('"))
      }
   }
}
