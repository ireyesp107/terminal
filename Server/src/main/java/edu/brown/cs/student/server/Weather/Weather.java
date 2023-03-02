package edu.brown.cs.student.server.Weather;

/**
 * Weather class for deserialization the weather api from longitude and latitude coordinates object
 * that will be able to get the link to get to the forecast api
 */
public class Weather {
  private WeatherProperties properties;

  public String getForecastLink() {
    return properties.getForecast();
  }
}
