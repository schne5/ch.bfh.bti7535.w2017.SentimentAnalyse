package classifier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import helper.Document;
import helper.Util;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.stemmers.LovinsStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * Naive Bayes Classification Algorithm
 */
public class NaiveBayesClassifier implements Classifier {
	private static final Logger LOGGER = Logger.getLogger( NaiveBayesClassifier.class.getName() );

    private FilteredClassifier filteredBayes = new FilteredClassifier();
    private Instances data = null;
    
    private String path;
    private String filename;

    /**
     * Naive Bayes classification algorithm
     * @param arffFilePath path to the .arff file
     * @param arffFileName name of the .arff file that contains the required header and data information
     */
    public NaiveBayesClassifier(String arffFilePath, String arffFileName) {
    	this.path = arffFilePath;
    	this.filename = arffFileName;
    }
    
    /**
     * Read Arff File with containing data and apply filters
     */
    public void setup(){
        readArffFile();
        StringToWordVector vectorFilter = getSTWVFilter();
        try
        {
            vectorFilter.setInputFormat(this.data);
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        //Combine filters
        MultiFilter multiFilter = new MultiFilter();
        Filter[] filters = new Filter[1];
        filters[0] = vectorFilter;

        multiFilter.setFilters(new Filter[] {vectorFilter});


        this.filteredBayes.setClassifier(new weka.classifiers.bayes.NaiveBayes());
        this.filteredBayes.setFilter(multiFilter);
        try
        {
            multiFilter.setInputFormat(this.data);
        } catch(Exception e)
        {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Executes Naive Bayes Algorithm with cross validation
     * @param numFolds
     * @param documents
     * @throws Exception
     */
    public void crossValidate(int numFolds,List<Document> documents) throws Exception {
    	if (numFolds <= 1) {
    		String msg = "Number of folds must be greater than 1";
    		LOGGER.log(Level.SEVERE, msg);
    		throw new Exception(msg);
    	}
    	//Shuffle documents for reaching the same distribution of pos and negative documents
        Collections.shuffle(documents);
    	
    	setup();

    	Util.print("Started cross validation");
    	double average =0;
        Evaluation eval = new Evaluation(data);
        for (int n = 0; n < numFolds; n++) {
            Instances train = data.trainCV(numFolds, n);
            Instances test = data.testCV(numFolds, n);

            filteredBayes.buildClassifier(train);
            eval.evaluateModel(filteredBayes, test);
            double correctRate = eval.pctCorrect();
            average += correctRate;
            Util.print(String.format("Accuracy in round %d of %d: %f %%", n + 1, numFolds, correctRate));
        }
        
        Util.print(String.format("Average result %f %%", average / numFolds));
        Util.print("Finished cross validation");
    }

    /**
     * Method for reading arff file
     */
    private  void  readArffFile() {
        try
        {
            //Read arff file
            BufferedReader reader = new BufferedReader(new FileReader(this.path + "\\" + this.filename));
            ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
            this.data = arff.getData();
            this.data.setClassIndex(0);
            reader.close();
        } catch(FileNotFoundException e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } catch(IOException e)
        {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Returns Ngram tokenzizer
     * @param maxSize
     * @return
     */
    private static NGramTokenizer getNgramTokenizer(int maxSize) {
        NGramTokenizer ngram = new NGramTokenizer();
        ngram.setNGramMinSize(1);
        ngram.setNGramMaxSize(maxSize);
        return ngram;
    }

    /**
     * Setup String to word Filter
     * @return
     */
    private static StringToWordVector getSTWVFilter() {
        StringToWordVector filter = new StringToWordVector();

        //filter options
        filter.setWordsToKeep(50000);
        filter.setLowerCaseTokens(true);
        filter.setMinTermFreq(2);
//        LovinsStemmer stem = new LovinsStemmer();
//        filter.setStemmer(stem); // negative impact
        filter.setIDFTransform(true);
//        filter.setTokenizer(getNgramTokenizer(3)); // negative impact
        return filter;
    }
}
