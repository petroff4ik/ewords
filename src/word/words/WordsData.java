/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

import android.content.Context;
import java.util.Map;

/**
 * 
 * @author petroff
 */
public class WordsData {

	String[] data = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
			"l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
			"y", "z" };
	DBConnector db;
	Integer countWords;
	private String wordSrc;
	private String wordTarget;
	private String wordEncode = "";
	private String langSrc = "en";
	private boolean availableWord = false;

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
	
	public boolean getAvailableWord(){
		return this.availableWord;
	}
	
	public WordsData(DBConnector db, Context c) {
		this.db = db;
		countWords = db.getCountWords(langSrc);
		Reload();

	}

	public String[] getData() {
		return data;
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
	
	public void resetWords(){
		db.resetWords(langSrc);
	}
	public boolean checkChar(String c){
		boolean flag = false;
		
		return flag;
	}

}
