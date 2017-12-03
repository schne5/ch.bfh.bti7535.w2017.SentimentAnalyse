package features;

public class SentimentWordCountResult {
    private int positiveWordCount;
    private int negativeWordCount;

    public SentimentWordCountResult(int posCount, int negCount){
        this.positiveWordCount = posCount;
        this.negativeWordCount = negCount;
    }

    public int getPositiveWordCount() {
        return positiveWordCount;
    }

    public void setPositiveWordCount(int positiveWordCount) {
        this.positiveWordCount = positiveWordCount;
    }

    public int getNegativeWordCount() {
        return negativeWordCount;
    }

    public void setNegativeWordCount(int negativeWordCount) {
        this.negativeWordCount = negativeWordCount;
    }
}
