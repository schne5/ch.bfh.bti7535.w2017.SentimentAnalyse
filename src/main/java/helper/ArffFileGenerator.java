package helper;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import features.Feature;

public class ArffFileGenerator {
	
	private static final Logger LOGGER = Logger.getLogger( ArffFileGenerator.class.getName() );
	
    ArrayList<Feature<?>> features;
    ArrayList<Attribute> attributes;
    ArrayList<String> classValues;

    private boolean useWordWeightIncreasing = false;
    private boolean useAdjectiveWordWeightIncreasing = false;
    private boolean removeStopWords = false;

    public ArffFileGenerator() {
    	features = new ArrayList<>();
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
    
    public void addFeature(Feature<?> feature) {
        this.features.add(feature);
        
        Attribute attribute = new Attribute(feature.getName());
        this.attributes.add(attribute);
    }
    
    public List<Feature<?>> getFeatures() {
        return this.features;
    }

    public void setUseWordWeightIncreasing(boolean value) {
        this.useWordWeightIncreasing = value;
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
        
        for (Feature<?> feature : this.features) {
        	Object result = feature.get(content);
        	if (result.getClass() == Double.class) {
        		instance.setValue(this.attributes.get(this.features.indexOf(feature) + 2), (double)result);
        	} else if (result.getClass() == String.class) {
        		instance.setValue(this.attributes.get(this.features.indexOf(feature) + 2), (String)result);
        	}
        }

        return instance;
    }
}
