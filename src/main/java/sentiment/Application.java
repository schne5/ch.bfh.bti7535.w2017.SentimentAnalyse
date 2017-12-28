package sentiment;

import classifier.Classifier;
import classifier.BaseLineClassifier;
import classifier.NaiveBayesClassifier;
import helper.Constants;
import helper.Document;
import helper.Util;

import java.util.List;

/**
 * Class containing main method
 */
public class Application {

    public static void main(String[] args) throws Exception {
        Classifier classifier = null;
        //TODO: whats the purpose of this? Classifier will always be NB no matter what arguments are passed in
        //args = new String[1];  
        //args[0]= Constants.NAIVE_BAYES;
        if (args.length > 0) {
            if (args[0] == Constants.BASELINE) {
                classifier = new BaseLineClassifier();
            } else if(args[0] == Constants.NAIVE_BAYES) {
                classifier = new NaiveBayesClassifier();
            } else {
            	printNoValidClassifierSet();            	
            }
            
            if (classifier != null) {
            	List<Document> documents = Util.getAllDocuments();
            	classifier.crossValidate(10, documents);	
            }
        } else {
        	printNoValidClassifierSet();
        }
    }
    
    private static void printNoValidClassifierSet() {
    	System.out.format("No valid classifier was set. Possible options are: %s, %s", Constants.BASELINE, Constants.NAIVE_BAYES);
    }
}
