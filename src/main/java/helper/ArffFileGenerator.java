package helper;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArffFileGenerator {
	
	private boolean useNegator;

	public void writeFile(String filename, Instances instances) {
		try {
			Files.write(Paths.get(Constants.PATH_RESSOURCES, filename), instances.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateFile() throws IOException {
		Util.print("Started generating arff file");
		
		ArrayList<Attribute> attributes = new ArrayList<Attribute>(); 
        ArrayList<String> classValues = new ArrayList<String>(); 
        classValues.add("pos"); 
        classValues.add("neg"); 
        
        // weka throws IllegalArgumentException (Attribute names are not unique!) if we use 'sentiment' instead of '@@class@@' 
        Attribute sentiment = new Attribute("@@class@@", classValues); 
        Attribute text = new Attribute("text", (ArrayList<String>) null); 
        Attribute dummy = new Attribute("dummy"); //TODO: replace with real attribute
 
        attributes.add(sentiment);
        attributes.add(text);
        attributes.add(dummy);
 
        //build training data 
        Instances instances = new Instances("Film Review", attributes, 1); 
        DenseInstance instance; 
		
		List<String> filesPositive = readAllFileNames(Constants.PATH_POSITIVE);
		List<String> filesNegative = readAllFileNames(Constants.PATH_NEGATIVE);

		for (String file : filesPositive) {
			String content = getContentFromFile(file, true);
			instance = new DenseInstance(3); 
			instance.setValue(attributes.get(0), classValues.get(0));
			instance.setValue(attributes.get(1), content);
			instance.setValue(attributes.get(2), 1);
			instances.add(instance); 
		}
		
		for (String file : filesNegative) {
			String content = getContentFromFile(file, false);
			instance = new DenseInstance(3);
			instance.setValue(attributes.get(0), classValues.get(1));
            instance.setValue(attributes.get(1), content);
            instance.setValue(attributes.get(2), 1);
            instances.add(instance); 
		}
		
		writeFile(Constants.FILE_NAME_REVIEW, instances);
		
		Util.print("Finished generating arff file");
	}
	
	public boolean getUseNegator() {
		return this.useNegator;
	}
	
	public void setUseNegator(boolean value){
		this.useNegator = value;
	}

	private static List<String> readAllFileNames(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();

		for (File file : files) {
			if (file.isFile()) {
				fileNames.add(file.getName());
			}
		}
		return fileNames;
	}

	private String getContentFromFile(String filename, boolean positive) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> lines = Files
				.readAllLines(Paths.get(positive ? Constants.PATH_POSITIVE : Constants.PATH_NEGATIVE, filename));

		for (String line : lines) {
			sb.append(line);
		}
		return sb.toString();
	}
}
