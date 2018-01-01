package features;

import helper.Document;
import helper.WordStatistik;

import java.util.HashMap;
import java.util.List;

/**
 * Feature that counts all the words, which are only used in negative reviews
 */
public class NegativeInfoFeature extends Feature<Double> {
    List<Document> documents;
    HashMap<String, Integer> vocab;

    public NegativeInfoFeature(String name, List<Document> docs) {
        super(name, true);
        documents = docs;
        vocab = WordStatistik.getVocabularyList(docs, false);
    }

    @Override
    public Double extract(String input) {
        HashMap<String, Integer> words = WordStatistik.countWords(input, false);
        int[] count = new int[1];
        words.keySet().stream().forEach(k -> {
            if (vocab.containsKey(k)) {
                count[0] += words.get(k);
            }
        });
        return new Double(count[0]);
    }
}
