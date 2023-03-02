package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.server.CSVObject;
import edu.brown.cs.student.server.Handlers.GetHandler;
import edu.brown.cs.student.server.Handlers.LoadHandler;
import edu.brown.cs.student.server.Handlers.WeatherHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class HandlerTest {

  /**
   * Method used in testing that takes in the connection and returns a map that is the deserialized
   * Json repsonse from the api
   *
   * @param clientConnection httpURLConnection
   * @return Map containing the json information from the api response
   * @throws IOException
   */
  public Map<String, Object> deserializeToMap(HttpURLConnection clientConnection)
      throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    return moshi
        .adapter(Map.class)
        .fromJson((new Buffer().readFrom(clientConnection.getInputStream())));
  }

  /** Set up Server once before all the tests */
  @BeforeAll
  public static void setUpServer() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /**
   * Shared state for all tests. We need to be able to mutate it (adding dating and changing loaded
   * boolean) but never need to replace the reference itself. We reset this state out after every
   * test runs.
   */
  CSVObject csv = new CSVObject(false, null);

  /** Before each test reset the csv data object and the spark endpoints and handlers */
  @BeforeEach
  public void setUpBeforeEach() {
    // Reset csv data object before each test
    this.csv.setLoaded(false);
    this.csv.setData(null);

    Spark.get("loadcsv", new LoadHandler(this.csv));
    Spark.get("getcsv", new GetHandler(this.csv));
    Spark.get("weather", new WeatherHandler());
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // stop Spark listening on both endpoints
    Spark.unmap("/loadcsv");
    Spark.unmap("/getcsv");
    Spark.unmap("/weather");

    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testLoadHandlerConnection() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=data/aeneidData.csv");
    assertEquals(200, clientConnection.getResponseCode());

  }

  @Test
  /**
   * Tests that a file can be loaded and get a success response
   */
  public void testLoadHandlerResponse() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=data/ten-star.csv");
    assertEquals(200, clientConnection.getResponseCode());
    System.out.println(clientConnection.getResponseMessage());
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> loadResult = this.deserializeToMap(clientConnection);

    assertEquals("success", loadResult.get("result"));
    clientConnection.disconnect();
  }

  @Test
  /**
   * Tests that an empty file can be loaded and get a success response
   */
  public void testLoadEmptyCSV() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=data/emptyCSV.csv");
    assertEquals(200, clientConnection.getResponseCode());
    System.out.println(clientConnection.getResponseMessage());
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> loadResult = this.deserializeToMap(clientConnection);

    assertEquals("success", loadResult.get("result"));
    clientConnection.disconnect();
  }

  @Test
  /**
   * Tests that an empty csv file can be loaded and get a success response
   */
  public void testGetEmptyCSV() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv?filepath=data/emptyCSV.csv");
    assertEquals(200, clientConnection.getResponseCode());
    System.out.println(clientConnection.getResponseMessage());
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> loadResult = this.deserializeToMap(clientConnection);

    assertEquals("success", loadResult.get("result"));
    clientConnection.disconnect();

    HttpURLConnection getconnection = tryRequest("getcsv");
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> getResult = this.deserializeToMap(getconnection);
    System.out.println(getResult.get("data"));

    // gets the correct data that is loaded on the csv
    assertEquals(new ArrayList(), getResult.get("data"));
    getconnection.disconnect();
  }

  @Test
  public void TestNonExistentParam() throws IOException {
    //Tests for load
    HttpURLConnection loadconnection = tryRequest("loadcsv?blaaaa");
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> loadResult = this.deserializeToMap(loadconnection);
    assertEquals("error_bad_request", loadResult.get("response_type"));
    loadconnection.disconnect();

    //Tests on get
    HttpURLConnection getconnection = tryRequest("getcsv?kdsjfhkjsdhfk");
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> getResult = this.deserializeToMap(getconnection);
    assertEquals("error_bad_request", getResult.get("response_type"));
    loadconnection.disconnect();

    HttpURLConnection connection = tryRequest("weather?hfsdh=dkfjhgd");
    //deserialize loadResult to a map to be able to check the result
    Map<String, Object> wResult = this.deserializeToMap(connection);
    assertEquals("error_bad_request", wResult.get("response_type"));
    assertNull(loadResult.get("temperature"));
    connection.disconnect();
  }

  @Test
  /** Tests that weather API can be called successfully and get a value for temperature*/
  public void TestWeatherSuccess() throws IOException {
    HttpURLConnection connection = tryRequest("weather?lat=39.7456&lon=-97.0892");
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> loadResult = this.deserializeToMap(connection);
    assertEquals("success", loadResult.get("result"));
    assertTrue(loadResult.get("temperature") != null);
    connection.disconnect();
  }

  @Test
  /** Tests that weather API sends the correct error response*/
  public void TestWeatherBadRequest() throws IOException {
    HttpURLConnection connection = tryRequest("weather?lat=hi&lon=chay");
     //deserialize loadResult to a map to be able to check the result
    Map<String, Object> loadResult = this.deserializeToMap(connection);
    assertEquals("error_bad_json", loadResult.get("response_type"));
    assertNull(loadResult.get("temperature"));
    connection.disconnect();
  }

  @Test
  /** Tests when a file is successfully loaded and then /get is called that the data is right */
  public void TestLoadAndGetSuccess() throws IOException {
    HttpURLConnection loadconnection = tryRequest("loadcsv?filepath=data/aeneidData.csv");
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> loadResult = this.deserializeToMap(loadconnection);
    assertEquals("success", loadResult.get("result"));
    loadconnection.disconnect();

    HttpURLConnection getconnection = tryRequest("getcsv");
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> getResult = this.deserializeToMap(getconnection);
    System.out.println(getResult.get("data"));

    // gets the correct data that is loaded on the csv
    assertEquals(this.csv.getData(), getResult.get("data"));
    getconnection.disconnect();
  }

//  @Test
//  /** Tests when a file is unsuccessfully loaded (checks error response) and then /get is called */
//  public void TestLoadAndGetFailure() throws IOException {
//    HttpURLConnection loadconnection = tryRequest("loadcsv?filepath=data/blaaaa.csv");
//    // deserialize loadResult to a map to be able to check the result
//    Map<String, Object> loadResult = this.deserializeToMap(loadconnection);
//    assertEquals("error_bad_request", loadResult.get("response_type"));
//    loadconnection.disconnect();
//
//    HttpURLConnection getconnection = tryRequest("getcsv");
//    // deserialize loadResult to a map to be able to check the result
//    Map<String, Object> getResult = this.deserializeToMap(getconnection);
//    Assertions.assertEquals("error_datasource", getResult.get("response_type"));
//    // Data should be null since you can't get csv data without
//    assertNull(getResult.get("data"));
//
//    getconnection.disconnect();
//  }

  /**
   * Test using a mock and injection. We create an injection using a strings and set it as our CSV to ensure we still
   * get the correct data
   */
  @Test
  public void testGetData() throws IOException {
    // example data for testing
    List<String> injection = Arrays.asList("Chay", "Sita!");
    List<List<String>> fullDummyList = new ArrayList<>();
    fullDummyList.add(injection);
    this.csv.setLoaded(true);
    this.csv.setData(fullDummyList);

    HttpURLConnection getconnection = tryRequest("getcsv");
    // deserialize loadResult to a map to be able to check the result
    Map<String, Object> getResult = this.deserializeToMap(getconnection);
    Assertions.assertEquals(fullDummyList, getResult.get("data"));
    // Data should be null since you can't get csv data without
    // establish connection

    getconnection.disconnect();

  }
}
