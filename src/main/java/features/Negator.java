package features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Negator {
	private static final Logger LOGGER = Logger.getLogger( Negator.class.getName() );
	
	static List<String> negations = new ArrayList<String>(Arrays.asList("n't", "not"));
	static List<Character> punctuations = new ArrayList<Character>(Arrays.asList('.', '.', ',', '?', '!', ';'));
	
	public static void process(String fileName) throws IOException {
		Boolean addNotPrefix = false;
		Boolean isNegation = false;
		Boolean isPunctuation = false;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			
			String line = reader.readLine();
	
		    while (line != null) {
		        String[] words = line.split(" ");
				for (String word : words) {
					if (punctuations.contains(word.charAt(word.length() -1))) {
						isPunctuation = true;
						isNegation = false;
						addNotPrefix = false;
					}
					else if (word.endsWith(negations.get(0)) || word.endsWith(negations.get(1))) {
						isPunctuation = false;
						isNegation = true;
					}
					
					if (addNotPrefix) {
						System.out.print("NOT_");	
					}
					
					System.out.print(word + " ");
					
					addNotPrefix = isNegation && !isPunctuation; 
				}
				
				System.out.println("");
				line = reader.readLine();
		    }
		} catch(Exception ex) {
			LOGGER.log(Level.SEVERE, ex.toString());
		} finally {
			reader.close();
		}
	}
}
