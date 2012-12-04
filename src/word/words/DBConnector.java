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

public class DBConnector {

	private static final String TAG = "DB";
	// Данные базы данных и таблиц
	private static final String DATABASE_NAME = "ewords.db";
	private static final int DATABASE_VERSION = 15;
	private static final String TABLE_NAME = "words";
	private static final String TABLE_NAME2 = "links";
	// Название столбцов
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_WORD = "word";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_PID = "w1id";
	private static final String COLUMN_TID = "w2id";
	private SQLiteDatabase mDataBase;

	public DBConnector(Context context) {
		// открываем (или создаем и открываем) БД для записи и чтения
		OpenHelper mOpenHelper = new OpenHelper(context);
		mDataBase = mOpenHelper.getWritableDatabase();
	}

	// Класс для создания БД
	private class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String query = "CREATE TABLE " + TABLE_NAME + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY UNIQUE, "
					+ COLUMN_WORD + " TEXT, "
					+ COLUMN_TYPE + " VARCHAR(2),"
					+ COLUMN_STATUS + " VARCHAR(3) default 'no'"
					+ "); ";
			db.execSQL(query);
			query = "CREATE TABLE " + TABLE_NAME2 + " ("
					+ COLUMN_PID + " INTEGER, "
					+ COLUMN_TID + " INTEGER "
					+ "); ";
			db.execSQL(query);
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_ID, 1);
			cv.put(COLUMN_WORD, "get");
			cv.put(COLUMN_TYPE, "en");
			db.insert(TABLE_NAME, null, cv);

			cv.put(COLUMN_ID, 2);
			cv.put(COLUMN_WORD, "получать");
			cv.put(COLUMN_TYPE, "ru");
			db.insert(TABLE_NAME, null, cv);

			cv.put(COLUMN_ID, 3);
			cv.put(COLUMN_WORD, "иметь");
			cv.put(COLUMN_TYPE, "ru");
			db.insert(TABLE_NAME, null, cv);

			ContentValues cv2 = new ContentValues();

			cv2.put(COLUMN_PID, 1);
			cv2.put(COLUMN_TID, 2);
			db.insert(TABLE_NAME2, null, cv2);

			cv2.put(COLUMN_PID, 1);
			cv2.put(COLUMN_TID, 3);
			db.insert(TABLE_NAME2, null, cv2);

			// second words
			cv.put(COLUMN_ID, 4);
			cv.put(COLUMN_WORD, "give");
			cv.put(COLUMN_TYPE, "en");
			db.insert(TABLE_NAME, null, cv);

			cv.put(COLUMN_ID, 5);
			cv.put(COLUMN_WORD, "отдавать");
			cv.put(COLUMN_TYPE, "ru");
			db.insert(TABLE_NAME, null, cv);

			cv.put(COLUMN_ID, 6);
			cv.put(COLUMN_WORD, "давать");
			cv.put(COLUMN_TYPE, "ru");
			db.insert(TABLE_NAME, null, cv);

			cv2.put(COLUMN_PID, 4);
			cv2.put(COLUMN_TID, 5);
			db.insert(TABLE_NAME2, null, cv2);

			cv2.put(COLUMN_PID, 4);
			cv2.put(COLUMN_TID, 6);
			db.insert(TABLE_NAME2, null, cv2);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
			Log.v(TAG, "onUpgrade");
			onCreate(db);
		}
	}

	// Метод выборки одной записи
	public String getWord(int id) {// TODO make check return value
		Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, COLUMN_ID);
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
}
