package features;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import helper.FileToList;

/**
 * @autor Anna
 * 
 * @since 03.12.2017
 * 
 *        Entfernt Stopwörter aus übergebenen Text (Liste mit Strings)
 */

public class StopWordElimination {

	public static List<String> removeStopWords(List<String> list) {
		Path pathStopWords = Paths.get("stopWords", "stopWords.txt");
		List<String> stopwords = FileToList.fileToList(pathStopWords);

		List<String> cleandList = removeWords(stopwords, list);

		return cleandList;
	}

	private static List<String> removeWords(List<String> toRemove, List<String> removeFrom) {
		List<String> cleandList = new ArrayList<String>();
		cleandList.addAll(removeFrom);
		cleandList.removeAll(toRemove);
		return cleandList;
	}
}
