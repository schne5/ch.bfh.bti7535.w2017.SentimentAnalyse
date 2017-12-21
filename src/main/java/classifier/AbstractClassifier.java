package classifier;

import helper.Document;

import java.util.List;

public abstract class AbstractClassifier {

    public abstract void crossValidate(List<Document> documents);
}
