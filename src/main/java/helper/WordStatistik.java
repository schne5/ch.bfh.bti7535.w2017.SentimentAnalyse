package helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class WordStatistik {
    public static HashMap<String, Integer> countWords(String text,boolean uppercase){
        StringTokenizer tokenizer = new StringTokenizer(text,"\n\"-+,&%$§.;:?!(){}[]1234567890*@ ");
        HashMap<String, Integer> wordMap = new HashMap<String,Integer>();
        
        // a first draft of adding more weight to words
        HashMap<String, Integer> intensifications = new HashMap<String, Integer>();
		intensifications.put("VERY", 1);
		intensifications.put("MORE", 1);
		intensifications.put("MOST", 2);
		intensifications.put("LESS", 1);
		intensifications.put("LEAST", -2);
		intensifications.put("ALWAYS", 3);
		intensifications.put("NEVER", -3);
		intensifications.put("TOO", 1);
        
        String preword = "";
        
        while(tokenizer.hasMoreTokens()){
            String word = tokenizer.nextToken();
            
            int weight = intensifications.containsKey(preword) ? intensifications.get(preword) : 0;
            
            if(uppercase)
                word = word.toUpperCase();
            Integer count = wordMap.get(word);
            if (count == null) {
                wordMap.put(word, new Integer(1));
            } else {
                int countValue = count.intValue() + 1 + weight;
                wordMap.put(word, new Integer(countValue));
            }
            
            preword = word.toUpperCase();
        }
        return wordMap;
    }
    public static List<String> getWords(List<String> lines){
        List<String> words = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(splitText(lines),"\n\"-+,&%$§.;:?!(){}[]1234567890*@ ");
        while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            words.add(word);
        }
        return words;
    }

    public static String splitText(List<String> lines) {
        StringBuilder text = new StringBuilder();
        lines.stream().forEach(line -> {
            text.append(line);
        });
        return text.toString();
    }

    public static List<String> getWords(String txt) {
        List<String> words = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(txt,"\n\"-+,&%$§.;:?!(){}[]1234567890*@ ");
        while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            words.add(word);
        }
        return words;
    }
}
