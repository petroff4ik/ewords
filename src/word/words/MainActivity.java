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
import android.view.Window;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Vibrator;
import android.content.Context;
import android.app.AlertDialog.Builder;
import android.widget.AutoCompleteTextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends Activity {

	GridView gvMain;
	GVadapter adapter;
	private static final String TAG = "MainActivity";
	WordsData wd;
	TextView we;
	TextView ws;
	MyToast myToast;
	LinearLayout llstar;
	AlertDialog dlg;
	DBConnector db;
	private SoundPool soundPool;
	private int soundWin;
	private int soundLose;
	private int soundApp;
	boolean loaded = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		db = new DBConnector(this);
		wd = new WordsData(db, this);
		if (wd.getPreferenceSound()) {
			// init sound
			this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					loaded = true;
				}
			});

			// Загружаем звуки в память
			soundWin = soundPool.load(this, R.raw.soundwin, 1);
			soundLose = soundPool.load(this, R.raw.soundlose, 1);
			soundApp = soundPool.load(this, R.raw.soundapp, 1);
		}

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
						play_sound("app");
						wd.Restart();
						isNewWorld();
						if (wd.getStaticFlag()) {
							cleanStar();
						}
					}
					play_sound("win");
					setText(wd.getWordEncode(), wd.getWordSrc());
				} else {
					if (wd.getStaticFlag()) {
						reloadStar();
					}
					myToast.showCharToast(wd.getWrongChar());
					play_sound("lose");
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
				if (wd.getStaticFlag()) {
					cleanStar();
				}
				adapter.notifyDataSetChanged();
				return true;
			}
		});


		MenuItem menuItem2 = menu.add("Search");
		menuItem2.setIcon(R.drawable.lens);
		menuItem2.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem _menuItem) {
				AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
				adb.setTitle(R.string.sw);

				LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.search, null);
				adb.setView(view);
				AutoCompleteTextView itemDescriptionView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete);
				itemDescriptionView.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							return true;
						}
						return false;
					}
				});
				ItemAutoTextAdapter adapterSearch = new ItemAutoTextAdapter(MainActivity.this, db, wd.getLangSrc());
				itemDescriptionView.setAdapter(adapterSearch);
				itemDescriptionView.setOnItemClickListener(adapterSearch);
				dlg = adb.create();
				dlg.show();
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
			ImageView star = (ImageView) ltInflater.inflate(R.layout.star,
					null, false);

			star.setTag("star" + i);
			star.setVisibility(star.INVISIBLE);
			llstar.addView(star);
		}
	}

	public void reloadStar() {

		for (int i = 1; i <= wd.getTotalCountStar(); i++) {
			if (i <= wd.getCountStar()) {
				llstar.findViewWithTag("star" + i).setVisibility(
						llstar.findViewWithTag("star" + i).VISIBLE);
			} else {
				llstar.findViewWithTag("star" + i).setVisibility(
						llstar.findViewWithTag("star" + i).INVISIBLE);
			}
		}

	}

	public void cleanStar() {
		for (int i = 1; i <= wd.getTotalCountStar(); i++) {
			llstar.findViewWithTag("star" + i).setVisibility(
					llstar.findViewWithTag("star" + i).INVISIBLE);
		}
	}

	public void play_sound(String type) {
		if (wd.getPreferenceSound()) {
			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = actualVolume / maxVolume;
			// Is the sound loaded already?
			if (type.equals("win")) {
				soundPool.play(soundWin, volume, volume, 1, 0, 1f);
			} else if (type.equals("lose")) {
				soundPool.play(soundLose, volume, volume, 1, 0, 1f);
			} else if (type.equals("app")) {
				soundPool.play(soundApp, volume, volume, 1, 0, 1f);
			}

			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long milliseconds = 100;
			v.vibrate(milliseconds);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		wd.dbClose();
	}
}
