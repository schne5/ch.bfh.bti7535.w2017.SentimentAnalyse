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

	public static void writeFile(String filename, Instances instances) {
		try {
			Files.write(Paths.get(Constants.PATH_RESSOURCES, filename), instances.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void generateFile() throws IOException {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>(); 
        ArrayList<String> classValues = new ArrayList<String>(); 
        classValues.add("pos"); 
        classValues.add("neg"); 
        
        Attribute sentiment = new Attribute("sentiment", classValues);
        Attribute text = new Attribute("text", (ArrayList<String>) null); 
 
        attributes.add(sentiment);
        attributes.add(text); 
 
        //build training data 
        Instances instances = new Instances("Film Review", attributes, 1); 
        DenseInstance instance; 
		
		List<String> filesPositive = readAllFileNames(Constants.PATH_POSITIVE);
		List<String> filesNegative = readAllFileNames(Constants.PATH_NEGATIVE);

		for (String file : filesPositive) {
			String content = getContentFromFile(file, true);
			instance = new DenseInstance(2); 
			instance.setValue(attributes.get(0), classValues.get(0));
			instance.setValue(attributes.get(1), content);
			instances.add(instance); 
		}
		
		for (String file : filesNegative) {
			String content = getContentFromFile(file, false);
			instance = new DenseInstance(2);
			instance.setValue(attributes.get(0), classValues.get(1));
            instance.setValue(attributes.get(1), content);
            instances.add(instance); 
		}
		
		writeFile(Constants.FILE_NAME_REVIEW, instances);
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

	private static String getContentFromFile(String filename, boolean positive) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> lines = Files
				.readAllLines(Paths.get(positive ? Constants.PATH_POSITIVE : Constants.PATH_NEGATIVE, filename));

		for (String line : lines) {
			sb.append(line);
		}
		return sb.toString();
	}
}
