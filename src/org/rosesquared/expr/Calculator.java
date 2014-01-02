package org.rosesquared.expr;

import java.io.*;

public class Calculator {
   public static final String TAG = Calculator.class.getName();
   public boolean verbose = false;
   private PushbackReader reader;
   private String token = "";
   private double currValue = 0;
   private String equation = "";

   public String getEquation() {
      return equation;
   }

   public double calculate(String input) throws Exception {
      equation = input.trim().replaceAll("@", "" + currValue);
      reader = new PushbackReader(new StringReader(equation));
      nextToken();

      if (token.equals("END")) {
         currValue = 0;
      } else {
         currValue = expression();
         if (! token.equals("END")) { throw new ArithmeticException("expected END; got '" + token + "'"); }
      }

      return currValue;
   }

   public void nextToken() throws Exception {
      if (verbose) { System.out.println("> nextToken..."); }
      token = "";

      while (true) {
         char c = getChar();

         // end of input
         if (c == 0) {
            token = "END";
            break;
         }

         // number token
         if (Character.isDigit(c) || (c == '.')) {
            reader.unread(c);
            readNumberToken();
            break;
         }

         // other token
         if (c != ' ') {
            token += c;
            break;
         }
      }

      if (verbose) { System.out.println("> nextToken: token: " + token); }
   }

   public char getChar() throws Exception {
      int c = reader.read();
      return (c == -1) ? 0 : (char) c;
   }

   public void readNumberToken() throws Exception {
      token = "NUM:";

      while (true) {
         char c = getChar();

         if ((c == 0) || (c == ' ') || (c == ')') || isOperator(c)) {
            reader.unread(c);
            break;
         }

         token += c;
      }
   }

   public boolean isOperator(char c) {
      return "*/+-^%".indexOf(c) > -1;
   }

   public double expression() throws Exception {
      if (verbose) { System.out.println("> expression..."); }
      double value = term();

      while (true) {
         if (token.equals("+")) {
            nextToken();
            value += term();
         } else if (token.equals("-")) {
            nextToken();
            value -= term();
         } else {
            break;
         }
      }

      if (verbose) { System.out.println("> expression: value: " + value); }
      return value;
   }

   public double term() throws Exception {
      if (verbose) { System.out.println("term..."); }
      double value = exponent();

      while (true) {
         if (token.equals("*")) {
            nextToken();
            value *= exponent();
         } else if (token.equals("/")) {
            nextToken();
            value /= exponent();
         } else if (token.equals("%")) {
            nextToken();
            value %= exponent();
         } else {
            break;
         }
      }

      if (verbose) { System.out.println("> term: value: " + value); }
      return value;
   }

   public double exponent() throws Exception {
      if (verbose) { System.out.println("exponent..."); }
      double value = primary();

      while (true) {
         if (token.equals("^")) {
            nextToken();
            value = Math.pow(value, primary());
         } else {
            break;
         }
      }

      if (verbose) { System.out.println("> term: value: " + value); }
      return value;
   }

   public double primary() throws Exception {
      if (verbose) { System.out.println("> primary..."); }
      double value = 0;

      if (token.startsWith("NUM:")) {
         value = getNumberValue(token);
         nextToken();
      //} else if (token.equals("@")) {
      //   value = currValue;
      //   nextToken();
      } else if (token.equals("-")) {
         nextToken();
         value = -primary();
      } else if (token.equals("(")) {
         nextToken();
         value = expression();
         if (! token.equals(")")) { throw new ArithmeticException("')' expected; got '" + token + "'"); }
         nextToken();
      } else {
         throw new ArithmeticException("expected NUMBER, '-', or '('; got '" + token + "'");
      }

      if (verbose) { System.out.println("> primary: value: " + value); }

      return value;
   }

   public double getNumberValue(String token) {
      String[] parts = token.split(":");

      try {
         return Double.parseDouble(parts[1]);
      } catch(NumberFormatException e) {
         throw new ArithmeticException("invalid number: " + parts[1]);
      }
   }
}
