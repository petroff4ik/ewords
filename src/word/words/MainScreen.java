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

/**
 *
 * @author petroff
 */
public class MainScreen extends Activity {

	private static final String TAG = "MainScreen";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.mainscreen);
		DBConnector db = new DBConnector(this);

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
