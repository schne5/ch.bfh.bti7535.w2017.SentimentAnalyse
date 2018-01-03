package classifier;

import features.SentimentWordCountResult;
import features.SentimentWordCounter;
import helper.Classification;
import helper.Document;
import helper.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Baseline Algorithm: counts positive and negative words
 */
public class BaseLineClassifier implements Classifier {

    SentimentWordCounter wordCounter = new SentimentWordCounter();

    /**
     * Executes the Baseline Algorithm with cross validation
     *
     * @param numFolds
     * @param documents
     */
    public void crossValidate(int numFolds, List<Document> documents) {
        Collections.shuffle(documents);
        
        double denominator = (double)1/numFolds;
        double average = 0;
        int offset = (int) Math.floor(((float) documents.size()) * denominator);
        int start = 0;
        
        for (int i = 0; i < numFolds; i++) {
        	List<Document> testSet = documents.subList(start, start + offset);
        	List<Document> results = execute(documents, testSet);
            
            double accuracy = classify(results);
            average += accuracy;
            
            start += offset;
        }
        
        Util.print(String.format("Average result %f %%", average / numFolds));
    }
    
    private List<Document> execute(List<Document> trainingSet, List<Document> testSet) {
        List<Document> result = new ArrayList<>();
        List<Document> combined = new ArrayList<>();
        combined.addAll(trainingSet);
        combined.addAll(testSet);
        
        combined.stream().forEach(d -> {
                result.add(processDocument(d));
            }
        );
        
        return result;
    }
    
    /**
     * Calculates the accuracy of the result
     *
     * @param documents
     * @return accuracy in per cent
     */
    private double classify(List<Document> documents) {
        double truePos = 0;
        double trueNeg = 0;
        double falsePos = 0;
        double falseNeg = 0;

        for (Document doc : documents) {
            Classification gold = doc.getGold();
            Classification tested = doc.getTest();

            // Ignores NOT_SPECIFIED
            if (gold == tested) {
                //true
                if (gold == Classification.POSITIVE)
                    truePos++;
                else if (gold == Classification.NEGATIVE)
                    trueNeg++;
            } else {
                //false
                if (tested == Classification.POSITIVE)
                    falsePos++;
                else if (tested == Classification.NEGATIVE)
                    falseNeg++;
            }
        }
        
        double denominator = truePos + trueNeg + falseNeg + falsePos;
        double nominator = trueNeg + truePos;
        double accuracy = (nominator / denominator) * 100;
        Util.print(String.format("Accuracy : %f %%", accuracy));
        
        return accuracy;
    }
    
    /**
     * Sets the classification based on calculated result
     *
     * @param document
     * @return
     */
    private Document processDocument(Document document) {

        SentimentWordCountResult result = wordCounter.countSentimentWords(document.getContent());
        if (result.getTotal() > 0) {
            Classification c = ((double) result.getPositiveWordCount() / (double) result.getTotal() > 0.5) ? Classification.POSITIVE : Classification.NEGATIVE;
            document.setTest(c);
        } else {
            document.setTest(Classification.NOT_SPECIFIED);
        }
        return document;
    }
}
