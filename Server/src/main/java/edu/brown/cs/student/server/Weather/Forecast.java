package edu.brown.cs.student.server.Weather;

/** Classe for deserialization of the forecast api that allows us to access the temperature field */
public class Forecast {
  private ForecastProperties properties;

  public ForecastProperties getProperties() {
    return this.properties;
  }
}
