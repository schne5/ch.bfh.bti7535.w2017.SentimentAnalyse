package features;

/**
 * Base class for text- based features
 */
public abstract class TextFeature {
    private String name;

    public TextFeature(String name) {
        this.name = name;
    }

    /**
     * Executes the feature on given text
     * @param input
     * @return
     */
    public abstract String execute(String input);

    /**
     * Returns the name of the feature
     * @return
     */
    public String getName() {
        return this.name;
    }
}
