/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.view.Gravity;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

/**
 *
 * @author petroff
 */
public class MyToast {

	TextView tt;
	TextView ttw;
	TextView ttc;
	Toast toast;
	Toast toastWin;
	Toast toastCng;
	Activity activity;

	MyToast(Activity activity) {
		this.activity = activity;
		init();
	}

	private void init() {
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) activity.findViewById(R.id.llToast));
		tt = (TextView) layout.findViewById(R.id.tvTitleToast);
		toast = new Toast(activity.getApplicationContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);


		layout = inflater.inflate(R.layout.toastwin,
				(ViewGroup) activity.findViewById(R.id.llToast));
		ttw = (TextView) layout.findViewById(R.id.tvTitleToast);
		toastWin = new Toast(activity.getApplicationContext());
		toastWin.setDuration(Toast.LENGTH_SHORT);
		toastWin.setGravity(Gravity.CENTER, 0, 0);
		toastWin.setView(layout);
		
		layout = inflater.inflate(R.layout.toastcng,
				(ViewGroup) activity.findViewById(R.id.llToast));
		ttc= (TextView) layout.findViewById(R.id.tvTitleToast);
		toastCng = new Toast(activity.getApplicationContext());
		toastCng.setDuration(Toast.LENGTH_SHORT);
		toastCng.setGravity(Gravity.CENTER, 0, 0);
		toastCng.setView(layout);
	}

	public void showCharToast(String chr) {
		tt.setText(chr);
		toast.show();
	}

	public void showRightWord(String str) {
		ttw.setText(str);
		toastWin.show();
	}
	
	public void showCng() {
		ttc.setText(R.string.ended_words);
		toastCng.show();
	}
}
