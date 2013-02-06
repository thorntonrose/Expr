package org.rosesquared.calcpad;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;

public class Help extends Activity {
   public static String TAG = Help.class.getName();

   @Override
   public void onCreate(Bundle savedInstanceState) {
      Log.d(TAG, "onCreate");
      super.onCreate(savedInstanceState);
      setContentView(R.layout.help);
      TextView helpView = (TextView) findViewById(R.id.help_view);
      helpView.setText(
         getString(R.string.app_name) + " is a calculator that lets you do calculations by entering equations.\n" +
         "\n" +
         "Operators:\n" +
         "\n" +
         "* = multiply\n" +
         "/ = divide\n" +
         "+ = add\n" +
         "- = subtract or negate\n" +
         "^ = raise to power\n" +
         "% = find remainder (modulo)\n" +
         "\n" +
         "Order of Operations:\n" +
         "\n" +
         "The order of operations is power; multiply, divide, and modulo; add and subtract. Parentheses " +
         "can be used for grouping.\n" +
         "\n" +
         "Special Symbols:\n" +
         "\n" +
         "The @ symbol will be replaced with the value of the last calculation. The value will be 0 if " +
         "there has not been a calculation or the last calculation had no result.\n" +
         "\n" +
         "Examples:\n" +
         "\n" +
         "1 + 1\n" +
         "3 * -5\n" +
         "(3 * 5) - (4 / 2)\n" +
         "2^3 - 1\n" +
         "@ + 1\n" +
         "2 * @"
      );
   }
}
