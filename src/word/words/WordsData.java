/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.content.SharedPreferences;
import android.content.Context;
import 	java.util.HashMap;
import java.util.Map;

/**
 *
 * @author petroff
 */
public class WordsData {

	String[] data = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	DBConnector db;
	Integer countWords;
	private String wordSrc;
	private String wordTarget;
	private String wordEncode = "";
	private String langSrc = "ru";

	SharedPreferences sPref;
	Context c;

	public String getWordTarget() {
		return this.wordTarget;
	}
	
	public String getWordSrc(){
		return this.wordSrc;
	}

	public WordsData(DBConnector db, Context c) {
		this.db = db;
		countWords = db.getCountWords(langSrc);

	}

	public String[] getData() {
		return data;
	}

	public String Reload() {
		Map word = db.getWordStatusNo(langSrc);
		wordSrc = word.get("wordSrc").toString();
		wordTarget = word.get("word").toString();
		for (int i = 1; i <= wordTarget.length(); i++) {
			wordEncode = wordEncode.concat("*");
		}
		return wordEncode;
	}

	public int getRandomInt(int max) {
		return (int) (Math.random() * (max - 1) + 1);
	}


}
