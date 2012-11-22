/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

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
}
