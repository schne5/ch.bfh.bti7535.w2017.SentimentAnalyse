package features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Feature for counting words which intensify the meaning of other words
 */
public final class IntenseWordFeature extends Feature<Integer> {
	
	private static final Logger LOGGER = Logger.getLogger( IntenseWordFeature.class.getName() );
	
	private HashMap<String, Integer> intenseWords;
	
	public IntenseWordFeature(String name) {
		super(name, true);
		
        intenseWords = new HashMap<String, Integer>();
        intenseWords.put("VERY", 1);
        intenseWords.put("MORE", 1);
        intenseWords.put("MOST", 2);
		intenseWords.put("LESS", 1);
		intenseWords.put("LEAST", 2);
		intenseWords.put("ALWAYS", 2);
		intenseWords.put("NEVER", 2);
		intenseWords.put("TOO", 1);
	}
	
	public Integer extract(String input) {
		int count = 0;
		
		try {	    
	        String[] words = input.toUpperCase().split(" ");
			for (String word : words) {
				if (word.length() == 0) {
					continue;
				}
				
				count += intenseWords.containsKey(word) ? intenseWords.get(word) : 0;
			}
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
		
		return count > 10 ? 1 : 0;
	}
}
