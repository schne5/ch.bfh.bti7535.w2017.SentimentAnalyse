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

public class ArffFileGenerator {
	
	private boolean useNegator;
	ArrayList<Attribute> attributes;
	ArrayList<String> classValues;
	
	public ArffFileGenerator() {
		attributes = new ArrayList<Attribute>();
		classValues = new ArrayList<String>(); 
        classValues.add("pos"); 
        classValues.add("neg"); 
        
        // weka throws IllegalArgumentException (Attribute names are not unique!) if we use 'sentiment' instead of '@@class@@' 
        Attribute sentiment = new Attribute("@@class@@", classValues); 
        Attribute text = new Attribute("text", (ArrayList<String>) null); 
        Attribute dummy = new Attribute("dummy"); //TODO: replace with real attribute
 
        attributes.add(sentiment);
        attributes.add(text);
        attributes.add(dummy);
	}

	public void writeFile(String filename, Instances instances) {
		try {
			Files.write(Paths.get(Constants.PATH_RESSOURCES, filename), instances.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateFile() throws IOException {
		Util.print("Started generating arff file");
		

 
        //build training data 
        Instances instances = new Instances("Film Review", attributes, 1); 
        DenseInstance instance; 
		
		List<String> filesPositive = readAllFileNames(Constants.PATH_POSITIVE);
		List<String> filesNegative = readAllFileNames(Constants.PATH_NEGATIVE);

		for (String fileName : filesPositive) {
			instance = createInstance(fileName, true);
			instances.add(instance); 
		}
		
		for (String fileName : filesNegative) {
			instance = createInstance(fileName, false);
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
	
	private DenseInstance createInstance(String fileName, boolean positive) {
		DenseInstance instance = new DenseInstance(3);
		
		String content = null;
		try {
			content = getContentFromFile(fileName, positive);
		} catch (IOException e) {
			Logger.log(Level.SEVERE, e.getMessage());
		}
		 
		instance.setValue(attributes.get(0), classValues.get(positive ? 0 : 1));
		instance.setValue(attributes.get(1), content);
		instance.setValue(attributes.get(2), 1);
		
		return instance;
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
