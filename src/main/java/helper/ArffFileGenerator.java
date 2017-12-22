package helper;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.logging.Logger;
import weka.core.logging.Logger.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import features.Negator;
import features.NegatorResult;
import features.Rating;

public class ArffFileGenerator {

    ArrayList<Attribute> attributes;
    ArrayList<String> classValues;

    /**
     * true means feature 'negator' is enabled; false otherwise
     */
    private boolean useNegator = false;
    /**
     * represents the index of the negator attribute in the arff file
     */
    private int negatorIndex = -1;

    private boolean useRating = false;
    private int ratingIndex = -1;
    private boolean useWordWeightIncreasing = false;
    private boolean useAdjectiveWordWeightIncreasing = false;
    private boolean removeStopWords = false;

    public ArffFileGenerator() {
        attributes = new ArrayList<Attribute>();
        classValues = new ArrayList<String>();
        classValues.add("pos");
        classValues.add("neg");

        // add "basic" attributes 
        Attribute sentiment = new Attribute("@@class@@", classValues); // weka throws IllegalArgumentException (Attribute names are not unique!) if we write 'sentiment' instead of '@@class@@' 
        Attribute text = new Attribute("text", (ArrayList<String>) null);
        attributes.add(sentiment);
        attributes.add(text);
    }

    public void generateFile(List<Document> documents) throws IOException {
        Util.print("Started generating arff file");

        Instances instances = new Instances("Film Review", attributes, 1);
        DenseInstance instance;

        documents.stream().forEach(d -> {
            instances.add(createInstance(d));
        });
        writeFile(Constants.FILE_NAME_REVIEW, instances);

        Util.print("Finished generating arff file");
    }

    public void writeFile(String filename, Instances instances) {
        try {
            Files.write(Paths.get(Constants.PATH_RESSOURCES, filename), instances.toString().getBytes());
            Util.print(String.format("File %s written to the following location: %s", filename, Constants.PATH_RESSOURCES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUseNegator(boolean value) {
        this.useNegator = value;

        if (value) {
            Attribute attribute = new Attribute("negation");
            this.attributes.add(attribute);
            this.negatorIndex = this.attributes.size() - 1;
        } else {
            for (int i = 0; i < this.attributes.size(); i++) {
                if (this.attributes.get(i).name() == "negation") {
                    this.attributes.remove(i);
                    this.negatorIndex = -1;
                    break;
                }
            }
        }
    }

    public void setUseWordWeightIncreasing(boolean value) {
        this.useWordWeightIncreasing = value;
    }

    public void setUseRating(boolean value) {
        this.useRating = value;

        if (value) {
            Attribute attribute = new Attribute("rating");
            this.attributes.add(attribute);
            this.ratingIndex = this.attributes.size() - 1;
        } else {
            for (int i = 0; i < this.attributes.size(); i++) {
                if (this.attributes.get(i).name() == "rating") {
                    this.attributes.remove(i);
                    this.ratingIndex = -1;
                    break;
                }
            }
        }
    }

    public void setRemoveStopWords(boolean removeStopWords) {
        this.removeStopWords = removeStopWords;
    }

    public void setUseAdjectiveWordWeightIncreasing(boolean useAdjectiveWordWeightIncreasing) {
        this.useAdjectiveWordWeightIncreasing = useAdjectiveWordWeightIncreasing;
    }

    private DenseInstance createInstance(Document d) {
        DenseInstance instance = new DenseInstance(attributes.size());

        String content = d.getContent();

        //Stopword removal
        if (removeStopWords)
            content = TextPreProcessor.removeStopWords(content);
        //Increase textregion
        if (useWordWeightIncreasing)
            content = TextPreProcessor.increaseWordWeight(2, 0.2, content);
        //Increase adjective weights
        if (useAdjectiveWordWeightIncreasing)
            content = TextPreProcessor.increaseAdjWordWeight(content);

        instance.setValue(attributes.get(0), classValues.get(d.getGold() == Classification.POSITIVE ? 1 : 0));
        instance.setValue(attributes.get(1), content);

        // negator feature
        if (useNegator) {
            NegatorResult result = null;
            try {
                result = Negator.executeNegation(content);
            } catch (IOException e) {
                Logger.log(Level.SEVERE, e.getMessage());
            }

            instance.setValue(attributes.get(negatorIndex), result.getNegatedWordWeight());
        }

        // rating feature
        if (useRating) {
            double result = Rating.getRating(content, 1);
            instance.setValue(attributes.get(ratingIndex), result);
        }

        return instance;
    }
}
