package edu.brown.cs.student.CSV;

import java.util.List;

/** Exception thrown when a Factory class fails. */
public class FactoryFailureException extends Exception {
  final List<String> row;

  /**
   * Factory class failure when creating from a row in csvParser
   *
   * @param row row of CSV data when called in csvParser
   */
  public FactoryFailureException(List<String> row) {
    this.row = row;
  }
}
