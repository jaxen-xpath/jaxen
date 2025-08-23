# API Compatibility Checking

Jaxen now includes automatic API compatibility checking using the Animal Sniffer Maven plugin. This tool helps maintain API stability and prevents accidental introduction of incompatible changes.

## What It Does

The Animal Sniffer plugin provides two main functions:

1. **Java API Compatibility**: Ensures the code only uses APIs available in the target Java version (Java 1.5 for Jaxen)
2. **Build Failure on Violations**: Breaks the build when incompatible APIs are detected

## How It Works

The plugin is automatically executed during the build process:

- **During `process-classes` phase**: Checks all compiled classes against the Java 1.5 API signature
- **During `test` phase**: Performs additional API compatibility checks on the core module

## Example

If you accidentally use a newer Java API, the build will fail with an error like:

```
[ERROR] /path/to/file.java: Field someField: Undefined reference: java.nio.file.Path
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.codehaus.mojo:animal-sniffer-maven-plugin:1.23:check (check-java-1.5-api) on project jaxen: Signature errors found.
```

## Configuration

The plugin is configured in the parent `pom.xml` for all modules and includes specific rules in the core module:

### Parent POM Configuration
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

### Core Module Configuration  
The core module includes additional configuration to allow some internal packages to have more flexibility while still protecting the public API.

## Excluded Packages

Some internal implementation packages are excluded from strict checking:
- `org.jaxen.saxpath.base.*` - Internal SAXPath implementation
- `org.jaxen.pattern.*` - Deprecated pattern matching (already marked deprecated)

## Running Manually

To run the API compatibility check manually:

```bash
mvn animal-sniffer:check
```

To run all checks including tests:

```bash
mvn clean test
```

## Benefits

1. **Prevents API Regressions**: Catches accidental use of newer Java APIs
2. **Maintains Compatibility**: Ensures Jaxen remains compatible with Java 1.5+
3. **Automated Detection**: No manual review needed - violations are caught automatically
4. **Clear Error Messages**: Provides specific information about which APIs are problematic

## Continuous Integration

The plugin runs automatically in CI/CD pipelines (CircleCI), ensuring that all commits and pull requests are checked for API compatibility violations.