/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package word.words;

/**
 *
 * @author petroff
 */
public class WordsData {

	String[] data = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	DBConnector db;
	
	public WordsData(DBConnector db) {
		this.db = db;
	}
	
	public String[] getData(){
		return data;
	}
}
