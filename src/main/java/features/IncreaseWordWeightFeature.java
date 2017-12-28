package features;

/**
 * Feature for increasing the weight of a text section
 */
public class IncreaseWordWeightFeature extends Feature<String> {
    int factor;
    double textSection;

    public IncreaseWordWeightFeature(String name){
        super(name, false);
        factor = 2;
        textSection = 0.2;
    }

    /**
     * Executes the feature on given text
     * @param input
     * @return
     */
    @Override
    public String extract(String input) {
        int length = input.length();
        int startIndex = (int) ((1 - textSection) * length);
        while (input.charAt(startIndex) != '.' && input.charAt(startIndex) != ',' && input.charAt(startIndex) != '?'
                && input.charAt(startIndex) != '!' && input.charAt(startIndex) != ' ') {
            startIndex++;
        }
        String section = input.substring(startIndex);
        for (int i = 0; i < factor - 1; i++)
            input += section;
        return input + " " + section;
    }
}
