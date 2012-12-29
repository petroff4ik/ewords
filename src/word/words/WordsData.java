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
import java.io.Serializable;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * 
 * @author petroff
 */
public class WordsData implements Serializable {

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
	private boolean newWords = false;
	private static final String TAG = "WordsData";
	protected boolean CharFindFlag = false;
	Context c;
	static SharedPreferences sp;
	private String wrongChar;
	
	public String getWrongChar(){
		return this.wrongChar;
	}

	public boolean getNewWords() {
		return this.newWords;
	}

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
		sp = PreferenceManager.getDefaultSharedPreferences(c);
		initSrcLang();
		this.db = db;
		Restart();
	}

	public void Restart() {
		hashmap.put("en", new String[]{"a", "b", "c", "d", "e", "f", "g",
					"h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
					"t", "u", "v", "w", "x", "y", "z"});
		hashmap.put("ru",
				new String[]{"а", "б", "в", "г", "д", "е", "з", "ж", "к",
					"л", "м", "н", "р", "п", "о", "е", "с", "т", "я", "ч",
					"и", "ь", "ю", "й", "ц", "у", "ш", "з", "х", "э", "ы",
					"ф"});
		countWords = db.getCountWords(langSrc);
		Reload();
	}

	public String[] getData() {
		return hashmap.get(alph);
	}

	public String getData(int position) {
		String[] chars = hashmap.get(alph);
		return chars[position];
	}

	public void setData(String ch, int position) {
		String[] chars = hashmap.get(alph);
		chars[position] = ch;
	}

	public void Reload() {
		Map word = db.getWordStatusNo(langSrc);
		chooseChars.removeAll(chooseChars);
		wordSrc = "";
		wordEncode = "";
		wordTarget = "";
		if (word.size() > 0) {
			wordSrc = word.get("wordSrc").toString();
			wordTarget = word.get("word").toString();
			for (int i = 1; i <= wordTarget.length(); i++) {
				wordEncode = wordEncode.concat("*");
			}
			availableWord = true;

		} else {
			availableWord = false;
		}
	}

	public int getRandomInt(int max) {
		return (int) (Math.random() * (max - 1) + 1);
	}

	public void resetWords() {
		db.resetWords(langSrc);
	}

	public boolean checkChar(int position) {
		String c = getData(position);
		chooseChars.add(c);
		wrongChar = "";
		int size = chooseChars.size();
		String like = "";
		for (int i = 0; i < size; i++) {
			like = like + " words.word LIKE '%" + chooseChars.get(i) + "%' ";
			if (i != (size - 1)) {
				like = like + " AND ";
			}

		}
		if (like.isEmpty()) {
			CharFindFlag = false;
			setData("", position);
			removeChars(c);
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
				CharFindFlag = true;
				setData("", position);
			} else {
				CharFindFlag = false;
				setData("", position);
				wrongChar = c;
				removeChars(c);
			}

		}
		if (wordEncode.indexOf("*")==-1) {
			newWords = true;
			db.update(wordSrc);
		}else{
			newWords = false;	
		}
		return CharFindFlag;

	}

	protected void removeChars(String c) {
		chooseChars.remove(chooseChars.lastIndexOf(c));
	}

	public String GetChar(int position) {
		return getData(position);
	}

	public int getColor(int position) {
		String ch = getData(position);
		if (ch.equals("")) {
			return 0x00000000;
		} else {
			return 0xff0000ff;
		}
	}

	public void nextWord() {
		db.update(wordSrc);
		Restart();
	}

	protected void initSrcLang() {
		String srcLang = sp.getString("SL", "RU");
		if (srcLang.equals("RU")) {
			langSrc = "ru";
			alph = "en";
		} else {
			langSrc = "en";
			alph = "ru";
		}
	}
}
