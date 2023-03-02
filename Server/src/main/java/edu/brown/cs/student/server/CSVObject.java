package edu.brown.cs.student.server;

import java.util.List;

public class CSVObject {
  private transient Boolean loaded; // transient makes moshi ignore it when serializing
  private List data;

  public CSVObject(Boolean loaded, List<List<String>> Data) {
    this.loaded = loaded;
    this.data = Data;
  }

  public void setLoaded(Boolean loaded) {
    this.loaded = loaded;
  }

  public Boolean getLoaded() {
    return this.loaded;
  }

  public List getData() {
    return this.data;
  }

  public void setData(List data) {
    this.data = data;
  }
}
