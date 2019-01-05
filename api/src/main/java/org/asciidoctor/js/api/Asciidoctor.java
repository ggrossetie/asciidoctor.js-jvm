package org.asciidoctor.js.api;

import java.util.Map;

public interface Asciidoctor {

  String convert(String content, Map<String, Object> options);

  String asciidoctorVersion();
}
