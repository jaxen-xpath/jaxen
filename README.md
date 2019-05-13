# jaxen

[![Maven Central](https://img.shields.io/maven-central/v/jaxen/jaxen.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22jaxen%22%20AND%20a:%22jaxen%22)

The Jaxen XPath Engine for Java

Jaxen is an open source XPath library written in Java.
It is adaptable to many different object models, including
DOM, XOM, dom4j, and JDOM. It is also possible to write
adapters that treat non-XML trees such as compiled Java byte code
or Java beans as XML, thus enabling you to query these trees with XPath too.

The current version is *1.2.0*. This release requires Java 1.4 or later.
If you're still using Java 1.3 or earlier, try Jaxen 1.1.6. 

## Adding Jaxen to your build

Jaxen's Maven group ID is `jaxen` and its artifact ID is `jaxen`. To add a dependency on jaxen using Maven, add this `dependency` element to your pom.xml:

```xml
<dependency>
  <groupId>jaxen</groupId>
  <artifactId>jaxen</artifactId>
  <version>1.2.0</version>
</dependency>
```

To add a dependency using Gradle:

```gradle
dependencies {
  compile 'jaxen:jaxen:1.2.0'
}
```

Jaxen was one of the earliest adopters of Maven, before a lot of practices had gelled. Consequently there are cycles in its dependency tree. Cleaning this up will be a major focus of Jaxen 2.0. 

