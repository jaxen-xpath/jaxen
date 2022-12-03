# jaxen

[![Maven Central](https://img.shields.io/maven-central/v/jaxen/jaxen.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22jaxen%22%20AND%20a:%22jaxen%22)

The Jaxen XPath Engine for Java

Jaxen is an open source XPath 1.0 library written in Java.
It is adaptable to many different object models, including
DOM, XOM, dom4j, and JDOM. It is also possible to write
adapters that treat non-XML trees such as compiled Java byte code
or Java beans as XML, thus enabling you to query these trees with XPath too.

The current version is *2.0.0*. This release requires Java 1.5 or later.
If you're still using Java 1.4 or earlier, try Jaxen 1.2.0. 
If you're still using Java 1.3 or earlier, try Jaxen 1.1.6. 

## Adding Jaxen to your build

Jaxen's Maven group ID is `jaxen` and its artifact ID is `jaxen`. To add a dependency on jaxen using Maven, add this `dependency` element to your pom.xml:

```xml
<dependency>
  <groupId>jaxen</groupId>
  <artifactId>jaxen</artifactId>
  <version>2.0.0</version>
</dependency>
```

To add a dependency using Gradle:

```gradle
dependencies {
  compile 'jaxen:jaxen:2.0.0'
}
```
