package edu.brown.cs.student.CSV;

import java.util.List;

/**
 * Creates an object of type T from a List of Strings.
 *
 * @param <T> object to be created
 */
public interface CreatorFromRow<T> {

  /**
   * creates an object that from a row of CSV data
   *
   * @param row List of strings parsed from CSV data
   * @return T generic object created from the CSV data
   * @throws FactoryFailureException failed to create object
   */
  T create(List<String> row) throws FactoryFailureException;
}
