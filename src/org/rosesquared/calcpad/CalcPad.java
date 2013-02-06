package org.rosesquared.calcpad;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

public class CalcPad extends Activity implements TextView.OnEditorActionListener, ListView.OnItemClickListener,
      Thread.UncaughtExceptionHandler {
   public static final String TAG = CalcPad.class.getName();
   private EditText inputView;
   private ListView resultsView;
   private ArrayAdapter<String> listAdapter;
   private Calculator calculator = new Calculator();
   private Menu menu;

   public Menu getMenu() {
      return menu;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      Log.d(TAG, "onCreate");
      super.onCreate(savedInstanceState);
      Thread.currentThread().setUncaughtExceptionHandler(this);
      setContentView(R.layout.main);

      inputView = (EditText) findViewById(R.id.input_view);
      inputView.setOnEditorActionListener(this);

      listAdapter = new ArrayAdapter<String>(this, R.layout.row);
      listAdapter.setNotifyOnChange(true);

      resultsView = (ListView) this.findViewById(R.id.results_view);
      resultsView.setAdapter(listAdapter);
      resultsView.setOnItemClickListener(this);
   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
      Log.d(TAG, "onCreateOptionsMenu");
      getMenuInflater().inflate(R.menu.main, menu);
      this.menu = menu;
      return true;
	}

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      Log.d(TAG, "onOptionsItemSelected: itemId: " + item.getItemId());

      switch (item.getItemId()) {
         case R.id.menu_clear:
            Log.d(TAG, "onOptionsItemSelected: menu_clear");
            clear();
            break;

         case R.id.menu_help:
            Log.d(TAG, "onOptionsItemSelected: menu_help");
            startActivity(new Intent(Intent.ACTION_VIEW, null, this, Help.class));
            break;

         case R.id.menu_about:
            Log.d(TAG, "onOptionsItemSelected: menu_about");
            Messages.about(this);
            break;
      }

      return super.onOptionsItemSelected(item);
   }
   
   @Override
   public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      Log.d(TAG, "onEditorAction: actionId: " + actionId + ", keyEvent: " + keyEvent);

      if (actionId == EditorInfo.IME_ACTION_DONE) {
         Log.d(TAG, "onEditorAction: IME_ACTION_DONE");
         String input = textView.getText().toString().trim();

         if (input.length() > 0) {
            String result = "";

            try {
               result += calculator.calculate(input);
            } catch(Exception e) {
               result = "no result (" + e.getMessage() + ")";
            }

            if (listAdapter.getCount() > 0) {
               listAdapter.add("");
            }

            listAdapter.add(input + " =");
            if (input.indexOf("@") > -1) { listAdapter.add(calculator.getEquation() + " ="); }
            listAdapter.add(result);
            listAdapter.notifyDataSetChanged();

            inputView.endBatchEdit();
            inputView.setText("");
         }
      }

      return false;
   }

   @Override
   public void onItemClick(AdapterView adapterView, View view, int position, long id) {
      Log.d(TAG, "onItemClick: position: " + position + ", id: " + id);
      String item = listAdapter.getItem(position).trim();
      if (item.endsWith(" =")) { item = item.substring(0, item.length() - 2); }
      if (! item.startsWith("no result")) { inputView.append(item); }
   }

   @Override
   public void uncaughtException(Thread thread, Throwable t) {
      Log.e(TAG, "uncaughtException: " + t.toString(), t);
      Messages.error(this, t);
   }
   
   public void clear() {
      listAdapter.clear();
      listAdapter.notifyDataSetChanged();
   }
}
