package features;
import helper.WordStatistik;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Feature that counts good words
 */
public class GoodWordSetFeature extends Feature<Double>{
    private List<String> wordList;

    public GoodWordSetFeature(String name) {
        super(name, true);
    }

    /**
     * counts the good words in a string
     * @param input
     * @return
     */
    @Override
    public Double extract(String input) {
        String content = input;
        int[] count = new int[1];
        HashMap<String,Integer> words = WordStatistik.countWords(content,false);
        getWordList().stream().forEach(w -> {
            if(words.containsKey(w))
                count[0]++;
        });
        return new Double(count[0]);
    }

    /**
     * Returns a list with good words
     * @return
     */
    private List<String> getWordList() {
        if (wordList == null) {
            wordList = Arrays.asList(
            		"amazing", 
            		"better",
            		"best",
            		"brilliant",
            		"excellent",
            		"exceptional",
            		"great",
            		"hilarious",
            		"good",
            		"marvelous",
            		"outstanding",
            		"phenomenal",
            		"stunning",
            		"terrific",
            		"thrilling",
            		"wonderful"
            		);
        }
        
        return wordList;
    }
}
