package edu.brown.cs.student.server.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.server.ResponseTypes;
import edu.brown.cs.student.server.ResponseTypes.SerializedMapResult;
import edu.brown.cs.student.server.Weather.Forecast;
import edu.brown.cs.student.server.Weather.Weather;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler class for the /weather endpoint.
 *
 * <p>Accesses the national weather service api with coordinates and gets the link to the forecast
 * and then gets the temperature which it returns as a json response.
 */
public class WeatherHandler implements Route {
  /**
   * Handle method called when the /weather endpoint is invoked on the local server. Takes in a
   * request and returns a json response.
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return response content
   * @throws Exception This is part of the interface; we don't have to throw anything.
   */

  @Override
  public Object handle(Request request, Response response) {
    // example formatting: localhost:3232/weather?lat=39.7456&lon=-97.0892
    QueryParamsMap paramsMap = request.queryMap();
    Map<String, Object> resultMap =
        new HashMap<>(); // will be populated with the success result info and temp
    try {
      if (paramsMap.toMap().size() == 2
          && paramsMap.toMap().containsKey("lat")
          && paramsMap.toMap().containsKey("lon")) { // check that long and latitude are given
        // get params
        String lon = request.queryParams("lon");
        String lat = request.queryParams("lat");
 // check that latitude and longitude are reasonable
          // get the forecast URI from the /points endpoint of the weather api
          URI forecastURI = this.getForecastURI(lon, lat);

          // get the temperature from the weather api using the forecast link
          int temp = this.getTemperature(forecastURI);

          // populate map to have a successful result and also contain the temperature
          resultMap.put("result", "success");
          resultMap.put("temperature", temp);
          return new SerializedMapResult(resultMap).serialize();
      } else {
        System.out.println("wrong params");
        return new ResponseTypes.BadRequestFailureResponse().serialize();
      }
    } catch (IOException e){
      return new ResponseTypes.BadJsonFailureResponse().serialize();
    }catch(URISyntaxException e){
      return new ResponseTypes.BadRequestFailureResponse().serialize();
    }catch(InterruptedException e){
      return new ResponseTypes.DataSourceErrorFailureResponse().serialize();
    }
  }


  /**
   * getForecastURI method is a helper method that contains the code to send a request to the nws
   * api and get back the response which is then deserialized into an object that gives us the
   * forecast field which we then create a URI from that link which is returned.
   *
   * @param lon queryParams passed in
   * @param lat queryParams passed in
   * @return
   * @throws IOException
   * @throws URISyntaxException
   * @throws InterruptedException
   */
  private URI getForecastURI(String lon, String lat)
      throws IOException, URISyntaxException, InterruptedException {
    HttpRequest pointRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.weather.gov/points/" + lat + "," + lon))
            .GET()
            .build();
    HttpResponse<String> pointResponse =
        HttpClient.newBuilder().build().send(pointRequest, BodyHandlers.ofString());
    if (pointResponse.statusCode() == 200) {

      Weather weather = deserializedWeather(pointResponse);
      System.out.println(weather);
      String forecastLink = weather.getForecastLink();
      System.out.println(forecastLink);
      return new URI(forecastLink);
    } else {
      throw new IOException();
    }
  }

  /**
   * helper method to connect to the forecast side of the nws api and get the temperature as an int
   * which is returned
   *
   * @param forecastURI
   * @return int temp
   * @throws IOException
   * @throws InterruptedException
   */
  public int getTemperature(URI forecastURI) throws IOException, InterruptedException {
    int temp = 999;
    HttpRequest forecastRequest = HttpRequest.newBuilder().uri(forecastURI).GET().build();
    HttpResponse<String> forecastResponse =
        HttpClient.newBuilder().build().send(forecastRequest, BodyHandlers.ofString());
    if (forecastResponse.statusCode() == 200) {
      Forecast myForecast = this.deserializedForecast(forecastResponse);

      temp = myForecast.getProperties().getPeriods().get(0).getTemp();
    } else {
      throw new IOException();
    }

    return temp;
  }

  public static Weather deserializedWeather(HttpResponse<String> response) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Weather> adapter = moshi.adapter(Weather.class);
    System.out.println("adapted created");
    Weather weather = adapter.fromJson(response.body());
    System.out.println(weather);
    return weather;
  }

  public static Forecast deserializedForecast(HttpResponse<String> response) throws IOException {
    Moshi moshi = new Moshi.Builder().build();

    System.out.println("adapter built");
    JsonAdapter<Forecast> adapter = moshi.adapter(Forecast.class);
    System.out.println("adapter");
    Forecast forecast = adapter.fromJson(response.body());
    return forecast;
  }
}
