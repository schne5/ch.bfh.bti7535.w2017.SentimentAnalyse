package features;

import helper.Constants;
import helper.WordStatistik;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Feature that weights the words with tf-idf
 */
public class TF_IDF_Feature {
    //Num of Documents
    private int numOfDocuments;

    //List of vocabulary
    private HashMap<String,Integer> vocabularyList;
    //List of filenames, with wordstatistik list
    private HashMap<String,HashMap<String,Integer>> wordStatistikDocument;
    //Input File List
    HashMap<String,Boolean> files;
    //Max occurrence of words
    private double max_df;
    //Min occurrence of words
    private int min_df;

    public TF_IDF_Feature(HashMap<String,Boolean> files, double max_df, int min_df){
        this.files = files;
        this.numOfDocuments = files.size();
        this.vocabularyList = new HashMap<String,Integer>();
        this.wordStatistikDocument = new HashMap<String,HashMap<String,Integer>>();
        this.max_df = max_df;
        this.min_df = min_df;
        generateVocabularyList();
    }
    /*Generates the Vocabulary list of words used for naive bayes
      Result is a HashMap with words as key and document frequency count as values
    */
    private void generateVocabularyList(){
        for(String file: files.keySet()) {
            try {
                //Read File content
                List<String> lines = Files.readAllLines(Paths.get(files.get(file) ? Constants.PATH_POSITIVE : Constants.PATH_NEGATIVE, file));
                List<String> words = WordStatistik.getWords(lines);
                //Remove stop words
                //TODO: Here perhaps negation feature
                //List with frequency of each word
                HashMap<String, Integer> wordStatistik = new HashMap<String, Integer>();
                for (String word : words) {
                   if (word.length() <= 2)
                        continue;
                    //Insert in vocab if not exists or increment count
                    if (!wordStatistik.containsKey(word)) {
                        if (!vocabularyList.containsKey(word)) {
                            vocabularyList.put(word, new Integer(1));
                        } else {
                            Integer count = vocabularyList.get(word);
                            count = count + 1;
                            vocabularyList.put(word, count);
                        }
                        wordStatistik.put(word, 1);
                    }
                    Integer count = wordStatistik.get(word);
                    count++;
                    wordStatistik.put(word, count);
                }
                wordStatistikDocument.put(file, wordStatistik);
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        //Remove all the words not in definded range
        for (Iterator<String> iter = vocabularyList.keySet().iterator(); iter.hasNext(); ) {
            String word = iter.next();
            Integer value = vocabularyList.get(word);
            if(value < min_df)
                iter.remove();
            //remove words that occurs more than max_df multiplied by num of files
            if(value > (max_df * numOfDocuments))
                iter.remove();
                // vocabularyList.remove(word);
        }
    }
    /*
    Generates a vector with length equal to num of vocabulary and values are tf-idf weights.
    if word doesn't exist in Document, than the tf-idf weight is 0.
    */
    public  HashMap<String,Double> generateVectorTraining(String filename){
        //List with word counts for current Document
        HashMap<String,Integer> wordStatistik = wordStatistikDocument.get(filename);
        //Iterate trough vocab list and calculate tf-idf weight for each word in vocabulary
        return calculateWeights(wordStatistik);
    }
    /*
       Generates a vector with length equal to num of vocabulary and values are tf-idf weights.
       if word doesn't exist in Document, than the tf-idf weight is 0.
       */
    public HashMap<String,Double> generateTestVector(String filename,boolean positive) {
        //List with word counts for current Document
        HashMap<String, Integer> wordStatistik = new HashMap<String, Integer>();
        try {
            //Read file content
            List<String> lines = Files.readAllLines(Paths.get(positive ?Constants.PATH_POSITIVE : Constants.PATH_NEGATIVE, filename));
            List<String> words = WordStatistik.getWords(lines);
            //Remove stop words
            words = StopWordFeature.removeStopWords(words);
            //Generate word frequency List
            for (String word : words) {
                if (wordStatistik.containsKey(word)) {
                    int value = wordStatistik.get(word) + 1;
                    wordStatistik.put(word, value);
                } else {
                    wordStatistik.put(word, 1);
                }
            }
            //calculate tf-idf weight for each word in vocabulary
          return calculateWeights(wordStatistik);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Integer> getVocabularyList() {
        return vocabularyList;
    }

    public void setVocabularyList(HashMap<String, Integer> vocabularyList) {
        this.vocabularyList = vocabularyList;
    }

    public HashMap<String, HashMap<String, Integer>> getWordStatistikDocument() {
        return wordStatistikDocument;
    }

    public void setWordStatistikDocument(HashMap<String, HashMap<String, Integer>> wordStatistikDocument) {
        this.wordStatistikDocument = wordStatistikDocument;
    }

    //Calculate tf-idf weights and returns a vector
    private HashMap<String, Double> calculateWeights(HashMap<String, Integer> wordStatistik){
        HashMap<String, Double> wordVector = new HashMap<String, Double>();
        for (String key : vocabularyList.keySet()) {
            if (wordStatistik.containsKey(key)) {
                double value = (double)wordStatistik.get(key);
                double tf = Math.log10(1+value);
                double idf = (double) ((double)numOfDocuments / (double)vocabularyList.get(key));
                double tf_idf = tf * idf;
                wordVector.put(key, tf_idf);
            }else{
                wordVector.put(key, 0.0);
            }
        }
        return wordVector;
    }
}
