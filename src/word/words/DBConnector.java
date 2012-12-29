/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class DBConnector implements Serializable {

	private static final String TAG = "DB";
	private static final String DATABASE_NAME = "ewords.db";
	private static final int DATABASE_VERSION = 31;
	private static final String TABLE_NAME = "words";
	private static final String TABLE_NAME2 = "links";
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_WORD = "word";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_PID = "w1id";
	private static final String COLUMN_TID = "w2id";
	private static SQLiteDatabase mDataBase;

	public DBConnector(Context context) {

		OpenHelper mOpenHelper = new OpenHelper(context);
		mDataBase = mOpenHelper.getWritableDatabase();
	}

	// /data/data/word.words/databases/ewords.db
	private class OpenHelper extends SQLiteOpenHelper {

		Context context;

		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			MyTask mt = new MyTask(context,db);
			mt.execute();
			String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
					+ " INTEGER PRIMARY KEY UNIQUE, " + COLUMN_WORD + " TEXT, "
					+ COLUMN_TYPE + " VARCHAR(2)," + COLUMN_STATUS
					+ " VARCHAR(3) default 'no'" + "); ";
			db.execSQL(query);
			query = "CREATE TABLE " + TABLE_NAME2 + " (" + COLUMN_PID
					+ " INTEGER, " + COLUMN_TID + " INTEGER " + "); ";
			db.execSQL(query);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
			Log.v(TAG, "onUpgrade");
			onCreate(db);
		}
	}


	public String getWord(int id) {// TODO make check return value
		Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?",
				new String[]{String.valueOf(id)}, null, null, COLUMN_ID);
		mCursor.moveToFirst();
		String word = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_WORD));
		mCursor.close();
		return word;
	}

	public Map getWordStatusNo(String lang) {// TODO make check return value
		String sql = "select wsrc.word as wordSrc, wsrc._id as wordSrcId, words.* from words as wsrc, words, links where (wsrc._id = links.w1id and words._id = links.w2id) and wsrc.status = ? and wsrc.type = ?";
		Cursor mCursor = mDataBase.rawQuery(sql, new String[]{"no", lang});
		Map<String, String> hashmap = new HashMap<String, String>();
		if (mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			String wordSrc = mCursor.getString(mCursor.getColumnIndexOrThrow("wordSrc"));
			String word = mCursor.getString(mCursor.getColumnIndexOrThrow("word"));
			hashmap.put("wordSrc", wordSrc);
			hashmap.put("word", word);
		}
		mCursor.close();
		return hashmap;
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		Cursor res = mDataBase.rawQuery(sql, selectionArgs);
		res.moveToFirst();

		return res;
	}

	public Cursor rawQuery(String sql) {
		Cursor res = mDataBase.rawQuery(sql, null);
		res.moveToFirst();

		return res;
	}

	public long insert(String table_name, ContentValues cv) {
		long res = mDataBase.insert(table_name, null, cv);
		return res;
	}

	public Integer getCountWords(String langSrc) {
		Integer res = 0;
		String sql = "SELECT COUNT() AS C FROM WORDS WHERE type = ?;";
		Cursor resDb = rawQuery(sql);
		try {
			res = resDb.getInt(resDb.getColumnIndexOrThrow("C"));
		} catch (Exception e) {
			Log.v(TAG, "error");
		}
		resDb.close();
		return res;
	}

	public void resetWords(String langSrc) {
		ContentValues s = new ContentValues();
		s.put(COLUMN_STATUS, "no");
		mDataBase.update(TABLE_NAME, s, " type = ? ", new String[]{langSrc});
	}
	

	public Map getWordCheck(String lang, String wordSrcSearch, String like) {// TODO
		// make
		// check
		// return
		// value
		String sql = "select wsrc.word as wordSrc, wsrc._id as wordSrcId, words.* from words as wsrc, words, links where (wsrc._id = links.w1id and words._id = links.w2id) and wsrc.status = ? and wsrc.type = ?"
				+ " AND  wsrc.word = ? AND " + like;
		Cursor mCursor = mDataBase.rawQuery(sql, new String[]{"no", lang,
					wordSrcSearch});
		Map<String, String> hashmap = new HashMap<String, String>();
		if (mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			String wordSrc = mCursor.getString(mCursor.getColumnIndexOrThrow("wordSrc"));
			String word = mCursor.getString(mCursor.getColumnIndexOrThrow("word"));
			hashmap.put("wordSrc", wordSrc);
			hashmap.put("word", word);
		}
		mCursor.close();
		return hashmap;
	}

	public int update(String str) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_STATUS, "yes");
		return mDataBase.update(TABLE_NAME, cv, COLUMN_WORD + " = ?", new String[]{str});
	}
}
