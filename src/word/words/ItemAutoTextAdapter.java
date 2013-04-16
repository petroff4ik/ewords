/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.util.Log;
import android.app.AlertDialog;

/**
 *
 * @author petroff
 */
class ItemAutoTextAdapter extends CursorAdapter implements android.widget.AdapterView.OnItemClickListener {

	private DBConnector DBConnector;
	private String Lang;
	AlertDialog ad;
	MainActivity ma;

	/**
	 * Constructor. Note that no cursor is needed when we create the
	 * adapter. Instead, cursors are created on demand when completions are
	 * needed for the field. (see
	 * {@link ItemAutoTextAdapter#runQueryOnBackgroundThread(CharSequence)}.)
	 *
	 * @param dbHelper
	 *            The AutoCompleteDbAdapter in use by the outer class
	 *            object.
	 */
	public ItemAutoTextAdapter(MainActivity ma, DBConnector DBConnector, String lang) {
		// Call the CursorAdapter constructor with a null Cursor.
		super(ma, null);
		this.DBConnector = DBConnector;
		this.Lang = lang;
		this.ma = ma;
	}

	/**
	 * Invoked by the AutoCompleteTextView field to get completions for the
	 * current input.
	 *
	 * NOTE: If this method either throws an exception or returns null, the
	 * Filter class that invokes it will log an error with the traceback,
	 * but otherwise ignore the problem. No choice list will be displayed.
	 * Watch those error logs!
	 *
	 * @param constraint
	 *            The input entered thus far. The resulting query will
	 *            search for Items whose description begins with this string.
	 * @return A Cursor that is positioned to the first row (if one exists)
	 *         and managed by the activity.
	 */
	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (getFilterQueryProvider() != null) {
			return getFilterQueryProvider().runQuery(constraint);
		}

		Cursor cursor = DBConnector.searchByWordLang(
				(constraint != null ? constraint.toString() : "@@@@"), Lang);

		return cursor;
	}

	/**
	 * Called by the AutoCompleteTextView field to get the text that will be
	 * entered in the field after a choice has been made.
	 *
	 * @param Cursor
	 *            The cursor, positioned to a particular row in the list.
	 * @return A String representing the row's text value. (Note that this
	 *         specializes the base class return value for this method,
	 *         which is {@link CharSequence}.)
	 */
	@Override
	public String convertToString(Cursor cursor) {
		final int columnIndex = cursor.getColumnIndexOrThrow("word");
		final String str = cursor.getString(columnIndex);
		return str;
	}
	
	public String toString(){
		return "sts";
	}

	/**
	 * Called by the ListView for the AutoCompleteTextView field to display
	 * the text for a particular choice in the list.
	 *
	 * @param view
	 *            The TextView used by the ListView to display a particular
	 *            choice.
	 * @param context
	 *            The context (Activity) to which this form belongs;
	 * @param cursor
	 *            The cursor for the list of choices, positioned to a
	 *            particular row.
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final int wordIndex = cursor.getColumnIndexOrThrow("word");
		TextView text1 = (TextView) view.findViewById(R.id.itemTextAutoSearch);
		text1.setText(cursor.getString(wordIndex));

	}

	/**
	 * Called by the AutoCompleteTextView field to display the text for a
	 * particular choice in the list.
	 *
	 * @param context
	 *            The context (Activity) to which this form belongs;
	 * @param cursor
	 *            The cursor for the list of choices, positioned to a
	 *            particular row.
	 * @param parent
	 *            The ListView that contains the list of choices.
	 *
	 * @return A new View (really, a TextView) to hold a particular choice.
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.item_list_auto, parent, false);
		return view;
	}

	/**
	 * Called by the AutoCompleteTextView field when a choice has been made
	 * by the user.
	 *
	 * @param listView
	 *            The ListView containing the choices that were displayed to
	 *            the user.
	 * @param view
	 *            The field representing the selected choice
	 * @param position
	 *            The position of the choice within the list (0-based)
	 * @param id
	 *            The id of the row that was chosen (as provided by the _id
	 *            column in the cursor.)
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
		Cursor cursor = (Cursor) listView.getItemAtPosition(position);
		String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
		ma.wd.nextWord(word);
		ma.isNewWorld();
		ma.setText(ma.wd.getWordEncode(), ma.wd.getWordSrc());
		if (ma.wd.getStaticFlag()) {
			ma.cleanStar();
		}
		ma.adapter.notifyDataSetChanged();
		ma.dlg.dismiss();


	}
}
