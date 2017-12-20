package helper;

import features.StopWordElimination;
import features.TextPreProcessor;
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

	public void generateFile() throws IOException {
		Util.print("Started generating arff file");
		 
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
	
	public void writeFile(String filename, Instances instances) {
		try {
			Files.write(Paths.get(Constants.PATH_RESSOURCES, filename), instances.toString().getBytes());
			Util.print(String.format("File %s written to the following location: %s", filename, Constants.PATH_RESSOURCES));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getUseNegator() {
		return this.useNegator;
	}
	
	public void setUseNegator(boolean value){
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
		content = TextPreProcessor.removeStopWords(content);
		content = TextPreProcessor.increaseWordWeight(2,0.2,content);
		 
		instance.setValue(attributes.get(0), classValues.get(positive ? 0 : 1));
		instance.setValue(attributes.get(1), content);
		
		if (useNegator) {
			NegatorResult result = null;
			try {
				result = Negator.executeNegation(content);
			} catch (IOException e) {
				Logger.log(Level.SEVERE, e.getMessage());
			}
			
			instance.setValue(attributes.get(negatorIndex), result.getNegatedWordWeight());			
		}
		
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
