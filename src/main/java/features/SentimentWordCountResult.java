package features;

public class SentimentWordCountResult {
    private int positiveWordCount;
    private int negativeWordCount;
    private int total;

    public SentimentWordCountResult(int posCount, int negCount, int total){
        this.positiveWordCount = posCount;
        this.negativeWordCount = negCount;
        this.total = total;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
