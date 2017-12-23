package features;

import java.util.logging.Logger;

/**
 * Feature counting all the exclamation points in a string
 */
public final class ExclamationMarkFeature extends Feature<Integer> {
	
	private static final Logger LOGGER = Logger.getLogger( ExclamationMarkFeature.class.getName() );
	
	public ExclamationMarkFeature(String name) {
		this(name, 1);
	}
	
	public ExclamationMarkFeature(String name, int weight) {
		super(name, weight);
	}
	
	public Integer get(String input) {
		int count = 0;
		
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '!') {
				count++;
			}
		}
		
		return count;
	}
}
