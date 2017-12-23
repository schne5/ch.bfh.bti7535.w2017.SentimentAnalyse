package helper;

/**
 * Object to store all Information of a textfile
 */
public class Document {
    private String fileName;
    private Classification gold;
    private Classification test;
    private String content;

    public Document(String filename, Classification gold, String content) {
        this.fileName = filename;
        this.gold = gold;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Classification getGold() {
        return gold;
    }

    public void setGold(Classification gold) {
        this.gold = gold;
    }

    public Classification getTest() {
        return test;
    }

    public void setTest(Classification test) {
        this.test = test;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
