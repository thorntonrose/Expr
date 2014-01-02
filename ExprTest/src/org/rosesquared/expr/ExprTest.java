package org.rosesquared.expr;

import android.app.*;
import android.test.*;
import android.view.inputmethod.*;
import android.widget.*;

/**
 * Expr tests.
 *
 * @author ethorro
 */
public class ExprTest extends ActivityInstrumentationTestCase2<Expr> {
   private Instrumentation instrumentation;
   private Expr expr;
   private EditText inputView;
   private ListView resultsView;
   private ArrayAdapter listAdapter;

   public ExprTest() {
      super(Expr.class.getPackage().getName(), Expr.class);
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      instrumentation = getInstrumentation();
      expr = getActivity();
      inputView = (EditText) expr.findViewById(R.id.input_view);
      resultsView = (ListView) expr.findViewById(R.id.results_view);
      enterExpression();
      listAdapter = (ArrayAdapter) resultsView.getAdapter();
   }

   public void enterExpression() {
      expr.runOnUiThread(new Runnable() {
         @Override
         public void run() {
            inputView.setText("1 + 1");
            inputView.onEditorAction(EditorInfo.IME_ACTION_DONE);
         }
      });

      instrumentation.waitForIdleSync();
   }

   //------------------------------------------------------------------------------------------------------------------

   public void testCalculate() {
      assertEquals("listAdapter.count:", 2, listAdapter.getCount());
      assertEquals("listAdapter.item(0):", "1 + 1 =", listAdapter.getItem(0));
      assertEquals("listAdapter.item(1):", "2.0", listAdapter.getItem(1));
   }

   public void testClear() {
      assertEquals("listAdapter.count:", 2, listAdapter.getCount());

      expr.runOnUiThread(new Runnable() {
         @Override
         public void run() {
            expr.onOptionsItemSelected(expr.getMenu().getItem(0));
         }
      });

      instrumentation.waitForIdleSync();
      assertEquals("listAdapter.count:", 0, listAdapter.getCount());
   }

   public void testResultClick() {
      assertEquals("listAdapter.count:", 2, listAdapter.getCount());

      expr.runOnUiThread(new Runnable() {
         @Override
         public void run() {
            expr.onItemClick(null, null, 1, 1);
         }
      });

      instrumentation.waitForIdleSync();
      assertEquals("inputView.text:", "2.0", inputView.getText().toString());
   }
}
