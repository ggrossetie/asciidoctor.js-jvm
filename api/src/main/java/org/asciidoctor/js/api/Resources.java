package org.asciidoctor.js.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Resources {

  public static String readScript() throws IOException {
    return read("asciidoctor.js");
  }

  public static String readUserManual() throws IOException {
    return read("user-manual.adoc");
  }

  private static String read(String resourceName) throws IOException {
    InputStreamReader inputStreamReader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
    try (BufferedReader buffer = new BufferedReader(inputStreamReader)) {
      return buffer.lines().collect(Collectors.joining("\n"));
    }
  }
}
