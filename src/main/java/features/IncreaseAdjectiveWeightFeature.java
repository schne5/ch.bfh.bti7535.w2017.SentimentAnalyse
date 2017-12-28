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
import java.util.ArrayList;
import java.util.List;

/**
 * Feature for increasing Adjective word weight
 */
public class IncreaseAdjectiveWeightFeature extends Feature<String> {
    private static Dictionary dict = null;

    public IncreaseAdjectiveWeightFeature(String name){
        super(name, false);
    }

    /**
     * Executes the feature on given text
     * @param input
     * @return
     */
    @Override
    public String extract(String input) {
        List<String> words = WordStatistik.getWords(input);
        List<String> adjectives = new ArrayList<>();
        words.stream().forEach(t->{
            if(isAdjective(t)){
                adjectives.add(t);
            }
        });
        for(String word : adjectives){
            input = input+ " "+word;
        }
        return input;
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
