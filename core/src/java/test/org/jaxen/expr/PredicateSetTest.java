package org.jaxen.expr;

import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.SimpleVariableContext;
import org.jaxen.XPathFunctionContext;
import org.jaxen.dom.DOMXPath;
import org.jaxen.dom.DocumentNavigator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests for {@link PredicateSet}, in particular the
 * {@code anyMatchingNode} method invoked via
 * {@link DefaultFilterExpr#asBoolean(Context)}.
 */
public class PredicateSetTest extends TestCase {

    private Document doc;

    public PredicateSetTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();

        // <root>
        //   <item class="a" type="x"/>
        //   <item class="a" type="y"/>
        //   <item class="b" type="x"/>
        // </root>
        Element root = doc.createElement("root");
        doc.appendChild(root);

        Element item1 = doc.createElement("item");
        item1.setAttribute("class", "a");
        item1.setAttribute("type", "x");
        root.appendChild(item1);

        Element item2 = doc.createElement("item");
        item2.setAttribute("class", "a");
        item2.setAttribute("type", "y");
        root.appendChild(item2);

        Element item3 = doc.createElement("item");
        item3.setAttribute("class", "b");
        item3.setAttribute("type", "x");
        root.appendChild(item3);
    }

    /**
     * Regression test: when a filter expression with two predicates is
     * evaluated as a boolean against a node set containing three nodes,
     * {@code anyMatchingNode()} must not throw
     * {@code NoSuchElementException} because the predicate iterator
     * was advanced inside the inner (node) loop instead of the outer
     * (predicate) loop.
     */
    public void testAnyMatchingNodeWithMoreNodesThanPredicates()
            throws JaxenException {

        // (//item)[@class='a'][@type='x'] is a FilterExpr with two predicates.
        // //item matches 3 nodes, which is more than the 2 predicates.
        DOMXPath xpath = new DOMXPath("(//item)[@class='a'][@type='x']");
        Expr rootExpr = xpath.getRootExpr();
        assertTrue("Expected a FilterExpr, got " + rootExpr.getClass().getName(),
                rootExpr instanceof FilterExpr);

        FilterExpr filterExpr = (FilterExpr) rootExpr;
        // Verify the expression has two predicates
        assertEquals(2, filterExpr.getPredicates().size());

        // Build a context rooted at the document
        ContextSupport support = new ContextSupport(
                new SimpleNamespaceContext(),
                XPathFunctionContext.getInstance(),
                new SimpleVariableContext(),
                DocumentNavigator.getInstance());
        Context context = new Context(support);
        context.setNodeSet(Collections.singletonList(doc));

        // asBoolean() calls evaluateAsBoolean() -> anyMatchingNode().
        // Before the fix this threw NoSuchElementException.
        boolean result = filterExpr.asBoolean(context);
        assertTrue("Expected at least one node to match both predicates", result);
    }

    /**
     * When no node satisfies all predicates the method must return false
     * rather than crash.
     */
    public void testAnyMatchingNodeReturnsFalseWhenNoMatch()
            throws JaxenException {

        // No item has both class="b" and type="y"
        DOMXPath xpath = new DOMXPath("(//item)[@class='b'][@type='y']");
        Expr rootExpr = xpath.getRootExpr();
        assertTrue(rootExpr instanceof FilterExpr);

        FilterExpr filterExpr = (FilterExpr) rootExpr;
        ContextSupport support = new ContextSupport(
                new SimpleNamespaceContext(),
                XPathFunctionContext.getInstance(),
                new SimpleVariableContext(),
                DocumentNavigator.getInstance());
        Context context = new Context(support);
        context.setNodeSet(Collections.singletonList(doc));

        boolean result = filterExpr.asBoolean(context);
        assertFalse("Expected no node to match both predicates", result);
    }

    /**
     * When no predicate matches any early node, the inner loop exhausts
     * the predicate iterator before all nodes are processed, causing
     * a {@code NoSuchElementException}.
     */
    public void testAnyMatchingNodeCrashesWithMoreNodesThanPredicates()
            throws JaxenException {

        // Neither predicate matches any early node, so the inner loop
        // tries to call predIter.next() a third time for the third node,
        // but only two predicates exist.
        DOMXPath xpath = new DOMXPath("(//item)[@class='c'][@type='z']");
        Expr rootExpr = xpath.getRootExpr();
        assertTrue(rootExpr instanceof FilterExpr);

        FilterExpr filterExpr = (FilterExpr) rootExpr;
        ContextSupport support = new ContextSupport(
                new SimpleNamespaceContext(),
                XPathFunctionContext.getInstance(),
                new SimpleVariableContext(),
                DocumentNavigator.getInstance());
        Context context = new Context(support);
        context.setNodeSet(Collections.singletonList(doc));

        // Before the fix this threw NoSuchElementException.
        boolean result = filterExpr.asBoolean(context);
        assertFalse("No node should match both predicates", result);
    }

    /**
     * Verify that evaluating the filter expression normally (not as boolean)
     * still returns the correct filtered node set.
     */
    public void testFilterExprEvaluateReturnsCorrectNodes()
            throws JaxenException {

        DOMXPath xpath = new DOMXPath("(//item)[@class='a'][@type='x']");
        List<?> result = xpath.selectNodes(doc);
        assertEquals("Only the first item should match both predicates", 1, result.size());
    }
}
