package features;
import helper.WordStatistik;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BadWordSetFeature extends Feature<Double>{
    private List<String> wordList;

    public BadWordSetFeature(String name) {
        super(name);
    }

    @Override
    public Double get(String input) {
        String content = input;
        int[] count = new int[1];
        HashMap<String,Integer> words =WordStatistik.countWords(content,false);
        getWordList().stream().forEach(w -> {
            if(words.containsKey(w))
                count[0]++;
        });
        return new Double(count[0]);
    }

    private List<String> getWordList() {
        if (wordList == null) {
            wordList = Arrays.asList("bad", "boring", "worst", "stupid", "waste",
                    "lame", "mess", "redundant","confusing","confused","tired");
        }
        return wordList;
    }
}
