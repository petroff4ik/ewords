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

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// ToDo add your GUI initialization code here 
		setContentView(R.layout.mainscreen);
	}

	public void onButtonStart(View view) {
		Intent intent = new Intent(MainScreen.this, MainActivity.class);
		startActivity(intent);
	}

	public void onBQuit(View view) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

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
