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

    public BadWordSetFeature(String name) {
        super(name, true);
    }

    /**
     * counts the bad words in a string
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
     * Returns a list with bad words
     * @return
     */
    private List<String> getWordList() {
        if (wordList == null) {
            wordList = Arrays.asList(
            		//"atrocious",
            		"bad", 
            		"boring", 
            		"confused",
            		"confusing",
            		//"dull",
            		//"failure",
                    "lame", 
                    "mess", 
                    //"poor",
                    "redundant",
                    "stupid", 
                    "tired",
                    //"uninspired",
                    "waste",
                    "worse", 
                    "worst" ,
                    "loveless",
                    "degenerates",
                    "psychlos",
                    "horrid"
//                    "stigmata"
//                    ,"kip"
//                    ,"sphere",
//                    "grinch",
//                    "switchback"
//                    ,"hawk"
                    );
        }
        
        return wordList;
    }
}
