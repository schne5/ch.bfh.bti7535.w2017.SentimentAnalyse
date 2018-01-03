package features;

/**
 * Feature counting all occurences of a certain character in a string
 */
public final class CharacterOccurenceFeature extends Feature<Integer> {
		
	private Character character;
	
	public CharacterOccurenceFeature(String name, Character character) {
		super(name, true);
		
		this.character = character;
	}
	
	public Integer extract(String input) {
		int count = 0;
		
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == this.character) {
				count++;
			}
		}
		
		return count > 0 ? 1 : 0;
	}
}
