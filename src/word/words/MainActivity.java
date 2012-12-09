package word.words;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class MainActivity extends Activity {

	GridView gvMain;
	ArrayAdapter<String> adapter;
	private static final String TAG = "MainActivity";
	String c = "";
	WordsData wd;
	TextView we;
	TextView ws;
	TextView tt;
	Toast toast;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DBConnector db = new DBConnector(this);
		wd = new WordsData(db, this);
		if (wd.getAvailableWord() == false) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.words_ended)
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									wd.resetWords();
									dialog.cancel();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									finish();

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) findViewById(R.id.llToast));
		tt = (TextView) layout.findViewById(R.id.tvTitleToast);
		toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);

		we = (TextView) findViewById(R.id.worldEncode);
		ws = (TextView) findViewById(R.id.worldSrc);
		String WordEncode = wd.getWordEncode();
		String WordSrc = wd.getWordSrc();
		we.setText(WordEncode);
		ws.setText(WordSrc);
		adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.tvText,
				wd.getData());
		gvMain = (GridView) findViewById(R.id.gvMain);
		gvMain.setAdapter(adapter);
		gvMain.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TextView tv = (TextView) adapter.getView(position, v,
				// parent);
				LinearLayout ll = (LinearLayout) adapter.getView(position, v,
						parent);
				ll.setBackgroundColor(0x00000000);
				TextView tv = (TextView) ll.getChildAt(0);
				tv.setText("");
				c = adapter.getItem(position);
				if (wd.checkChar(c)) {
					we.setText(wd.getWordEncode());
					ws.setText(wd.getWordSrc());
				} else {

					// Toast toast = Toast.makeText(MainActivity.this, "",
					// Toast.LENGTH_SHORT);
					tt.setText(c);
					toast.show();
				}

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
