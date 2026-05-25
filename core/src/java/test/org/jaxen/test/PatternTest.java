package org.jaxen.test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.dom.DocumentNavigator;
import org.jaxen.pattern.AnyNodeTest;
import org.jaxen.pattern.LocationPathPattern;
import org.jaxen.pattern.NameTest;
import org.jaxen.pattern.NamespaceTest;
import org.jaxen.pattern.NodeTypeTest;
import org.jaxen.pattern.Pattern;
import org.jaxen.pattern.PatternParser;
import org.jaxen.pattern.TextNodeTest;
import org.jaxen.pattern.UnionPattern;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import junit.framework.TestCase;

public class PatternTest extends TestCase {

    private Context context;

    public PatternTest(String name) {
        super(name);
    }

    public void setUp() {
        ContextSupport support = new ContextSupport(
            null, null, null, DocumentNavigator.getInstance());
        context = new Context(support);
    }

    // --- Parsing and metadata tests ---

    public void testAnyNode() throws SAXPathException {
        Pattern pattern = PatternParser.parse("*");
        assertEquals("child()", pattern.getText());
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals(Pattern.ELEMENT_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
        assertNull(pattern.getMatchesNodeName());
        assertNull(pattern.getUnionPatterns());
    }

    public void testNamePattern() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo");
        assertTrue(pattern instanceof NameTest);
        assertEquals("foo", pattern.getText());
        assertEquals(Pattern.ELEMENT_NODE, pattern.getMatchType());
        assertEquals(0.0, pattern.getPriority(), 0.001);
        assertNull(pattern.getMatchesNodeName());
    }

    public void testAttributeName() throws SAXPathException {
        Pattern pattern = PatternParser.parse("@foo");
        assertTrue(pattern instanceof NameTest);
        assertEquals("@foo", pattern.getText());
        assertEquals(Pattern.ATTRIBUTE_NODE, pattern.getMatchType());
        assertEquals(0.0, pattern.getPriority(), 0.001);
    }

    public void testNamespacePattern() throws SAXPathException {
        Pattern pattern = PatternParser.parse("pre:foo");
        assertTrue(pattern instanceof NameTest);
        assertEquals("foo", pattern.getText());
        assertEquals(Pattern.ELEMENT_NODE, pattern.getMatchType());
    }

    public void testNamespaceWildcard() throws SAXPathException {
        Pattern pattern = PatternParser.parse("pre:*");
        assertTrue(pattern instanceof NamespaceTest);
        assertEquals("pre:", pattern.getText());
        assertEquals(-0.25, pattern.getPriority(), 0.001);
    }

    public void testAttributeWildcard() throws SAXPathException {
        Pattern pattern = PatternParser.parse("@*");
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals("@*", pattern.getText());
        assertEquals(Pattern.ATTRIBUTE_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
    }

    public void testTextNode() throws SAXPathException {
        Pattern pattern = PatternParser.parse("text()");
        assertTrue(pattern instanceof TextNodeTest);
        assertEquals("text()", pattern.getText());
        assertEquals(Pattern.TEXT_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
    }

    public void testCommentNode() throws SAXPathException {
        Pattern pattern = PatternParser.parse("comment()");
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals("comment()", pattern.getText());
        assertEquals(Pattern.COMMENT_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
    }

    public void testProcessingInstruction() throws SAXPathException {
        Pattern pattern = PatternParser.parse("processing-instruction()");
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals("processing-instruction()", pattern.getText());
        assertEquals(Pattern.PROCESSING_INSTRUCTION_NODE, pattern.getMatchType());
        assertEquals(-0.5, pattern.getPriority(), 0.001);
    }

    public void testTextRoundTrip() throws SAXPathException {
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

    public void testGetTextOfWildcard() throws SAXPathException {
        Pattern pattern = PatternParser.parse("*");
        assertEquals("child()", pattern.getText());
    }

    public void testGetTextOfDescendantPath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo//bar");
        assertEquals("foo/child()//bar", pattern.getText());
    }

    public void testGetTextOfAbsolutePath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("/foo/bar");
        assertEquals("//foo/bar", pattern.getText());
    }

    public void testGetTextOfUnionPattern() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo | bar");
        assertEquals("foo | bar", pattern.getText());
    }

    public void testGetTextOfPredicate() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo[1]");
        assertEquals("foo[[1.0]]", pattern.getText());
    }

