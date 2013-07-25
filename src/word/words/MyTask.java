/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.xmlpull.v1.XmlPullParser;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 *
 * @author petroff
 */
class MyTask extends AsyncTask<Void, Integer, Void> {

	private static SQLiteDatabase db;
	Context context;
	ProgressDialog mProgressDialog;
	static final int IDD__HORIZONTAL_PROGRESS = 0;
	static final int IDD_WHEEL_PROGRESS = 1;
	private static final String TAG = "MyTask";
	private int dMax = 10000;

	MyTask(Context context, SQLiteDatabase db) {
		super();
		this.context = context;
		this.db = db;
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case IDD__HORIZONTAL_PROGRESS:
				mProgressDialog = new ProgressDialog(
						context);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // устанавливаем стиль
				mProgressDialog.setMessage("First launch. Wait...");  // задаем текст
				mProgressDialog.setCancelable(false);
				return mProgressDialog;

			case IDD_WHEEL_PROGRESS:
				mProgressDialog = new ProgressDialog(
						context);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setMessage("First launch. Wait...");
				mProgressDialog.setCancelable(false);
				return mProgressDialog;

			default:
				return null;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Activity activity = (Activity) context;
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		onCreateDialog(0);
		mProgressDialog.setMax(dMax);
		mProgressDialog.show();
		mProgressDialog.setProgress(0);
	}

	protected Void doInBackground(Void... params) {

		try {
			XmlPullParser parser = context.getResources().getXml(R.xml.dictionary);
			ContentValues word = new ContentValues();
			ContentValues link = new ContentValues();
			Integer i = 0;
			db.beginTransaction();
			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlPullParser.START_TAG
						&& parser.getName().equals("word")) {
					word.put("_id", parser.getAttributeValue(3));
					word.put("word", parser.getAttributeValue(2));
					word.put("type", parser.getAttributeValue(0));
					db.insert("words", null, word);
				} else if (parser.getEventType() == XmlPullParser.START_TAG
						&& parser.getName().equals("link")) {
					link.put("w1id", parser.getAttributeValue(0));
					link.put("w2id", parser.getAttributeValue(1));
					//db.insert("links", null, link);
					link.put("w1id", parser.getAttributeValue(1));
					link.put("w2id", parser.getAttributeValue(0));
					db.insert("links", null, link);
				}

				i++;
				publishProgress(i);

				parser.next();
			}
			db.setTransactionSuccessful();
			db.endTransaction();

		} catch (Throwable t) {
			Toast.makeText(context,
					"Errors upload words: " + t.toString(), 4000).show();
		}




		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mProgressDialog.setProgress(dMax);
		mProgressDialog.cancel();
		Activity activity = (Activity) context;
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor ed = sp.edit();
		ed.putBoolean("FL", true);
		ed.commit();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate();
		if (!mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
		mProgressDialog.setProgress(progress[0]);

	}
}
