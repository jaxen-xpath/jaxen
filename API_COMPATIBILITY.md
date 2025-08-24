# API Compatibility Checking

Jaxen includes comprehensive API compatibility checking using two complementary tools that detect different types of API violations and break the build when issues are found.

## What It Does

Jaxen's API compatibility checking provides three main functions:

1. **Java API Compatibility**: Ensures the code only uses APIs available in the target Java version (Java 1.5 for Jaxen)
2. **Jaxen Public API Protection**: Detects breaking changes to jaxen's own public API compared to the latest release
3. **Build Failure on Violations**: Breaks the build when any API compatibility issues are detected

## Tools Used

### 1. Animal Sniffer Plugin
- **Purpose**: Checks for usage of Java APIs not available in Java 1.5
- **When it runs**: During `process-classes` and `test` phases
- **What it catches**: Accidental use of newer Java APIs (e.g., `java.nio.file.Path`)

### 2. Japicmp Plugin  
- **Purpose**: Detects changes to jaxen's own public API
- **When it runs**: During `verify` phase
- **What it catches**: Breaking changes to jaxen's public API (method removals, signature changes, etc.)

## How It Works

The plugins are automatically executed during the build process:

### Animal Sniffer Plugin
- **During `process-classes` phase**: Checks all compiled classes against the Java 1.5 API signature
- **During `test` phase**: Performs additional API compatibility checks on the core module

### Japicmp Plugin
- **During `verify` phase**: Compares the current build against jaxen 2.0.0 to detect API changes
- **Scope**: Only checks public API in core jaxen packages (excludes internal implementation packages)

## Examples

### Java API Violation (Animal Sniffer)
If you accidentally use a newer Java API, the build will fail with an error like:

```
[ERROR] /path/to/file.java: Field someField: Undefined reference: java.nio.file.Path
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.codehaus.mojo:animal-sniffer-maven-plugin:1.23:check (check-java-1.5-api) on project jaxen: Signature errors found.
```

### Jaxen API Change (Japicmp)
If you modify jaxen's public API incompatibly, the build will fail with an error like:

```
[ERROR] /path/to/file.java: Method removed: public void someMethod()
[INFO] ------------------------------------------------------------------------  
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal com.github.siom79.japicmp:japicmp-maven-plugin:0.23.1:cmp (check-jaxen-api-compatibility) on project jaxen: There are binary incompatible changes.
```

## Configuration

### Animal Sniffer Plugin (Java API Checking)

The Animal Sniffer plugin is configured in the parent `pom.xml` for all modules and includes specific rules in the core module:

#### Parent POM Configuration
```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>animal-sniffer-maven-plugin</artifactId>
  <version>1.23</version>
  <configuration>
    <signature>
      <groupId>org.codehaus.mojo.signature</groupId>
      <artifactId>java15</artifactId>
      <version>1.0</version>
    </signature>
  </configuration>
  <executions>
    <execution>
      <id>check-java-1.5-api</id>
      <phase>process-classes</phase>
      <goals>
        <goal>check</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

#### Core Module Configuration  
The core module includes additional configuration to allow some internal packages to have more flexibility while still protecting the public API.

## Excluded Packages

Some internal implementation packages are excluded from strict checking:
- `org.jaxen.saxpath.base.*` - Internal SAXPath implementation
- `org.jaxen.pattern.*` - Deprecated pattern matching (already marked deprecated)

### Japicmp Plugin (Jaxen API Change Detection)

The Japicmp plugin is configured in the core module to detect changes to jaxen's public API:

```xml
<plugin>
  <groupId>com.github.siom79.japicmp</groupId>
  <artifactId>japicmp-maven-plugin</artifactId>
  <version>0.23.1</version>
  <executions>
    <execution>
      <id>check-jaxen-api-compatibility</id>
      <phase>verify</phase>
      <goals>
        <goal>cmp</goal>
      </goals>
      <configuration>
        <parameter>
          <breakBuildOnModifications>true</breakBuildOnModifications>
          <breakBuildOnBinaryIncompatibleModifications>true</breakBuildOnBinaryIncompatibleModifications>
          <breakBuildOnSourceIncompatibleModifications>true</breakBuildOnSourceIncompatibleModifications>
          <accessModifier>public</accessModifier>
        </parameter>
        <oldVersion>
          <dependency>
            <groupId>jaxen</groupId>
            <artifactId>jaxen</artifactId>
            <version>2.0.0</version>
          </dependency>
        </oldVersion>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Protected Packages
The plugin focuses on public API packages and excludes internal implementation:
- **Included**: `org.jaxen.*` (main public API)
- **Excluded**: Internal packages like `org.jaxen.saxpath.base.*`, `org.jaxen.pattern.*`, `org.jaxen.util.*`, and navigator implementations

## Running Manually

### Java API Compatibility Check
To run the Java API compatibility check manually:

```bash
mvn animal-sniffer:check
```

### Jaxen API Compatibility Check  
To run the jaxen API compatibility check manually:

```bash
mvn japicmp:cmp
```

### All Checks Including Tests
To run all checks including tests:

```bash
mvn clean verify
```

## Benefits

1. **Prevents Java API Regressions**: Catches accidental use of newer Java APIs
2. **Protects Jaxen's Public API**: Detects breaking changes to jaxen's own API
3. **Maintains Compatibility**: Ensures Jaxen remains compatible with Java 1.5+ and existing client code
4. **Automated Detection**: No manual review needed - violations are caught automatically
5. **Clear Error Messages**: Provides specific information about which APIs are problematic

## Continuous Integration

Both plugins run automatically in CI/CD pipelines (CircleCI), ensuring that all commits and pull requests are checked for:
- Java API compatibility violations
- Breaking changes to jaxen's public API

## API Change Guidelines

When the Japicmp plugin detects API changes, consider these guidelines:

### Allowed Changes (Won't Break Build)
- Adding new public methods/classes
- Adding optional parameters with defaults
- Expanding method visibility (private → public)

### Breaking Changes (Will Break Build) 
- Removing public methods/classes
- Changing method signatures
- Reducing method visibility (public → private)
- Changing return types
- Adding checked exceptions

### Workarounds for Legitimate API Changes
If you need to make breaking changes for a new major version:

1. **Temporary Disable**: Add `<skip>true</skip>` to the japicmp plugin configuration
2. **Update Baseline**: Change the `<version>2.0.0</version>` to the current release version
3. **Document Changes**: Update release notes with breaking changes