package org.jaxen.test;

import org.jaxen.JaxenException;
import org.jaxen.pattern.AnyNodeTest;
import org.jaxen.pattern.LocationPathPattern;
import org.jaxen.pattern.NameTest;
import org.jaxen.pattern.NamespaceTest;
import org.jaxen.pattern.NodeTest;
import org.jaxen.pattern.NodeTypeTest;
import org.jaxen.pattern.Pattern;
import org.jaxen.pattern.PatternParser;
import org.jaxen.pattern.TextNodeTest;
import org.jaxen.pattern.UnionPattern;

import junit.framework.TestCase;

public class PatternTest extends TestCase {

    public PatternTest(String name) {
        super(name);
    }

    public void testAnyNode() throws Exception {
        Pattern pattern = PatternParser.parse("*");
        assertEquals("child()", pattern.getText());
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals(Pattern.ELEMENT_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
        assertNull(pattern.getMatchesNodeName());
        assertNull(pattern.getUnionPatterns());
    }

    public void testNamePattern() throws Exception {
        Pattern pattern = PatternParser.parse("foo");
        assertTrue(pattern instanceof NameTest);
        assertEquals("foo", pattern.getText());
        assertEquals(Pattern.ELEMENT_NODE, pattern.getMatchType());
        assertEquals(0.0, pattern.getPriority(), 0.001);
        assertNull(pattern.getMatchesNodeName());
    }

    public void testAttributeName() throws Exception {
        Pattern pattern = PatternParser.parse("@foo");
        assertTrue(pattern instanceof NameTest);
        assertEquals("@foo", pattern.getText());
        assertEquals(Pattern.ATTRIBUTE_NODE, pattern.getMatchType());
        assertEquals(0.0, pattern.getPriority(), 0.001);
    }

    public void testNamespacePattern() throws Exception {
        Pattern pattern = PatternParser.parse("pre:foo");
        assertTrue(pattern instanceof NameTest);
        assertEquals("foo", pattern.getText());
        assertEquals(Pattern.ELEMENT_NODE, pattern.getMatchType());
    }

    public void testNamespaceWildcard() throws Exception {
        Pattern pattern = PatternParser.parse("pre:*");
        assertTrue(pattern instanceof NamespaceTest);
        assertEquals("pre:", pattern.getText());
        assertEquals(-0.25, pattern.getPriority(), 0.001);
    }

    public void testGetTextOfNamespaceWildcard() throws Exception {
        Pattern pattern = PatternParser.parse("pre:*");
        assertEquals("pre:", pattern.getText());
    }

    public void testAttributeWildcard() throws Exception {
        Pattern pattern = PatternParser.parse("@*");
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals("@*", pattern.getText());
        assertEquals(Pattern.ATTRIBUTE_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
    }

    public void testTextNode() throws Exception {
        Pattern pattern = PatternParser.parse("text()");
        assertTrue(pattern instanceof TextNodeTest);
        assertEquals("text()", pattern.getText());
        assertEquals(Pattern.TEXT_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
    }

    public void testCommentNode() throws Exception {
        Pattern pattern = PatternParser.parse("comment()");
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals("comment()", pattern.getText());
        assertEquals(Pattern.COMMENT_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
    }

    public void testProcessingInstruction() throws Exception {
        Pattern pattern = PatternParser.parse("processing-instruction()");
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals("processing-instruction()", pattern.getText());
        assertEquals(Pattern.PROCESSING_INSTRUCTION_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
    }

    public void testTextRoundTrip() throws Exception {
        String[] patterns = {
            "foo",
            "@foo",
            "@*",
            "text()",
            "comment()",
            "processing-instruction()",
            "foo/bar",
        };
        for (String p : patterns) {
            Pattern pattern = PatternParser.parse(p);
            assertEquals("round-trip failed for: " + p, p, pattern.getText());
        }
    }

    public void testGetTextOfWildcard() throws Exception {
        Pattern pattern = PatternParser.parse("*");
        assertEquals("child()", pattern.getText());
    }

    public void testGetTextOfDescendantPath() throws Exception {
        Pattern pattern = PatternParser.parse("foo//bar");
        assertEquals("foo/child()//bar", pattern.getText());
    }

    public void testGetTextOfAbsolutePath() throws Exception {
        Pattern pattern = PatternParser.parse("/foo/bar");
        assertEquals("//foo/bar", pattern.getText());
    }

    public void testGetTextOfUnionPattern() throws Exception {
        Pattern pattern = PatternParser.parse("foo | bar");
        assertEquals("foo | bar", pattern.getText());
    }

    public void testGetTextOfPredicate() throws Exception {
        Pattern pattern = PatternParser.parse("foo[1]");
        assertEquals("foo[[1.0]]", pattern.getText());
    }

    public void testGetTextOfPredicateWithPath() throws Exception {
        Pattern pattern = PatternParser.parse("foo[@bar]");
        assertEquals("foo[[attribute::bar]]", pattern.getText());
    }

    public void testGetTextOfUnionWithPredicate() throws Exception {
        Pattern pattern = PatternParser.parse("a[1] | b[2]");
        assertEquals("a[[1.0]] | b[[2.0]]", pattern.getText());
    }

    public void testChildPath() throws Exception {
        Pattern pattern = PatternParser.parse("foo/bar");
        assertTrue("expected LocationPathPattern but got " + pattern.getClass().getName(),
            pattern instanceof LocationPathPattern);
        assertEquals("foo/bar", pattern.getText());
    }

    public void testDescendantPath() throws Exception {
        Pattern pattern = PatternParser.parse("foo//bar");
        assertTrue(pattern instanceof LocationPathPattern);
        assertEquals("foo/child()//bar", pattern.getText());
    }

    public void testAbsolutePath() throws Exception {
        Pattern pattern = PatternParser.parse("/foo/bar");
        assertTrue(pattern instanceof LocationPathPattern);
        assertEquals("//foo/bar", pattern.getText());
    }

    public void testRootPattern() throws Exception {
        Pattern pattern = PatternParser.parse("/");
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals("/", pattern.getText());
        assertEquals(Pattern.DOCUMENT_NODE, pattern.getMatchType());
    }

    public void testUnionPattern() throws Exception {
        Pattern pattern = PatternParser.parse("foo | bar");
        assertTrue("expected UnionPattern but got " + pattern.getClass().getName(),
            pattern instanceof UnionPattern);
        assertEquals("foo | bar", pattern.getText());
        Pattern[] unionPatterns = pattern.getUnionPatterns();
        assertNotNull(unionPatterns);
        assertEquals(2, unionPatterns.length);
        assertTrue(unionPatterns[0] instanceof NameTest);
        assertEquals("foo", unionPatterns[0].getText());
        assertTrue(unionPatterns[1] instanceof NameTest);
        assertEquals("bar", unionPatterns[1].getText());
    }

    public void testUnionWithPath() throws Exception {
        Pattern pattern = PatternParser.parse("foo/bar | baz");
        assertEquals("foo/bar | baz", pattern.getText());
        assertTrue(pattern instanceof UnionPattern);
    }

    public void testPredicate() throws Exception {
        Pattern pattern = PatternParser.parse("foo[1]");
        assertEquals("foo[[1.0]]", pattern.getText());
    }

    public void testPredicateWithPath() throws Exception {
        Pattern pattern = PatternParser.parse("foo[@bar]");
        assertEquals("foo[[attribute::bar]]", pattern.getText());
    }

    public void testMultiStepPath() throws Exception {
        Pattern pattern = PatternParser.parse("a/b/c");
        assertTrue(pattern instanceof LocationPathPattern);
        assertEquals("a/b/c", pattern.getText());
    }

    public void testDeepPath() throws Exception {
        Pattern pattern = PatternParser.parse("a/b/c/d/e");
        assertEquals("a/b/c/d/e", pattern.getText());
    }

    public void testUnionOfThreePatterns() throws Exception {
        Pattern pattern = PatternParser.parse("a | b | c");
        assertTrue(pattern instanceof UnionPattern);
        assertEquals("a | b | c", pattern.getText());
    }

    public void testUnionWithPredicate() throws Exception {
        Pattern pattern = PatternParser.parse("a[1] | b[2]");
        assertTrue(pattern instanceof UnionPattern);
        assertEquals("a[[1.0]] | b[[2.0]]", pattern.getText());
    }

}
