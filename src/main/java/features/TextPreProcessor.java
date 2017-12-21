package features;

import helper.Constants;
import helper.WordStatistik;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class TextPreProcessor {
    static Dictionary dict = null;
    public static String removeStopWords(String text){
        return StopWordElimination.removeStopWordsFromText(text);
    }
    public static String increaseWordWeight(int factor, double textSection,String text){
        int length = text.length();
        int startIndex = (int)((1-textSection) *length);
        while(text.charAt(startIndex) != '.' && text.charAt(startIndex)!=','
                && text.charAt(startIndex)!= '?' && text.charAt(startIndex)!= '!' && text.charAt(startIndex)!= ' '){
            startIndex++;
        }
        String section = text.substring(startIndex);
        for(int i=0; i<factor-1;i++)
            text += section;
       return text+" "+section;
    }
    private static boolean isAdjective(String word) throws FileNotFoundException {
        try {
            IndexWord iword =  getDictionary().lookupIndexWord(POS.ADJECTIVE, word);
            return iword != null;
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static boolean isAdverb(String word) throws FileNotFoundException {
        try {

            IndexWord iword = getDictionary().lookupIndexWord(POS.ADVERB, word);
            return iword != null;
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String increaseAdjAdvWordWeight(String text) throws FileNotFoundException {
        List<String> words = WordStatistik.getWords(text);
        for(String word : words){
        if(isAdjective(word) || isAdverb(word)){
           text= text.replaceAll(word,word +" "+word);
        }
        }
        return text;
    }

    private static Dictionary getDictionary(){
        if(dict == null) {
            try {
                JWNL.initialize(new FileInputStream(Constants.PATH_RESSOURCES + "/properties.xml"));
                final Dictionary dictionary = Dictionary.getInstance();
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
