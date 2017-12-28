package features;

/**
 * base class for all features
 * @param <T>
 */
public abstract class Feature<T> {
	private String name;
	private boolean hasAttribute;
	
	public Feature(String name, boolean hasAttribute) {
		this.name = name;
		this.hasAttribute = hasAttribute;
	}
	
	public abstract T extract(String input);
	
	public String getName() {
		return this.name;
	}
	
	public boolean getHasAttribute() {
		return this.hasAttribute;
	}
}
