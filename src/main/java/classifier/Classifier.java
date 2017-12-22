package classifier;

import helper.Document;

import java.util.List;

public interface Classifier {

    void crossValidate(int numFolds,List<Document> documents) throws Exception;
}
