/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.preference.PreferenceActivity;
import android.os.Bundle;

/**
 *
 * @author petroff
 */
public class prf extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prf);
	}
}
