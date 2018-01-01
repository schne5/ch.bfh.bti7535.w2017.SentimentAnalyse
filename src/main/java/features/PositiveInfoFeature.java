package features;

import helper.Document;
import helper.WordStatistik;

import java.util.HashMap;
import java.util.List;

/**
 * Feature that counts all the words, which are only used in positive reviews
 */
public class PositiveInfoFeature extends Feature<Double> {
    List<Document> documents;
    HashMap<String, Integer> vocab;

    public PositiveInfoFeature(String name, List<Document> docs) {
        super(name, true);
        documents = docs;
        vocab = WordStatistik.getVocabularyList(docs, true);
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


