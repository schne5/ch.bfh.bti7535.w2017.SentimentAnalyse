package classifier;

import helper.Document;

import java.util.List;

/**
 * Base class for Classifiers
 */
public interface Classifier {
    /**
     * Executes Classifier with cross validation
     * @param numFolds
     * @param documents
     * @throws Exception
     */
    void crossValidate(int numFolds,List<Document> documents) throws Exception;
}
