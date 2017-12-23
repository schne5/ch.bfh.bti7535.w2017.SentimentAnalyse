package classifier;

import features.SentimentWordCountResult;
import features.SentimentWordCounter;
import helper.Classification;
import helper.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Baseline Algorithm: counts positive and negative words
 */
public class BaseLineClassifier implements Classifier {

    SentimentWordCounter wordCounter = new SentimentWordCounter();

    public List<Document> execute(List<Document> trainingSet, List<Document> testSet) {
        List<Document> result = new ArrayList<>();
        trainingSet.addAll(testSet);
        trainingSet.stream().forEach(d -> {
                    result.add(processDocument(d));
                }
        );
        return result;
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

    /**
     * Calculates the accuracy of the result
     *
     * @param documents
     */
    public void classify(List<Document> documents) {
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
        System.out.println(String.format("Accuracy : %f %%", accuracy));
    }

    /**
     * Executes the Baseline Algorithm with cross validation
     *
     * @param numFolds
     * @param documents
     */
    public void crossValidate(int numFolds, List<Document> documents) {
        Collections.shuffle(documents);
        double denominator = 1 - 0.8;
        int offset = (int) Math.floor(((float) documents.size()) * denominator);
        for (double i = 0; i < 1.0; i += denominator) {
            int start = (int) Math.floor(i * denominator);
            List<Document> trainingSet = documents.subList(start, offset);

            List<Document> testSet = new ArrayList<>(documents);
            testSet.removeAll(trainingSet);

            List<Document> results = execute(trainingSet, testSet);
            classify(results);
        }
    }
}
