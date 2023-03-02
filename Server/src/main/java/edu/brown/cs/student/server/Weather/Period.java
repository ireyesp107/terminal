package edu.brown.cs.student.server.Weather;

/** Period object for deserialization, nested in weather api json */
public class Period {
  private int temperature;

  public int getTemp() {
    return this.temperature;
  }
}
