package features;

public class TextPreProcessor {
	public static String removeStopWords(String text) {
		return StopWordElimination.removeStopWords(text);
	}

	public static String increaseWordWeight(int factor, double textSection, String text) {
		int length = text.length();
		int startIndex = (int) ((1 - textSection) * length);
		while (text.charAt(startIndex) != '.' && text.charAt(startIndex) != ',' && text.charAt(startIndex) != '?'
				&& text.charAt(startIndex) != '!' && text.charAt(startIndex) != ' ') {
			startIndex++;
		}
		String section = text.substring(startIndex);
		for (int i = 0; i < factor - 1; i++)
			text += section;
		return text + " " + section;
	}
}
