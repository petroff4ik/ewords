/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import 	android.view.Window;

/**
 *
 * @author petroff
 */
public class MainScreen extends Activity {

	private static final String TAG = "MainScreen";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mainscreen);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		DBConnector db = new DBConnector(this);
		show_score();
	}

	public void onResume() {
		super.onResume();
		show_score();
	}

	public void show_score() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean st = sp.getBoolean("ST", false);
		LinearLayout ll = (LinearLayout) findViewById(R.id.you_score_l);
		if (st) {
			TextView tv = (TextView) findViewById(R.id.you_score_d);
			Integer score = sp.getInt("SCORE", 0);
			tv.setText(score.toString());
			ll.setVisibility(ll.VISIBLE);
		} else {
			ll.setVisibility(ll.INVISIBLE);
		}
	}

	public void onButtonStart(View view) {
		Intent intent = new Intent(MainScreen.this, MainActivity.class);
		startActivity(intent);
	}

	public void onBQuit(View view) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.qm).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void onBSetting(View view) {
		Intent intent = new Intent(this, prf.class);
		startActivity(intent);
	}
}
