/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

/**
 *
 * @author petroff
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

public class GVadapter extends ArrayAdapter<String> {

	private final Activity context;
	private WordsData wd;

	public GVadapter(Activity context, String[] values, WordsData wd) {
		super(context, R.layout.item, values);
		this.context = context;
		this.wd = wd;

	}

	static class ViewHolder {
		public LinearLayout linearLayout;
		public TextView textView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder буферизирует оценку различных полей шаблона элемента

		ViewHolder holder;
		// Очищает сущетсвующий шаблон, если параметр задан
		// Работает только если базовый шаблон для всех классов один и тот же
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.item, null, true);
			holder = new ViewHolder();
			holder.textView = (TextView) rowView.findViewById(R.id.tvText);
			holder.linearLayout = (LinearLayout) rowView.findViewById(R.id.ll);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		holder.textView.setText(wd.GetChar(position));
		holder.linearLayout.setBackgroundColor(wd.getColor(position));
		// Изменение иконки для Windows и iPhone

		return rowView;
	}
}
