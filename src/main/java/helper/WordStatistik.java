package helper;

import java.util.HashMap;
import java.util.StringTokenizer;

public class WordStatistik {
    public static HashMap<String, Integer> countWords(String text,boolean uppercase){
        StringTokenizer tokenizer = new StringTokenizer(text,"\n\"-+,&%$§.;:?!(){}[]1234567890*@ ");
        HashMap<String, Integer> wordMap = new HashMap<String,Integer>();
        while(tokenizer.hasMoreTokens()){
            String word = tokenizer.nextToken();
            if(uppercase)
                word = word.toUpperCase();
            Integer count =wordMap.get(word);
            if(count == null){
                wordMap.put(word, new Integer(1));
            }else{
                int countValue = count.intValue() +1;
                wordMap.put(word, new Integer(countValue));
            }
        }
        return wordMap;
    }
}
