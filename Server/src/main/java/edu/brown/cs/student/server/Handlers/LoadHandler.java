package edu.brown.cs.student.server.Handlers;

import edu.brown.cs.student.CSV.CSVParser;
import edu.brown.cs.student.CSV.FactoryFailureException;
import edu.brown.cs.student.server.CSVObject;
import edu.brown.cs.student.server.ResponseTypes.BadRequestFailureResponse;
import edu.brown.cs.student.server.ResponseTypes.DataSourceErrorFailureResponse;
import edu.brown.cs.student.server.ResponseTypes.SerializedMapResult;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler class for the load endpoint. Takes a basic GET request with no Json body, and returns a
 * Json object in reply.
 */
public class LoadHandler implements Route {
  private CSVParser parser;
  private CSVObject dataSource;

  /** Constructor accepts some shared state */
  public LoadHandler(CSVObject myDatasource) {
    this.dataSource = myDatasource;
  }

  /**
   * Handle a load request by getting the file path in the parameter and then loading and parsing
   * the file to fill the datasource object to prep for a getcsv request.
   *
   * <p>Expects calls in format: localhost:3232/loadcsv?filepath=FILENAMEHERE
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return response content
   * @throws Exception This is part of the interface; we don't have to throw anything.
   */
  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap paramsMap = request.queryMap();
    Map<String, Object> resultMap = new HashMap<>(); // will be populated with result info
    try {
      if (paramsMap.toMap().size() == 1 && paramsMap.toMap().containsKey("filepath")) {
        String filePath = request.queryParams("filepath");
        System.out.println("file: " + filePath);
        this.parser = new CSVParser(new FileReader(filePath), row -> row);
          this.dataSource.setLoaded(true);
          this.dataSource.setData(this.parser.getContentList());
          resultMap.put("result", "success");
          return new SerializedMapResult(resultMap).serialize();
      } else {
        this.dataSource.setLoaded(false);
        return new BadRequestFailureResponse().serialize();
      }
    } catch (IOException e) {
      this.dataSource.setLoaded(false);
      return new DataSourceErrorFailureResponse().serialize(); // Failure responses are in another class
    }catch(FactoryFailureException e) {
      this.dataSource.setLoaded(false);
      return new BadRequestFailureResponse().serialize();
    }
  }
}
