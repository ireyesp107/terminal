package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.CSV.CSVParser;
import edu.brown.cs.student.CSV.CreatorFromRow;
import edu.brown.cs.student.CSV.FactoryFailureException;
import edu.brown.cs.student.CSV.listStringFactory;
import edu.brown.cs.student.Sprint0.stars.Star;
import edu.brown.cs.student.Sprint0.stars.StarFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests our Parser's word, character, column, and row counter function as well as the Creater
 * from Row interface and exceptions thrown.
 */

public class ParserTest {

  /**
   * FUZZ TESTING FOR REFLECTION
   */
  public List<List<String>> parse_random_csv() throws IOException, FactoryFailureException {
    String random =
        Math.random() + "," + Math.random() + "," + Math.random() +
            "\n"
            + Math.random() + "," + Math.random() + "," + Math.random() +
            "\n"
            + Math.random() + "," + Math.random() + "," + Math.random();
    CSVParser parser = new CSVParser(new StringReader(random), row -> row);
    return parser.getContentList();
  }

  @Test
  public void fuzz_test() throws IOException, FactoryFailureException {
    List parsed = new ArrayList();
    for(int i = 0; i<10000; i++){
      parsed.add(this.parse_random_csv());
    }
    assertEquals(10000, parsed.size());
  }








  @Test
  public void test_csvCounter() throws IOException, FactoryFailureException {
    /** testData1 tests for multiple words in a column, empty first and middle column, */
    CSVParser parser = new CSVParser(new FileReader("data/testShapeData.csv"), row -> row);
    assertEquals(6, parser.getRowCount());
    assertEquals(20, parser.getWordCount());
    assertEquals(3, parser.getColCount());
    // assertEquals(84, parser.getColCount());
  }

  @Test
  public void testParsedData() throws IOException, FactoryFailureException {
    CSVParser parse = new CSVParser(new FileReader("data/aeneidData.csv"), new listStringFactory());

    assertEquals(4, parse.getColCount());
    assertEquals(8, parse.getRowCount());
    assertEquals(28, parse.getWordCount());
    assertEquals(181, parse.getCharCount());

  }


  @Test
  public void test_ten_stars_wordCount() throws IOException, FactoryFailureException {
    CSVParser parser = new CSVParser(new FileReader("data/ten-star.csv"), row -> row);
    assertEquals(parser.getColCount(), 5);
    assertEquals(59, parser.getWordCount());
  }




      @Test
      public void test_parseDataMethod() throws IOException, FactoryFailureException {
        /**
         * Tests parseData method in CSVUtility class to make sure it returns a List of lists of
   strings
         */
        CSVParser utility = new CSVParser(new FileReader("data/testShapeData.csv"), row -> row);
        List<List<String>> list = utility.getContentList();
        List<String> row = list.get(2);
        assertEquals("2", row.get(0));
      }

      @Test
      public void test_parsed_star_data() throws IOException, FactoryFailureException {
        /** Tests csvParser with the Star data example using StarFactory CreatorFromRow */
        CreatorFromRow starFactory = new StarFactory();
        CSVParser parser = new CSVParser(new FileReader("data/stars/ten-star.csv"), starFactory);
        List<Star> starList = parser.getContentList();
        Star myStar = starList.get(2);
        assertEquals(43.04329, myStar.getXPoint(), 1);
      }

      @Test
      public void test_stars_with_counter() throws IOException, FactoryFailureException {
        CSVParser counter = new CSVParser(new FileReader("data/stars/stardata.csv"), row -> row);
        assertEquals(counter.getColCount(), 5);
      }

      @Test
      public void test_empty_CSV() throws IOException, FactoryFailureException {
        new CSVParser(new FileReader("data/emptyCSV.csv"), row -> row);
      }

      @Test
      public void test_factoryFailureException() {
        assertThrows(
            FactoryFailureException.class,
            () ->
                new CSVParser(new FileReader("data/testShapeData.csv"), new StarFactory()));
      }

      @Test
      public void test_FileNotFoundException() {
        assertThrows(
            FileNotFoundException.class,
            () -> new CSVParser(new FileReader("blaaah"), new StarFactory()));
      }

}
