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

/**
 * Generates the arff file used for naive bayes classification
 */
public class ArffFileGenerator {
	
	private static final Logger LOGGER = Logger.getLogger( ArffFileGenerator.class.getName() );
	
    ArrayList<Feature<?>> features;
    ArrayList<Feature<?>> textBasedfeatures;
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
    
    public List<Feature<?>> getTextBasedFeatures() {
        return this.textBasedfeatures;
    }

    public List<Feature<?>> getFeatures() {
        return this.features;
    }

    /**
     * generates the Instances object
     * @param documents
     * @param path path to the location where the .arff file will be saved
     * @param filename name of the .arff file that will contain the header and data information
     * @throws IOException
     */
    public void generateFile(List<Document> documents, String path, String filename) throws IOException {
        Util.print("Started generating arff file");

        Instances instances = new Instances("Film Review", attributes, 1);

        documents.stream().forEach(d -> {
            instances.add(createInstance(d));
        });
        Util.print("Finished setting Feature");
        writeFile(path, filename, instances);

        Util.print("Finished generating arff file");
    }

    /**
     * adding a feature
     * @param feature
     */
    public void addFeature(Feature<?> feature) {
    	if (feature.getHasAttribute()) {
    		this.features.add(feature);
            
            Attribute attribute = new Attribute(feature.getName());
            this.attributes.add(attribute);    		
    	} else {
            this.textBasedfeatures.add(feature);
    	}
    }

    /**
     * creates an entry for the arff file
     * @param d
     * @return
     */
    private DenseInstance createInstance(Document d) {
        DenseInstance instance = new DenseInstance(attributes.size());

        //Apply text- based features on text
        textBasedfeatures.stream().forEach(f -> {
        	Object result = f.extract(d.getContent());
        	if (result.getClass() == String.class) {
        		d.setContent((String)result);
        	}
        });

        instance.setValue(attributes.get(0), classValues.get(d.getGold() == Classification.POSITIVE ? 0 : 1));
        instance.setValue(attributes.get(1), d.getContent());

        //Add features to arff file
        features.stream().forEach(f -> {
        	Object result = f.extract(d.getContent());
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
    
    /**
     * Writes the file to filesystem
     * @param filename
     * @param instances
     */
    private void writeFile(String path, String filename, Instances instances) {
        try {
            Files.write(Paths.get(path, filename), instances.toString().getBytes());
            Util.print(String.format("File %s written to the following location: %s", filename, path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