    public void testGetTextOfPredicateWithPath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo[@bar]");
        assertEquals("foo[[attribute::bar]]", pattern.getText());
    }

    public void testGetTextOfUnionWithPredicate() throws SAXPathException {
        Pattern pattern = PatternParser.parse("a[1] | b[2]");
        assertEquals("a[[1.0]] | b[[2.0]]", pattern.getText());
    }

    public void testChildPath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo/bar");
        assertTrue("expected LocationPathPattern but got " + pattern.getClass().getName(),
            pattern instanceof LocationPathPattern);
        assertEquals("foo/bar", pattern.getText());
    }

    public void testDescendantPath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo//bar");
        assertTrue(pattern instanceof LocationPathPattern);
        assertEquals("foo/child()//bar", pattern.getText());
    }

    public void testAbsolutePath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("/foo/bar");
        assertTrue(pattern instanceof LocationPathPattern);
        assertEquals("//foo/bar", pattern.getText());
    }

    public void testRootPattern() throws SAXPathException {
        Pattern pattern = PatternParser.parse("/");
        assertTrue(pattern instanceof NodeTypeTest);
        assertEquals("/", pattern.getText());
        assertEquals(Pattern.DOCUMENT_NODE, pattern.getMatchType());
    }

    public void testUnionPattern() throws SAXPathException {
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

    public void testUnionWithPath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo/bar | baz");
        assertEquals("foo/bar | baz", pattern.getText());
        assertTrue(pattern instanceof UnionPattern);
    }

    public void testPredicate() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo[1]");
        assertEquals("foo[[1.0]]", pattern.getText());
    }

    public void testPredicateWithPath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("foo[@bar]");
        assertEquals("foo[[attribute::bar]]", pattern.getText());
    }

    public void testMultiStepPath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("a/b/c");
        assertTrue(pattern instanceof LocationPathPattern);
        assertEquals("a/b/c", pattern.getText());
    }

    public void testDeepPath() throws SAXPathException {
        Pattern pattern = PatternParser.parse("a/b/c/d/e");
        assertEquals("a/b/c/d/e", pattern.getText());
    }

    public void testUnionOfThreePatterns() throws SAXPathException {
        Pattern pattern = PatternParser.parse("a | b | c");
        assertTrue(pattern instanceof UnionPattern);
        assertEquals("a | b | c", pattern.getText());
    }

    public void testUnionWithPredicate() throws SAXPathException {
        Pattern pattern = PatternParser.parse("a[1] | b[2]");
        assertTrue(pattern instanceof UnionPattern);
        assertEquals("a[[1.0]] | b[[2.0]]", pattern.getText());
    }

    // --- matches() tests ---

    private Document buildDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("root");
        doc.appendChild(root);

        Element a = doc.createElement("a");
        root.appendChild(a);

        Element b = doc.createElement("b");
        b.setAttribute("id", "x");
        a.appendChild(b);

        Element c = doc.createElement("c");
        c.appendChild(doc.createTextNode("hello"));
        a.appendChild(c);

        Comment comment = doc.createComment("a comment");
        a.appendChild(comment);

        ProcessingInstruction pi = doc.createProcessingInstruction("target", "data");
        a.appendChild(pi);

        Element d = doc.createElement("d");
        d.setAttributeNS("http://example.com/ns", "pre:attr", "val");
        root.appendChild(d);

        Element preEl = doc.createElementNS("http://example.com/ns", "pre:el");
        root.appendChild(preEl);

        return doc;
    }

    // --- AnyNodeTest matches ---

    public void testAnyNodeTestMatchesElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        AnyNodeTest pattern = AnyNodeTest.getInstance();
        assertTrue(pattern.matches(doc.getDocumentElement(), context));
    }

    public void testAnyNodeTestMatchesDocument() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        AnyNodeTest pattern = AnyNodeTest.getInstance();
        assertTrue(pattern.matches(doc, context));
    }

    public void testAnyNodeTestMatchesAttribute() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        AnyNodeTest pattern = AnyNodeTest.getInstance();
        Element b = (Element) doc.getDocumentElement()
            .getElementsByTagName("b").item(0);
        assertTrue(pattern.matches(b.getAttributeNode("id"), context));
    }

    public void testAnyNodeTestMatchesText() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        AnyNodeTest pattern = AnyNodeTest.getInstance();
        Element c = (Element) doc.getDocumentElement()
            .getElementsByTagName("c").item(0);
        assertTrue(pattern.matches(c.getFirstChild(), context));
    }

    public void testAnyNodeTestMatchesComment() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        AnyNodeTest pattern = AnyNodeTest.getInstance();
        Element a = (Element) doc.getDocumentElement()
            .getElementsByTagName("a").item(0);
        Comment comment = (Comment) a.getChildNodes()
            .item(2);
        assertTrue(pattern.matches(comment, context));
    }

    // --- NameTest matches ---

    public void testNameTestMatchesElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("a");
        Element a = (Element) doc.getDocumentElement()
            .getElementsByTagName("a").item(0);
        assertTrue("pattern 'a' should match element <a>", pattern.matches(a, context));
    }

    public void testNameTestDoesNotMatchDifferentElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("a");
        Element b = (Element) doc.getDocumentElement()
            .getElementsByTagName("b").item(0);
        assertFalse("pattern 'a' should not match element <b>", pattern.matches(b, context));
    }

    public void testNameTestDoesNotMatchTextNode() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("c");
        Element c = (Element) doc.getDocumentElement()
            .getElementsByTagName("c").item(0);
        assertFalse("pattern 'c' should not match text node",
            pattern.matches(c.getFirstChild(), context));
    }

    public void testNameTestMatchesAttribute() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("@id");
        Element b = (Element) doc.getDocumentElement()
            .getElementsByTagName("b").item(0);
        assertTrue("pattern '@id' should match attribute id",
            pattern.matches(b.getAttributeNode("id"), context));
    }

    public void testNameTestDoesNotMatchDifferentAttribute() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("@id");
        Element a = (Element) doc.getDocumentElement()
            .getElementsByTagName("a").item(0);
        // <a> has no attributes
        assertNull(a.getAttributes().item(0));
    }

    // --- NodeTypeTest matches ---

    public void testNodeTypeTestElementMatchesElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("*");
        Element a = (Element) doc.getDocumentElement()
            .getElementsByTagName("a").item(0);
        assertTrue(pattern.matches(a, context));
    }

    public void testNodeTypeTestElementDoesNotMatchText() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("*");
        Element c = (Element) doc.getDocumentElement()
            .getElementsByTagName("c").item(0);
        assertFalse("pattern 'child()' should not match text node",
            pattern.matches(c.getFirstChild(), context));
    }

    public void testNodeTypeTestAttributeMatchesAttribute() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("@*");
        Element b = (Element) doc.getDocumentElement()
            .getElementsByTagName("b").item(0);
        assertTrue("pattern '@*' should match attribute id",
            pattern.matches(b.getAttributeNode("id"), context));
    }

    public void testNodeTypeTestAttributeDoesNotMatchElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("@*");
        assertFalse("pattern '@*' should not match element",
            pattern.matches(doc.getDocumentElement(), context));
    }

    public void testTextNodeTestMatchesText() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("text()");
        Element c = (Element) doc.getDocumentElement()
            .getElementsByTagName("c").item(0);
        assertTrue("text() should match text node",
            pattern.matches(c.getFirstChild(), context));
    }

    public void testTextNodeTestDoesNotMatchElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("text()");
        assertFalse("text() should not match element",
            pattern.matches(doc.getDocumentElement(), context));
    }

    public void testCommentTestMatchesComment() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("comment()");
        Element a = (Element) doc.getDocumentElement()
            .getElementsByTagName("a").item(0);
        Comment comment = (Comment) a.getChildNodes()
            .item(2);
        assertTrue("comment() should match comment node",
            pattern.matches(comment, context));
    }

    public void testCommentTestDoesNotMatchElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("comment()");
        assertFalse("comment() should not match element",
            pattern.matches(doc.getDocumentElement(), context));
    }

    public void testProcessingInstructionTestMatchesPI() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("processing-instruction()");
        Element a = (Element) doc.getDocumentElement()
            .getElementsByTagName("a").item(0);
        ProcessingInstruction pi = (ProcessingInstruction) a.getChildNodes()
            .item(3);
        assertTrue("processing-instruction() should match PI node",
            pattern.matches(pi, context));
    }

    public void testProcessingInstructionDoesNotMatchElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("processing-instruction()");
        assertFalse("processing-instruction() should not match element",
            pattern.matches(doc.getDocumentElement(), context));
    }

    // --- LocationPathPattern matches ---

    public void testChildLocationPathMatches() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("root/a");
        Element a = (Element) doc.getDocumentElement()
            .getElementsByTagName("a").item(0);
        assertTrue("root/a should match <a> child of <root>",
            pattern.matches(a, context));
    }

    public void testChildLocationPathDoesNotMatchNonChild() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("root/a");
        Element b = (Element) doc.getDocumentElement()
            .getElementsByTagName("b").item(0);
        assertFalse("root/a should not match <b> which is not a child of <a>",
            pattern.matches(b, context));
    }

    public void testDescendantLocationPathMatches() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("root//c");
        Element c = (Element) doc.getDocumentElement()
            .getElementsByTagName("c").item(0);
        assertTrue("root//c should match <c> descendant of <root>",
            pattern.matches(c, context));
    }

    public void testTwoStepChildPathMatches() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("root/a/b");
        Element b = (Element) doc.getDocumentElement()
            .getElementsByTagName("b").item(0);
        assertTrue("root/a/b should match <b> child of <a> child of <root>",
            pattern.matches(b, context));
    }

    public void testTwoStepChildPathDoesNotMatchWrongAncestor() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("root/a/d");
        Element d = (Element) doc.getDocumentElement()
            .getElementsByTagName("d").item(0);
        assertFalse("root/a/d should not match <d> which is not a child of <a>",
            pattern.matches(d, context));
    }

    // --- UnionPattern matches ---

    public void testUnionPatternMatchesLHS() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("a | nonexistent");
        Element a = (Element) doc.getDocumentElement()
            .getElementsByTagName("a").item(0);
        assertTrue("a | nonexistent should match <a>",
            pattern.matches(a, context));
    }

    public void testUnionPatternMatchesRHS() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("nonexistent | b");
        Element b = (Element) doc.getDocumentElement()
            .getElementsByTagName("b").item(0);
        assertTrue("nonexistent | b should match <b>",
            pattern.matches(b, context));
    }

    public void testUnionPatternDoesNotMatchUnlisted() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("a | b");
        Element c = (Element) doc.getDocumentElement()
            .getElementsByTagName("c").item(0);
        assertFalse("a | b should not match <c>",
            pattern.matches(c, context));
    }

    // --- Edge cases ---

    public void testMatchDocumentNode() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("/");
        assertTrue("pattern '/' should match document node",
            pattern.matches(doc, context));
    }

    public void testDocumentPatternDoesNotMatchElement() throws ParserConfigurationException, SAXPathException {
        Document doc = buildDocument();
        Pattern pattern = PatternParser.parse("/");
        assertFalse("pattern '/' should not match element",
            pattern.matches(doc.getDocumentElement(), context));
    }

}
