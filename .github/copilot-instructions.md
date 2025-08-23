# Jaxen XPath Engine for Java

Jaxen is an open source XPath 1.0 library written in Java. It is adaptable to many different object models, including DOM, XOM, dom4j, and JDOM. This is a multi-module Maven project targeting Java 1.5+ compatibility.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Build and Test Requirements
- **Maven**: 3.6.3 or later (enforced by build)
- **Java**: JDK 8 required (Java 1.5 configured in pom.xml FAILS on modern JDKs)
- **Internet**: Required for Maven dependencies and JavaDoc generation

### Essential Build Commands with Timing
NEVER CANCEL builds or tests. All commands below include validated timing and required workarounds.

#### Basic Development Workflow
```bash
# Set Java 8 environment (REQUIRED - modern JDK 17+ will fail)
export JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64

# Clean and compile - takes ~3 seconds
mvn clean compile

# Run tests - takes ~6 seconds, NEVER CANCEL (runs 714 tests total)
mvn test

# Package without JavaDoc (avoids network issues) - takes ~11 seconds
mvn package -Dmaven.javadoc.skip=true
```

#### Full Build with All Artifacts
```bash
# Complete build - takes ~20 seconds, NEVER CANCEL
# Note: May fail if JavaDoc cannot access external URLs
export JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64
mvn clean package
```

#### Critical Java Compatibility Issue
The project pom.xml specifies Java 1.5 source/target, but modern JDKs (17+) no longer support Java 1.5. You MUST use Java 8:
- **REQUIRED**: `export JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64` (or your Java 8 path)
- Without Java 8, builds will fail with "Source option 5 is no longer supported"
- Command-line compiler overrides do NOT work due to explicit pom.xml configuration

### Project Structure
```
jaxen/                          # Root directory
├── .circleci/config.yml        # CI configuration (runs mvn test only)
├── .github/                    # GitHub configuration
├── core/                       # Main Jaxen library module
│   ├── src/java/main/          # Main source code
│   │   └── org/jaxen/          # Core XPath engine
│   ├── src/java/test/          # Unit tests (85 test classes)
│   └── src/java/samples/       # Demo code (DOMDemo, Dom4jDemo, JDOMDemo)
├── integration-tests/          # Integration test module
│   ├── src/java/test/          # Navigator tests for DOM/JDOM/DOM4J/XOM
│   └── xml/                    # Test XML files
├── pom.xml                     # Parent Maven configuration
├── README.md                   # Basic project info
└── RELEASING.md                # Release process documentation
```

## Validation and Testing

### Always Run These Before Committing
```bash
# Set Java 8 environment first
export JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64

# 1. Clean build with tests - takes ~10 seconds total, NEVER CANCEL
mvn clean test

# 2. Package verification - takes ~11 seconds, NEVER CANCEL  
mvn package -Dmaven.javadoc.skip=true
```

### Manual Functional Validation
After making changes to XPath functionality, ALWAYS test core functionality manually:

```bash
# Create and run functional test
cat > /tmp/test_xpath.java << 'EOF'
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.jaxen.dom.DOMXPath;

public class test_xpath {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new java.io.ByteArrayInputStream(
            "<root><item id='1'>First</item><item id='2'>Second</item></root>".getBytes()));
        
        // Test basic XPath selection
        DOMXPath xpath = new DOMXPath("//item[@id='2']");
        Object result = xpath.selectSingleNode(doc);
        System.out.println(result != null ? "✓ XPath selection works" : "✗ XPath failed");
        
        // Test XPath functions
        DOMXPath countXPath = new DOMXPath("count(//item)");
        Number count = (Number) countXPath.evaluate(doc);
        System.out.println(count.intValue() == 2 ? "✓ XPath functions work" : "✗ Functions failed");
    }
}
EOF

# Compile and run test
export JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64
javac -cp "core/target/classes:$(find ~/.m2/repository -name '*.jar' | tr '\n' ':')" /tmp/test_xpath.java -d /tmp/
java -cp "/tmp:core/target/classes:$(find ~/.m2/repository -name '*.jar' | tr '\n' ':')" test_xpath
```

### Test Suites Information
- **Core tests**: 85 test classes covering XPath engine functionality  
- **Integration tests**: Navigator implementations for DOM, JDOM, DOM4J, XOM
- **Total test runtime**: ~6 seconds, runs 714 tests (447 core + 267 integration)
- **Test execution**: Uses JUnit 3.8.2 with custom test framework

## Common Development Tasks

### Working with XPath Engine Code
Key packages to understand:
- `org.jaxen` - Core interfaces (XPath, Navigator, Context)
- `org.jaxen.expr` - XPath expression implementations  
- `org.jaxen.function` - XPath function implementations
- `org.jaxen.dom` - DOM navigator implementation (most commonly used)
- `org.jaxen.pattern` - XPath pattern matching (deprecated)

### Adding New XPath Functions
1. Implement `org.jaxen.Function` interface
2. Add to `SimpleFunctionContext` or custom function context
3. Add unit tests in `core/src/java/test/org/jaxen/test/`
4. Run full test suite to ensure compatibility

### Debugging Failed Tests
```bash
# Set Java 8 environment first
export JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64

# Run specific test class
mvn test -Dtest=YourTestClass

# Run tests with verbose output
mvn test -X
```

## Build Analysis and Troubleshooting

### Common Build Issues
1. **"Source option 5 is no longer supported"**
   - Solution: Set `export JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64` (or your Java 8 path)
   - Command-line properties don't work due to explicit pom.xml configuration
   
2. **JavaDoc generation fails**
   - Solution: Add `-Dmaven.javadoc.skip=true` to skip JavaDoc generation
   - Cause: Network access issues to Oracle documentation URLs
   
3. **Test failures in integration-tests**
   - Verify all XML object model dependencies are available (JDOM, DOM4J, XOM)
   - Check XML test files in `integration-tests/xml/` are accessible

### Dependency Information
- **JUnit**: 3.8.2 (older version, affects test writing style)
- **Optional dependencies**: JDOM 1.1.3, DOM4J 1.6.1, XOM 1.3.8
- **Build plugins**: Maven Bundle Plugin 5.1.9, Surefire 3.5.1

### Performance Expectations
- **Clean build**: ~3 seconds
- **Test execution**: ~6 seconds (NEVER CANCEL) - runs 714 tests
- **Full package**: ~11-20 seconds (NEVER CANCEL)
- **Maven dependency download**: ~30 seconds first time

## CI/CD Integration

The project uses CircleCI with a simple configuration:
- **Build command**: `mvn test` (no compiler overrides in CI)
- **Java version**: OpenJDK 8 (cimg/openjdk:8.0.322)
- **Maven cache**: Enabled for dependencies

If CI fails but local builds work, verify the Java compatibility flags are properly set.

## Release Process

See `RELEASING.md` for complete release instructions. Key requirements:
- GPG credentials for signing
- Sonatype OSSRH permissions
- Version management via GitHub PRs

## Key Files for Reference

### Frequently Modified Files
- `core/src/java/main/org/jaxen/BaseXPath.java` - Main XPath implementation
- `core/src/java/main/org/jaxen/Navigator.java` - Object model navigation interface
- `core/src/java/main/org/jaxen/function/` - XPath function implementations
- `integration-tests/xml/` - XML test files for validation

### Configuration Files  
- `pom.xml` - Maven build configuration (parent)
- `core/pom.xml` - Core module configuration
- `integration-tests/pom.xml` - Integration test configuration
- `.circleci/config.yml` - CI configuration

Always validate your changes work correctly by running the full test suite and manual functional tests before committing.