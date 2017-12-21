package features;

import helper.Constants;
import helper.WordStatistik;
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
		return StopWordElimination.removeStopWords(text);
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

	public static String increaseAdjWordWeight(String text){
        List<String> words = WordStatistik.getWords(text);
        List<String> adjectives = new ArrayList<>();
        words.stream().forEach(t->{
            if(isAdjective(t)){
               adjectives.add(t);
            }
        });
        for(String word : adjectives){
            text = text+ " "+word;
        }
       return text;
    }

    private static boolean isAdjective(String word){
        try {
            IndexWord indexWord = getDictionary().getIndexWord(POS.ADJECTIVE, word);
            return indexWord !=null;
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static net.didion.jwnl.dictionary.Dictionary getDictionary(){
	    if(dict == null){
            try {
                JWNL.initialize(new FileInputStream(Constants.PATH_RESSOURCES+"\\properties.xml"));
                net.didion.jwnl.dictionary.Dictionary dictionary = net.didion.jwnl.dictionary.Dictionary.getInstance();
                dict = dictionary;
            } catch (JWNLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return dict;
    }
}
