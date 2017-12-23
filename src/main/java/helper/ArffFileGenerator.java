package helper;

import features.TextFeature;
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
    ArrayList<TextFeature> textBasedfeatures;
    ArrayList<Attribute> attributes;
    ArrayList<String> classValues;

    public ArffFileGenerator() {
    	features = new ArrayList<>();
    	textBasedfeatures = new ArrayList<>();
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

        documents.stream().forEach(d -> {
            instances.add(createInstance(d));
        });
        Util.print("Finished setting Feature");
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

    public void addTextBasedFeature(TextFeature feature) {
        this.textBasedfeatures.add(feature);
    }
    
    public List<TextFeature> getTextBasedFeatures() {
        return this.textBasedfeatures;
    }

    public List<Feature<?>> getFeatures() {
        return this.features;
    }


    private DenseInstance createInstance(Document d) {
        DenseInstance instance = new DenseInstance(attributes.size());

        //Apply text- based features on text
        textBasedfeatures.stream().forEach(f ->{
            d.setContent(f.execute(d.getContent()));
        });

        instance.setValue(attributes.get(0), classValues.get(d.getGold() == Classification.POSITIVE ? 1 : 0));
        instance.setValue(attributes.get(1), d.getContent());

        //Add features to arff file
        features.stream().forEach(f ->{
            Object result = f.get(d.getContent());
            if (result.getClass() == Double.class) {
                instance.setValue(this.attributes.get(this.features.indexOf(f) + 2), (double)result);
            } else if (result.getClass() == Double.class || result.getClass() == Integer.class) {
                instance.setValue(this.attributes.get(this.features.indexOf(f) + 2), ((Integer)result).doubleValue());
            } else if (result.getClass() == String.class) {
                instance.setValue(this.attributes.get(this.features.indexOf(f) + 2), (String)result);
            }
        });
        return instance;
    }
}
