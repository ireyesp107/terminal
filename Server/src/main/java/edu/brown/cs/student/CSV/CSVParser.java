package edu.brown.cs.student.CSV;

import edu.brown.cs.student.Sprint0.stars.StarFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/** CSVParser class parser the CSV file and compiles CSV into a list of T. */
public class CSVParser<T> {

  private List<T> ContentList;
  private BufferedReader br;
  private String filePath;
  private Boolean fileFound;
  private int colCount;
  private int wordCount;
  private int charCount;
  private Reader reader;

  /**
   * csvReader class takes in a Reader and a CreatorFromRow and then parses the CSV data from the
   * file in the Reader and converts the rows into the CreatorFromRow type in order to organize the
   * CSV data into a List.
   */
  public CSVParser(Reader reader, CreatorFromRow<T> rowObject)
      throws IOException, FactoryFailureException {
    this.br = new BufferedReader(reader);
    this.ContentList = new ArrayList<T>();
    //this.loadFile();
    this.parseData(rowObject);
  }

//  /** Parses the data and puts into a List of lists of strings */
//  public Boolean loadFile() {
//    this.fileFound = false;
//    this.br = new BufferedReader(this.reader);
//    this.fileFound = true;
//    return this.fileFound;
//  }

  public void parseData(CreatorFromRow<T> rowObject) throws IOException, FactoryFailureException {
    this.wordCount = 0;
    this.charCount = 0;


      Boolean firstLine = true;
      while (true) {
        String curLine = this.br.readLine();
        if (curLine != null) {
          if(firstLine & rowObject.getClass() == StarFactory.class){
            firstLine = false;
          }else{
            this.charCount += curLine.length();
            List<String> data = List.of(curLine.split(","));
            T row = rowObject.create(data);
            this.colCount = data.size();
            this.ContentList.add(row);
            this.wordCount += this.countWordsInLine(curLine);
            firstLine = false;
          }
        } else {
          break;
        }
      }
      this.br.close();
  }

  /**
   * Helper method that counts the words in a line and returns the amt of words in the line. Method
   * is public for testing
   *
   * @param curLine the current line being counted in readAndCount
   * @return int of the words in the line passed in
   */
  public int countWordsInLine(String curLine) {
    String[] data = curLine.split(",");
    int wordsInLine = 0;
    for (String col : data) {
      // for each comma seperated column in data, separate based on spaces to get the amt of words
      int wordsInCol = new StringTokenizer(col, " ").countTokens();
      wordsInLine += wordsInCol;
    }
    return wordsInLine;
  }

  public int getRowCount() {
    return this.getContentList().size();
  }

  public int getColCount() {
    int col = this.colCount;
    return col;
  }

  public int getWordCount() {
    int w = this.wordCount;
    return w;
  }

  public int getCharCount(){
    int c = this.charCount;
    return c;
  }

  public Boolean getFileFound() {
    return fileFound;
  }

  public List<T> getContentList() {
    return this.ContentList;
  }
}
