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
		// ViewHolder Р±СѓС„РµСЂРёР·РёСЂСѓРµС‚ РѕС†РµРЅРєСѓ СЂР°Р·Р»РёС‡РЅС‹С… РїРѕР»РµР№ С€Р°Р±Р»РѕРЅР° СЌР»РµРјРµРЅС‚Р°

		ViewHolder holder;
		// РћС‡РёС‰Р°РµС‚ СЃСѓС‰РµС‚СЃРІСѓСЋС‰РёР№ С€Р°Р±Р»РѕРЅ, РµСЃР»Рё РїР°СЂР°РјРµС‚СЂ Р·Р°РґР°РЅ
		// Р Р°Р±РѕС‚Р°РµС‚ С‚РѕР»СЊРєРѕ РµСЃР»Рё Р±Р°Р·РѕРІС‹Р№ С€Р°Р±Р»РѕРЅ РґР»СЏ РІСЃРµС… РєР»Р°СЃСЃРѕРІ РѕРґРёРЅ Рё С‚РѕС‚ Р¶Рµ
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
		// Р�Р·РјРµРЅРµРЅРёРµ РёРєРѕРЅРєРё РґР»СЏ Windows Рё iPhone

		return rowView;
	}
}
