package edu.brown.cs.student.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Class holds the various response records called in the Handlers. It contains the failure
 * responses for "error_bad_request", "error_bad_json", and "error_datasource"
 *
 * <p>Additionally, we have a general serializer that turns a map of strings to object into a json
 * file
 *
 * <p>We decided to make different error responses rather than just putting the error message into a
 * a map and the serializing the map, because we wanted to avoid the risk of typos and stuff.
 */
public class ResponseTypes {

  /** Response object to send if file is not successfully loaded */
  public record BadRequestFailureResponse(String response_type) {
    public BadRequestFailureResponse() {
      this("error_bad_request");
    }

    /**
     * @return this response, serialized as Json
     */
    public String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(BadRequestFailureResponse.class).toJson(this);
    }
  }

  /** Response object to send if the request was ill-formed */
  public record BadJsonFailureResponse(String response_type) {
    public BadJsonFailureResponse() {
      this("error_bad_json");
    }

    /**
     * @return this response, serialized as Json
     */
    public String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(BadJsonFailureResponse.class).toJson(this);
    }
  }

  /**
   * Response object to send if file is not found or there is an error connecting to the datasource
   */
  public record DataSourceErrorFailureResponse(String response_type) {
    public DataSourceErrorFailureResponse() {
      this("error_datasource");
    }

    /**
     * @return this response, serialized as Json
     */
    public String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(DataSourceErrorFailureResponse.class).toJson(this);
    }
  }

  public record SerializedMapResult(Map<String, Object> result) {

    /**
     * @return this response, serialized as Json, public for testing
     */
    public String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(Map.class).toJson(result);
    }
  }

  public static Map<String, Object> deserializeMap(HttpResponse<String> response)
      throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    System.out.println("adapter built");
    JsonAdapter<Map> adapter = moshi.adapter(Map.class);
    System.out.println("adapter");
    Map map = adapter.fromJson(response.body());
    return map;
  }
}
