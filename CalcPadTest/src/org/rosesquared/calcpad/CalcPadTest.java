package org.rosesquared.calcpad;

import android.app.*;
import android.test.*;
import android.view.inputmethod.*;
import android.widget.*;

/**
 * CalcPad tests.
 * 
 * @author ethorro
 */
public class CalcPadTest extends ActivityInstrumentationTestCase2<CalcPad> {
   private Instrumentation instrumentation;
	private CalcPad calcPad;
   private EditText inputView;
   private ListView resultsView;

   public CalcPadTest() {
      super(CalcPad.class.getPackage().getName(), CalcPad.class);
   }
   
   @Override
   protected void setUp() throws Exception {
      super.setUp();
      instrumentation = getInstrumentation();
      calcPad = getActivity();
      inputView = (EditText) calcPad.findViewById(R.id.input_view);
      resultsView = (ListView) calcPad.findViewById(R.id.results_view);
   }

   public void enterEquation() {
      calcPad.runOnUiThread(new Runnable() {
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
      enterEquation();
      ArrayAdapter listAdapter = (ArrayAdapter) resultsView.getAdapter();
      assertEquals("listAdapter.count:", 2, listAdapter.getCount());
      assertEquals("listAdapter.item(0):", "1 + 1 =", listAdapter.getItem(0));
      assertEquals("listAdapter.item(1):", "2.0", listAdapter.getItem(1));
   }

   public void testClear() {
      enterEquation();
      ArrayAdapter listAdapter = (ArrayAdapter) resultsView.getAdapter();
      assertEquals("listAdapter.count:", 2, listAdapter.getCount());

      calcPad.onOptionsItemSelected(calcPad.getMenu().getItem(0));
      assertEquals("listAdapter.count:", 0, listAdapter.getCount());
   }

   public void testResultClick() {
      enterEquation();
      ArrayAdapter listAdapter = (ArrayAdapter) resultsView.getAdapter();
      assertEquals("listAdapter.count:", 2, listAdapter.getCount());
      
      calcPad.runOnUiThread(new Runnable() {
         @Override
         public void run() {
            calcPad.onItemClick(null, null, 1, 1);
         }
      });

      instrumentation.waitForIdleSync();
      assertEquals("inputView.text:", "2.0", inputView.getText().toString());
   }
}
