package edu.brown.cs.student.CSV;

import java.util.List;

/** Generic string list creator type, can be substituted for lambda expression row -> row */
public class listStringFactory implements CreatorFromRow<List<String>> {
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
