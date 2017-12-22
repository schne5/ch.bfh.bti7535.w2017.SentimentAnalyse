package sentiment;

import classifier.Classifier;
import classifier.BaseLineClassifier;
import classifier.NaiveBayesClassifier;
import helper.Document;
import helper.Util;

import java.util.List;

public class Application {

    public static void main(String[] args) throws Exception {
        List<Document> documents = Util.getAllDocuments();
        Classifier classifier = null;
        args[0]="NB";
        if (args.length > 0) {
            if (args[0] == "base") {
                classifier = new BaseLineClassifier();
            }else if(args[0] == "NB"){
                classifier = new NaiveBayesClassifier();
            }
            classifier.crossValidate(10, documents);
        }
        /*ArffFileGenerator generator = new ArffFileGenerator();
		generator.setRemoveStopWords(false);
		generator.setUseNegator(true);
		generator.setUseRating(false);
		generator.setUseWordWeightIncreasing(true);
		generator.setUseAdjectiveWordWeightIncreasing(true);
		generator.generateFile();
		
		NaiveBayesClassifier classifier = new NaiveBayesClassifier();
		classifier.setup();
		classifier.crossValidate(10);*/

      //  BaseLineClassifier baseLineClassifier = new BaseLineClassifier();
       // baseLineClassifier.crossValidate(10,documents);
    }
}
