package word.words;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.content.res.Configuration;
import android.util.Log;

public class MainActivity extends Activity {

	GridView gvMain;
	ArrayAdapter<String> adapter;
	private static final String TAG = "MainActivity";
	String c = "";
	WordsData wd;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DBConnector db = new DBConnector(this);
		wd = new WordsData(db);

		adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.tvText, wd.getData());
		gvMain = (GridView) findViewById(R.id.gvMain);
		gvMain.setAdapter(adapter);
		gvMain.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// TextView tv = (TextView) adapter.getView(position, v, parent);
				LinearLayout ll = (LinearLayout) adapter.getView(position, v, parent);
				ll.setBackgroundColor(0x00000000);
				TextView tv = (TextView) ll.getChildAt(0);
				tv.setText("");
				c = adapter.getItem(position);
				Toast.makeText(MainActivity.this, "" + c, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Проверяем ориентацию экрана
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
			Log.v(TAG, c);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.v(TAG, c);
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}
}
