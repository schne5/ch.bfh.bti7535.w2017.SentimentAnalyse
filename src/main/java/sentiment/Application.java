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
        List<Document> documents = Util.getAllDocuments();
        Classifier classifier = null;
        args = new String[1];
        args[0]= Constants.NAIVE_BAYES;
        if (args.length > 0) {
            if (args[0] == Constants.BASELINE) {
                classifier = new BaseLineClassifier();
            }else if(args[0] == Constants.NAIVE_BAYES){
                classifier = new NaiveBayesClassifier();
            }
            classifier.crossValidate(10, documents);
        }
    }
}
