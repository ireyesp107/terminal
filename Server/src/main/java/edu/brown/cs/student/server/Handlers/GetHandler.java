package edu.brown.cs.student.server.Handlers;

import edu.brown.cs.student.server.CSVObject;
import edu.brown.cs.student.server.ResponseTypes;
import edu.brown.cs.student.server.ResponseTypes.SerializedMapResult;
import java.util.HashMap;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler class for the getcsv endpoint. */
public class GetHandler implements Route {
  private CSVObject datasource;

  /**
   * Constructor accepts a shared datasource object that has the information about the loaded csv
   */
  public GetHandler(CSVObject datasource) {
    this.datasource = datasource;
  }

  /**
   * Gets the loaded csv and returns its data serialized into a json format. Needs to be called
   * after loadcsv endpoint has been run successfully or it will return a bad request failure
   * response.
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return response content
   * @throws Exception This is part of the interface; we don't have to throw anything.
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> resultMap = new HashMap<>();
    QueryParamsMap paramsMap = request.queryMap();
    if (paramsMap.toMap().size()==0) {
      System.out.println("no params");
      if (this.datasource.getLoaded()) {
        resultMap.put("result", "success");
        resultMap.put("data", this.datasource.getData());
        return new SerializedMapResult(resultMap).serialize();
      } else {
        return new ResponseTypes.DataSourceErrorFailureResponse().serialize();
      }
    }else{
      System.out.println("param error");
      return new ResponseTypes.BadRequestFailureResponse().serialize();

    }
  }
}
