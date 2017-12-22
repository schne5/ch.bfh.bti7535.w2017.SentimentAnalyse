package features;

public abstract class Feature<T> {
	private String name;
	private int weight;
	
	public Feature(String name) {
		this(name, 1);
	}
	
	public Feature(String name, int weight) {
		this.name = name;
		this.weight = weight;
	}
	
	public abstract T get(String input);
	
	public String getName() {
		return this.name;
	}
	
	public int getWeight() {
		return this.weight;
	}
}
