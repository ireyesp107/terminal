package edu.brown.cs.student.server.Weather;

import java.util.Collections;
import java.util.List;

/**
 * Properties Object that contains the list of periods that contains the temp. For deserialization
 */
public class ForecastProperties {
  private List<Period> periods;

  public List<Period> getPeriods() {
    return Collections.unmodifiableList(this.periods); // defensive programming
  }
}
