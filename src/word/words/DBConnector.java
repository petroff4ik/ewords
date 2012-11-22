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

public class DBConnector {

	// Данные базы данных и таблиц
	private static final String DATABASE_NAME = "ewords.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "words";
	private static final String TABLE_NAME2 = "links";
	// Название столбцов
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_WORD = "word";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_PID = "pid";
	private static final String COLUMN_TID = "tid";
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
					+ COLUMN_TYPE + " VARCHAR(2)"
					+ "); ";
			db.execSQL(query);
			query = "CREATE TABLE " + TABLE_NAME2 + " ("
					+ COLUMN_PID + " INTEGER, "
					+ COLUMN_TID + " INTEGER "
					+ "); ";
			db.execSQL(query);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
			onCreate(db);
		}
	}

	// Метод выборки одной записи
	public Cursor select_words(long id) {
		Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, COLUMN_ID);
		return mCursor;
	}

	public long insert(String table_name,ContentValues cv) {
		return mDataBase.insert(table_name, null, cv);
	}
}
