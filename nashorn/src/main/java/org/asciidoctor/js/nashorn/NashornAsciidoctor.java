package org.asciidoctor.js.nashorn;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.asciidoctor.js.api.Asciidoctor;
import org.asciidoctor.js.api.Reporter;
import org.asciidoctor.js.api.Resources;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

public class NashornAsciidoctor implements Asciidoctor {

  private final ScriptObjectMirror asciidoctor;
  private final Reporter reporter;

  private NashornAsciidoctor(Reporter reporter) throws IOException, URISyntaxException, ScriptException {
    this.reporter = reporter;
    ScriptEngineManager engineManager = new ScriptEngineManager();
    ScriptEngine engine = engineManager.getEngineByName("nashorn");
    String script = Resources.readScript();
    long start = System.currentTimeMillis();
    engine.eval(script);
    this.reporter.register("load", (System.currentTimeMillis() - start));
    start = System.currentTimeMillis();
    asciidoctor = (ScriptObjectMirror) engine.eval("Asciidoctor({'runtime': {'platform': 'java', 'engine': 'nashorn'}})"); // init
    this.reporter.register("instantiate", (System.currentTimeMillis() - start));
  }

  public static NashornAsciidoctor create(Reporter reporter) throws IOException, URISyntaxException, ScriptException {
    return new NashornAsciidoctor(reporter);
  }

  @Override
  public String convert(String content, Map<String, Object> options) {
    long start = System.currentTimeMillis();
    String result = asciidoctor.callMember("convert", content, options).toString();
    this.reporter.register("call-convert", (System.currentTimeMillis() - start));
    return result;
  }

  @Override
  public String asciidoctorVersion() {
    long start = System.currentTimeMillis();
    String result = asciidoctor.callMember("getVersion").toString();
    this.reporter.register("call-getVersion", (System.currentTimeMillis() - start));
    return result;
  }
}
