# jaxen
The Jaxen XPath Engine for Java

Jaxen is an open source XPath library written in Java.
It is adaptable to many different object models, including
DOM, XOM, dom4j, and JDOM. It is also possible to write
adapters that treat non-XML trees such as compiled Java byte code
or Java beans as XML, thus enabling you to query these trees with XPath too.

The current version is *1.2.0*

Version *1.2.x* is compiled with maven-toolchains-plugin to retain compatibility with Java 5..

To compile from src you will need a toolchains.xml file in your local maven root.

i.e. ~/.m2/toolchains.xml

```
<toolchains>
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>8</version>
      <vendor>oracle</vendor>
      <id>for_jaxen</id>
    </provides>
    <configuration>
      <jdkHome>/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home</jdkHome>
    </configuration>
  </toolchain>
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>10</version>
      <vendor>oracle</vendor>
      <id>for_jaxen_10</id>
    </provides>
    <configuration>
      <jdkHome>/Library/Java/JavaVirtualMachines/jdk-10.0.2.jdk/Contents/Home</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```


