package org.asciidoctor.js.graalvm;

import org.asciidoctor.js.api.Asciidoctor;
import org.asciidoctor.js.api.Reporter;
import org.asciidoctor.js.api.Resources;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

public class GraalVMAsciidoctor implements Asciidoctor {

  private final Value asciidoctor;
  private final Reporter reporter;

  private GraalVMAsciidoctor(Reporter reporter) throws IOException {
    this.reporter = reporter;
    Context context = Context.newBuilder("js").allowIO(true).build();
    String script = Resources.readScript();
    long start = System.currentTimeMillis();
    context.eval("js", script);
    this.reporter.register("load", (System.currentTimeMillis() - start));
    start = System.currentTimeMillis();
    asciidoctor = context.eval("js", "Asciidoctor({runtime: {platform: 'java', engine: 'graalvm'}})"); // init
    this.reporter.register("instantiate", (System.currentTimeMillis() - start));
  }

  private static String readScript() throws IOException {
    InputStreamReader inputStreamReader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("asciidoctor.js"));
    try (BufferedReader buffer = new BufferedReader(inputStreamReader)) {
      return buffer.lines().collect(Collectors.joining("\n"));
    }
  }

  public static GraalVMAsciidoctor create(Reporter reporter) throws IOException, URISyntaxException {
    return new GraalVMAsciidoctor(reporter);
  }

  @Override
  public String convert(String content, Map<String, Object> options) {
    long start = System.currentTimeMillis();
    String result = asciidoctor.getMember("convert").execute(content, options).asString();
    this.reporter.register("call-convert", (System.currentTimeMillis() - start));
    return result;
  }

  @Override
  public String asciidoctorVersion() {
    long start = System.currentTimeMillis();
    String result = asciidoctor.getMember("getVersion").execute().asString();
    this.reporter.register("call-getVersion", (System.currentTimeMillis() - start));
    return result;
  }

  public Value getAsciidoctor() {
    return asciidoctor;
  }
}
