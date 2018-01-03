package sentiment;

import java.util.List;

import classifier.BaseLineClassifier;
import classifier.Classifier;
import classifier.NaiveBayesClassifier;
import features.*;
import helper.ArffFileGenerator;
import helper.Constants;
import helper.Document;
import helper.Util;

/**
 * Class containing main method
 */
public class Application {

    public static void main(String[] args) throws Exception {
        Classifier classifier = null;
        List<Document> documents = Util.getAllDocuments();
        
        if (args.length > 0) {
            if (args[0].equals(Constants.BASELINE)) {
                classifier = new BaseLineClassifier();
            } else if(args[0].equals(Constants.NAIVE_BAYES)) {
                ArffFileGenerator generator = new ArffFileGenerator();
//                generator.addFeature(new NegationFeature("negation")); // negative impact
//                generator.addFeature(new RatingFeature("@@rating")); // negative impact
                generator.addFeature(new CharacterOccurenceFeature("exclamationMark", '!'));
                generator.addFeature(new CharacterOccurenceFeature("questionMark", '?'));
                generator.addFeature(new CharacterRepetitionFeature("exclamationMarkRepetition", '!')); 
                generator.addFeature(new CharacterRepetitionFeature("questionMarkRepetition", '?')); 
                generator.addFeature(new IntenseWordFeature("intenseWords")); 
                generator.addFeature(new BadWordSetFeature("badwords"));
                generator.addFeature(new GoodWordSetFeature("goodwords"));
//                generator.addFeature(new StopWordFeature("stopwords")); // negative impact 
                generator.addFeature(new IncreaseWordWeightFeature("wordweight"));
//                generator.addFeature(new IncreaseAdjectiveWeightFeature("adjective")); // no impact, bad performance
                generator.addFeature(new PositiveInfoFeature("posInfo",documents));
                generator.addFeature(new NegativeInfoFeature("negInfo",documents));
                generator.generateFile(documents, Constants.PATH_RESSOURCES, Constants.FILE_NAME_REVIEW);
                
                classifier = new NaiveBayesClassifier(Constants.PATH_RESSOURCES, Constants.FILE_NAME_REVIEW);
            } else {
            	printNoValidClassifierSet();            	
            }
            
            if (classifier != null) {
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
