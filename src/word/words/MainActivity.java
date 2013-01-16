package word.words;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.ImageView;
import 	android.view.Window;

public class MainActivity extends Activity {

	GridView gvMain;
	GVadapter adapter;
	private static final String TAG = "MainActivity";
	WordsData wd;
	TextView we;
	TextView ws;
	MyToast myToast;
	LinearLayout llstar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
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

		llstar = (LinearLayout) findViewById(R.id.star);
		if (wd.getStaticFlag()) {
			initStar();
			llstar.setVisibility(llstar.VISIBLE);
		} else {
			llstar.setVisibility(llstar.GONE);
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
					if (wd.getStaticFlag()) {
						reloadStar();
					}
					if (wd.getNewWords()) {
						wd.Restart();
						isNewWorld();
						if (wd.getStaticFlag()) {
							cleanStar();
						}
					}
					setText(wd.getWordEncode(), wd.getWordSrc());
				} else {
					if (wd.getStaticFlag()) {
						reloadStar();
					}
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
				cleanStar();
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
			myToast.showRightWord(wd.getWordPrev());
		}
	}

	public void initStar() {
		LayoutInflater ltInflater = getLayoutInflater();
		for (int i = 1; i <= wd.getTotalCountStar(); i++) {
			ImageView star = (ImageView) ltInflater.inflate(R.layout.star, null, false);

			star.setTag("star" + i);
			star.setVisibility(star.INVISIBLE);
			llstar.addView(star);
		}
	}

	public void reloadStar() {

		for (int i = 1; i <= wd.getTotalCountStar(); i++) {
			if (i <= wd.getCountStar()) {
				llstar.findViewWithTag("star" + i).setVisibility(llstar.findViewWithTag("star" + i).VISIBLE);
			} else {
				llstar.findViewWithTag("star" + i).setVisibility(llstar.findViewWithTag("star" + i).INVISIBLE);
			}
		}

	}

	public void cleanStar() {
		for (int i = 1; i <= wd.getTotalCountStar(); i++) {
			llstar.findViewWithTag("star" + i).setVisibility(llstar.findViewWithTag("star" + i).INVISIBLE);
		}
	}
}
