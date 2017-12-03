package features;

import helper.WordStatistik;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class SentimentWordCounter {
    HashMap<String, Integer> sentimentWords;
    public  SentimentWordCountResult countSentimentWords(Path p) {
        int posCount = 0;
        int negCount = 0;
        try {
            List<String> lines = Files.readAllLines(p);
            HashMap<String, Integer> wordMap = splitText(lines);
            if(sentimentWords == null)
                sentimentWords = getSentimentList();

            for (String word : wordMap.keySet()) {
                if (sentimentWords.containsKey(word)){
                    if(sentimentWords.get(word) == 0){
                        negCount = negCount + wordMap.get(word);
                    }else{
                        posCount = posCount + wordMap.get(word);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File not available");
        }
        return new SentimentWordCountResult(posCount, negCount);
    }

    public static HashMap<String, Integer> splitText(List<String> lines) {
        StringBuilder text = new StringBuilder();
        for (String line : lines)
            text.append(line);

        return WordStatistik.countWords(text.toString(), true);
    }

    public static HashMap<String, Integer> getSentimentList() {
        HashMap<String, Integer> sentimentList = new HashMap<String, Integer>();
        File f = new File("C:\\Users\\elisa\\IdeaProjects\\sentimentanalyse.parent\\lexicon\\inquirerbasic.xls");
        FileInputStream ios = null;

        try {
            ios = new FileInputStream(f);
            HSSFWorkbook wb = new HSSFWorkbook(ios);
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if(row.getRowNum() == 0 )
                    continue;
                Cell cell = row.getCell(0);
                Cell posCell = row.getCell(2);
                Cell negCell = row.getCell(3);
                if (posCell != null && posCell.getStringCellValue().equals("Positiv"))
                    sentimentList.put(cell.getStringCellValue(), 1);
                if (negCell != null && negCell.getStringCellValue().equals("Negativ"))
                    sentimentList.put(cell.getStringCellValue(), 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sentimentList;
    }
}
