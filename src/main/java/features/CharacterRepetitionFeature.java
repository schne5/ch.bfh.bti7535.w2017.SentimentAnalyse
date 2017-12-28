package features;

import java.util.logging.Logger;

/**
 * Feature counting all repetitions of a certain character in a string
 */
public final class CharacterRepetitionFeature extends Feature<Integer> {
	
	private static final Logger LOGGER = Logger.getLogger( CharacterRepetitionFeature.class.getName() );
	
	private Character character;
	
	public CharacterRepetitionFeature(String name, Character character) {
		super(name, true);
		
		this.character = character;
	}
	
	public Integer extract(String input) {
		int count = 0;
		
		Character previousChar = ' ';
		
		for (int i = 0; i < input.length(); i++) {
			Character currentChar = input.charAt(i);
			
			if (currentChar.equals(' ')) {
				continue;
			}
			
			if (currentChar.equals(this.character) && previousChar.equals(currentChar)) {
				count++;
			}
			
			previousChar = input.charAt(i);
		}
		
		return count;
	}
}
