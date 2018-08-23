module jaxen {
  requires java.xml;
  requires jdom;
  requires dom4j;
  requires xom;
  exports org.jaxen;
  exports org.jaxen.dom;
  exports org.jaxen.dom4j;
  exports org.jaxen.expr;
  exports org.jaxen.expr.iter;
  exports org.jaxen.function;
  exports org.jaxen.function.ext;
  exports org.jaxen.function.xslt;
  exports org.jaxen.javabean;
  exports org.jaxen.jdom;
  exports org.jaxen.pattern;
  exports org.jaxen.saxpath;
  exports org.jaxen.saxpath.base;
  exports org.jaxen.saxpath.helpers;
  exports org.jaxen.util;
  exports org.jaxen.xom;
}