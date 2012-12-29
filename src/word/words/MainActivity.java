package word.words;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class MainActivity extends Activity {

	GridView gvMain;
	GVadapter adapter;
	private static final String TAG = "MainActivity";
	WordsData wd;
	TextView we;
	TextView ws;
	MyToast myToast;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DBConnector db = new DBConnector(this);
		wd = new WordsData(db, this);

		if (wd.getAvailableWord() == false) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.words_ended).setCancelable(false).setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int id) {
							wd.resetWords();
							wd.Restart();
							adapter.notifyDataSetChanged();
							setText(wd.getWordEncode(), wd.getWordSrc());
							dialog.cancel();
						}
					}).setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int id) {
							finish();

						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}

		myToast = new MyToast(this);
		we = (TextView) findViewById(R.id.worldEncode);
		ws = (TextView) findViewById(R.id.worldSrc);
		String WordEncode = wd.getWordEncode();
		String WordSrc = wd.getWordSrc();
		setText(WordEncode, WordSrc);
		adapter = new GVadapter(this, wd.getData(), wd);
		gvMain = (GridView) findViewById(R.id.gvMain);
		gvMain.setAdapter(adapter);
		gvMain.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				if (wd.checkChar(position)) {
					if (wd.getNewWords()) {
						wd.Restart();
						isNewWorld();
					}
					setText(wd.getWordEncode(), wd.getWordSrc());
				} else {
					myToast.showCharToast(wd.getWrongChar());
				}
				adapter.notifyDataSetChanged();

			}
		});

	}

	protected void setText(String WordEncode, String WordSrc) {
		we.setText(WordEncode);
		ws.setText(WordSrc);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("wd", wd);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		wd = (WordsData) savedInstanceState.getSerializable("wd");
		setText(wd.getWordEncode(), wd.getWordSrc());
		adapter = new GVadapter(this, wd.getData(), wd);
		gvMain.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuItem menuItem = menu.add("Next word");
		menuItem.setIcon(R.drawable.ra);
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem _menuItem) {
				wd.nextWord();
				isNewWorld();
				setText(wd.getWordEncode(), wd.getWordSrc());
				adapter.notifyDataSetChanged();
				return true;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	public void isNewWorld() {
		if (wd.getAvailableWord() == false) {
			Intent intent = new Intent(MainActivity.this, MainScreen.class);
			myToast.showCng();
			startActivity(intent);
		} else {
			myToast.showRightWord(wd.getWordTarget());
		}
	}
}
