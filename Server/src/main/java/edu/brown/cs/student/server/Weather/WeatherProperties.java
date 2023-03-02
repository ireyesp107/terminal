package edu.brown.cs.student.server.Weather;

/** Class for deserialization of weather api data to get the forecast link */
public class WeatherProperties {
  private String forecast;

  public String getForecast() {
    return forecast; // uses a method rather than a public variable to increase security
  }
}
