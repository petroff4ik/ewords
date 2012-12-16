/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.content.Context;
import android.util.Log;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.String;

/**
 * 
 * @author petroff
 */
public class WordsData {

	Map<String, String[]> hashmap = new HashMap<String, String[]>();
	DBConnector db;
	Integer countWords;
	private String wordSrc;
	private String wordTarget;
	private String wordEncode = "";
	private String langSrc = "ru";
	private String alph = "en";
	private List<String> chooseChars = new ArrayList<String>();
	private boolean availableWord = false;
	private static final String TAG = "WordsData";

	Context c;

	public String getWordTarget() {
		return this.wordTarget;
	}

	public String getWordEncode() {
		return this.wordEncode;
	}

	public String getWordSrc() {
		return this.wordSrc;
	}

	public boolean getAvailableWord() {
		return this.availableWord;
	}

	public WordsData(DBConnector db, Context c) {
		this.db = db;
		hashmap.put("en", new String[] { "a", "b", "c", "d", "e", "f", "g",
				"h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z" });
		hashmap.put("ru",
				new String[] { "à", "á", "â", "ã", "ä", "å", "ç", "æ", "ê",
						"ë", "ì", "í", "ð", "ï", "î", "å", "ñ", "ò", "ÿ", "÷",
						"è", "ü", "þ", "é", "ö", "ó", "ø", "ç", "õ", "ý", "û",
						"ô" });
		countWords = db.getCountWords(langSrc);
		Reload();

	}

	public String[] getData() {
		return hashmap.get(alph);
	}

	public void Reload() {
		Map word = db.getWordStatusNo(langSrc);
		if (word.size() > 0) {
			wordSrc = word.get("wordSrc").toString();
			wordTarget = word.get("word").toString();
			for (int i = 1; i <= wordTarget.length(); i++) {
				wordEncode = wordEncode.concat("*");
			}
			availableWord = true;
		} else {
			wordSrc = "";
			wordEncode = "";
			wordTarget = "";
			availableWord = false;
		}
	}

	public int getRandomInt(int max) {
		return (int) (Math.random() * (max - 1) + 1);
	}

	public void resetWords() {
		db.resetWords(langSrc);
	}

	public boolean checkChar(String c) {
		boolean flag = false;
		chooseChars.add(c);
		int size = chooseChars.size();
		String like = "";
		for (int i = 0; i < size; i++) {
			like = like + " words.word LIKE '%" + chooseChars.get(i) + "%' ";
			if (i != (size - 1))
				like = like + " AND ";

		}
		if (like.isEmpty()) {
			flag = false;
		} else {
			Map<String, String> word = db.getWordCheck(langSrc, wordSrc, like);
			if (word.size() > 0) {
				wordTarget = word.get("word").toString();
				wordEncode = "";
				boolean findChars = false;
				String ch = "";
				Character cht;
				int ii;
				for (int i = 1; i <= wordTarget.length(); i++) {
					findChars = false;
					ii = i;
					ii--;
					cht = wordTarget.charAt(ii);
					for (int i1 = 0; i1 < size; i1++) {
						ch = chooseChars.get(i1);
						Log.i(TAG,"" + cht.toString() + " " + ch);
						if (ch.equals(cht.toString())) {
							findChars = true;
							break;
						}
					}
					if (findChars == true) {
						wordEncode = wordEncode.concat(ch);
					} else {
						wordEncode = wordEncode.concat("*");
					}
				}
				Log.i(TAG, wordTarget);
				flag = true;
			} else {
				flag = false;
			}

		}
		return flag;

	}

}
