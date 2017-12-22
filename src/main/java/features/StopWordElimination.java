package features;

import helper.Util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @autor Anna
 * 
 * @since 03.12.2017
 * 
 *        Entfernt Stopw�rter aus �bergebenen Text (Liste mit Strings)
 */

public class StopWordElimination {

	public static List<String> removeStopWords(List<String> list) {
		return removeWords(readStopWords(), list);
	}

	public static String removeStopWords(String text) {
		return removeWords(readStopWords(), text);
	}

	public static List<String> readStopWords() {
		Path pathStopWords = Paths.get("stopWords", "stopWords.txt");
		return Util.fileToList(pathStopWords);
	}

	private static List<String> removeWords(List<String> toRemove, List<String> removeFrom) {
		List<String> cleandList = new ArrayList<String>();
		cleandList.addAll(removeFrom);
		cleandList.removeAll(toRemove);
		return cleandList;
	}

	private static String removeWords(List<String> toRemove, String removeFrom) {
		String[] textArray = (removeFrom.split(" "));
		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		for (String s : textArray) {
			if (!toRemove.contains(s)) {
				hm.put(hm.size() + 1, s);
			}
		}
		String cleandString = "";
		int j = hm.size();
		for (int i = 1; i <= j; i++) {
			cleandString += " " + hm.remove(i);
		}
		return cleandString.trim();
	}

}
