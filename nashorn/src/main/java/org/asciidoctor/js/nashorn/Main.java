package org.asciidoctor.js.nashorn;

import org.asciidoctor.js.api.Reporter;
import org.asciidoctor.js.api.Resources;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class Main {

  public static void main(String[] args) throws ScriptException, IOException, URISyntaxException {
    System.out.println("-- Nashorn");
    System.out.println("warmup");
    int warmupCount = 5;
    int createCount = 5;
    int callCount = 10;
    for (int i = 0; i < warmupCount; i++) {
      // warmup
      NashornAsciidoctor.create(new Reporter()).asciidoctorVersion();
    }
    Reporter reporter = new Reporter();

    System.out.println("start");
    for (int i = 0; i < createCount; i++) {
      System.out.println("Run create#" + (i + 1));
      NashornAsciidoctor.create(reporter);
    }
    System.out.println("load: " + reporter.get("load"));
    System.out.println("instantiate: " + reporter.get("instantiate"));

    String content = Resources.readUserManual();
    NashornAsciidoctor instance = NashornAsciidoctor.create(reporter);
    for (int i = 0; i < callCount; i++) {
      System.out.println("Run call#" + (i + 1));
      instance.asciidoctorVersion();
      instance.convert(content, new HashMap<>());
    }
    System.out.println("convert: " + reporter.get("call-convert"));
    System.out.println("getVersion: " + reporter.get("call-getVersion"));
  }
}
