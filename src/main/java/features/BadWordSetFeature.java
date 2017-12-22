package features;

import helper.Document;
import helper.WordStatistik;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BadWordSetFeature {
    private List<String> wordList;

    public List<String> getWordList() {
        if (wordList == null) {
            wordList = Arrays.asList("bad", "boring", "worst", "stupid", "waste",
                    "lame", "mess", "redundant","confusing","confused","tired");
        }
        return wordList;
    }

    public double applyFeature(Document d){
        String content = d.getContent();
        int[] count = new int[1];
        HashMap<String,Integer> words =WordStatistik.countWords(content,false);
        getWordList().stream().forEach(w -> {
            if(words.containsKey(w))
                count[0]++;
        });
        return count[0];


    }
}
