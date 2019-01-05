package org.asciidoctor.js.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reporter {

  private Map<String, List<Long>> timings = new HashMap<>();

  public void register(String name, long duration) {
    List<Long> durations = timings.computeIfAbsent(name, k -> new ArrayList<>());
    durations.add(duration);
  }

  public List<Long> get(String name) {
    return timings.get(name);
  }
}
