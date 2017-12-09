package features;

public class NegatorResult {
	private String output;
	private int negatedWordCount;
	private int totalWordCount;
	
	public NegatorResult() {
		
	}
	
	public NegatorResult(String output, int negatedWordCount) {
		this.output = output;
		this.negatedWordCount = negatedWordCount;
	}
	
	public String getOutput() {
		return output;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	public int getNegatedWordCount() {
		return negatedWordCount;
	}
	
	public void setNegatedWordCount(int negatedWordCount) {
		this.negatedWordCount = negatedWordCount;
	}

	public int getTotalWordCount() {
		return totalWordCount;
	}

	public void setTotalWordCount(int totalWordCount) {
		this.totalWordCount = totalWordCount;
	}
	
	public void incrementNegatedWordCount() {
		this.negatedWordCount++;
	}
	
	public void incrementTotalWordCount() {
		this.totalWordCount++;
	}
	
	public double getNegatedWordWeight() {
		if (negatedWordCount == 0) {
			return 1;
		}
		
		if (totalWordCount == 0) {
			return 0;
		}
		
		return (double)(negatedWordCount * 5) / (double)totalWordCount; // multiply by factor 5 for higher accuracy
	}
}
