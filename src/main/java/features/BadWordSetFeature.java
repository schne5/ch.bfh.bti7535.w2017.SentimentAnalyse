package features;
import helper.WordStatistik;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Feature that counts bad words
 */
public class BadWordSetFeature extends Feature<Double>{
    private List<String> wordList;

    public BadWordSetFeature(String name, boolean hasAttribute) {
        super(name, true);
    }

    /**
     * counts the bad words in text file
     * @param input
     * @return
     */
    @Override
    public Double extract(String input) {
        String content = input;
        int[] count = new int[1];
        HashMap<String,Integer> words =WordStatistik.countWords(content,false);
        getWordList().stream().forEach(w -> {
            if(words.containsKey(w))
                count[0]++;
        });
        return new Double(count[0]);
    }

    /**
     * Returns a list with the worst words
     * @return
     */
    private List<String> getWordList() {
        if (wordList == null) {
            wordList = Arrays.asList("bad", "boring", "worst", "stupid", "waste",
                    "lame", "mess", "redundant","confusing","confused","tired");
        }
        return wordList;
    }
}
