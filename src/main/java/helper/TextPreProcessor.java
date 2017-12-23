package helper;

import features.StopWordFeature;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class TextPreProcessor {
    private static net.didion.jwnl.dictionary.Dictionary dict = null;
	public static String removeStopWords(String text) {
		return StopWordFeature.removeStopWords(text);
	}

	public static String increaseWordWeight(int factor, double textSection, String text) {
		int length = text.length();
		int startIndex = (int) ((1 - textSection) * length);
		while (text.charAt(startIndex) != '.' && text.charAt(startIndex) != ',' && text.charAt(startIndex) != '?'
				&& text.charAt(startIndex) != '!' && text.charAt(startIndex) != ' ') {
			startIndex++;
		}
		String section = text.substring(startIndex);
		for (int i = 0; i < factor - 1; i++)
			text += section;
		return text + " " + section;
	}




}
