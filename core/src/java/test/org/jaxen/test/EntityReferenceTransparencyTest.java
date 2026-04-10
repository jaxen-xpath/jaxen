/* $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2024 Jaxen Project.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Jaxen Project and was originally
 * created by bob mcwhirter <bob@werken.com> and
 * James Strachan <jstrachan@apache.org>.  For more information on the
 * Jaxen Project, please see <https://github.com/jaxen-xpath/jaxen/>.
 *
 * $Id$
 */
package org.jaxen.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.jaxen.dom.DocumentNavigator;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/**
 * Tests that DocumentNavigator's NodeIterator correctly expands
 * DocumentFragment and EntityReference nodes by iterating their children
 * (making them "transparent") rather than skipping them entirely.
 *
 * <p>Because the standard JAXP/Xerces DOM implementation does not allow
 * programmatic addition of children to EntityReference nodes, these tests
 * use a minimal stub {@link StubNode} implementation to build the required
 * tree structure directly.</p>
 */
public class EntityReferenceTransparencyTest extends TestCase {

    public EntityReferenceTransparencyTest(String name) {
        super(name);
    }

    // ---------------------------------------------------------------
    // Minimal DOM Node stub used to build test trees
    // ---------------------------------------------------------------

    /**
     * A minimal, mutable DOM {@link Node} stub for unit testing.
     * Only the traversal methods used by {@code NodeIterator} are
     * implemented; all other methods throw
     * {@link UnsupportedOperationException}.
     */
    static class StubNode implements Node {

        private final short nodeType;
        private final String nodeName;
        private StubNode parent;
        private StubNode firstChild;
        private StubNode lastChild;
        private StubNode nextSibling;
        private StubNode prevSibling;

        StubNode(short nodeType, String nodeName) {
            this.nodeType = nodeType;
            this.nodeName = nodeName;
        }

        /** Append a child to this node (updates sibling links). */
        StubNode addChild(StubNode child) {
            child.parent = this;
            child.prevSibling = lastChild;
            child.nextSibling = null;
            if (lastChild != null) {
                lastChild.nextSibling = child;
            } else {
                firstChild = child;
            }
            lastChild = child;
            return child;
        }

        // -- Node methods used by NodeIterator --

        public short getNodeType()         { return nodeType; }
        public String getNodeName()        { return nodeName; }
        public Node   getParentNode()      { return parent; }
        public Node   getFirstChild()      { return firstChild; }
        public Node   getLastChild()       { return lastChild; }
        public Node   getNextSibling()     { return nextSibling; }
        public Node   getPreviousSibling() { return prevSibling; }
        public boolean hasChildNodes()     { return firstChild != null; }

        // -- Unsupported Node methods --

        public String     getNodeValue()                    { return null; }
        public void       setNodeValue(String v)            { throw new UnsupportedOperationException(); }
        public NodeList   getChildNodes()                   { throw new UnsupportedOperationException(); }
        public NamedNodeMap getAttributes()                 { return null; }
        public Document   getOwnerDocument()                { return null; }
        public Node       insertBefore(Node n, Node r)      { throw new UnsupportedOperationException(); }
        public Node       replaceChild(Node n, Node o)      { throw new UnsupportedOperationException(); }
        public Node       removeChild(Node o)               { throw new UnsupportedOperationException(); }
        public Node       appendChild(Node n)               { throw new UnsupportedOperationException(); }
        public Node       cloneNode(boolean d)              { throw new UnsupportedOperationException(); }
        public void       normalize()                       { throw new UnsupportedOperationException(); }
        public boolean    isSupported(String f, String v)   { throw new UnsupportedOperationException(); }
        public String     getNamespaceURI()                 { return null; }
        public String     getPrefix()                       { return null; }
        public void       setPrefix(String p)               { throw new UnsupportedOperationException(); }
        public String     getLocalName()                    { return nodeName; }
        public boolean    hasAttributes()                   { return false; }
        public String     getBaseURI()                      { return null; }
        public short      compareDocumentPosition(Node n)   { throw new UnsupportedOperationException(); }
        public String     getTextContent()                  { return null; }
        public void       setTextContent(String t)          { throw new UnsupportedOperationException(); }
        public boolean    isSameNode(Node n)                { return this == n; }
        public String     lookupPrefix(String ns)           { return null; }
        public boolean    isDefaultNamespace(String ns)     { return false; }
        public String     lookupNamespaceURI(String p)      { return null; }
        public boolean    isEqualNode(Node n)               { return this == n; }
        public Object     getFeature(String f, String v)    { return null; }
        public Object     setUserData(String k, Object d, UserDataHandler h) { return null; }
        public Object     getUserData(String k)             { return null; }
    }

    // ---------------------------------------------------------------
    // Helper: collect all nodes returned by an iterator into a List
    // ---------------------------------------------------------------

    private static List<Node> iteratorToList(Iterator iter) {
        List<Node> result = new ArrayList<Node>();
        while (iter.hasNext()) {
            result.add((Node) iter.next());
        }
        return result;
    }

    // ---------------------------------------------------------------
    // Helper factory methods for stub nodes
    // ---------------------------------------------------------------

    private static StubNode element(String name) {
        return new StubNode(Node.ELEMENT_NODE, name);
    }

    private static StubNode entityRef(String name) {
        return new StubNode(Node.ENTITY_REFERENCE_NODE, name);
    }

    private static StubNode docFragment() {
        return new StubNode(Node.DOCUMENT_FRAGMENT_NODE, "#document-fragment");
    }

    // ---------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------

    /**
     * Child axis: EntityReference children should appear inline.
     *
     * Tree: root -> a, &entity;(c, d), b
     * Expected child::* of root: a, c, d, b
     */
    public void testChildAxisEntityReferenceTransparency() {
        StubNode root = element("root");
        StubNode a    = element("a");
        StubNode ref  = entityRef("entity");
        StubNode c    = element("c");
        StubNode d    = element("d");
        StubNode b    = element("b");

        root.addChild(a);
        ref.addChild(c);
        ref.addChild(d);
        root.addChild(ref);
        root.addChild(b);

        DocumentNavigator nav = (DocumentNavigator) DocumentNavigator.getInstance();
        List result = iteratorToList(nav.getChildAxisIterator(root));

        assertEquals(4, result.size());
        assertSame(a, result.get(0));
        assertSame(c, result.get(1));
        assertSame(d, result.get(2));
        assertSame(b, result.get(3));
    }

    /**
     * Child axis: empty EntityReference should be skipped (no children).
     *
     * Tree: root -> a, &entity;(empty), b
     * Expected child::* of root: a, b
     */
    public void testChildAxisEmptyEntityReferenceIsSkipped() {
        StubNode root = element("root");
        StubNode a    = element("a");
        StubNode ref  = entityRef("entity");  // no children
        StubNode b    = element("b");

        root.addChild(a);
        root.addChild(ref);
        root.addChild(b);

        DocumentNavigator nav = (DocumentNavigator) DocumentNavigator.getInstance();
        List result = iteratorToList(nav.getChildAxisIterator(root));

        assertEquals(2, result.size());
        assertSame(a, result.get(0));
        assertSame(b, result.get(1));
    }

    /**
     * Child axis: nested EntityReferences should be flattened.
     *
     * Tree: root -> &outer;( &inner;(x) )
     * Expected child::* of root: x
     */
    public void testChildAxisNestedEntityReferenceTransparency() {
        StubNode root  = element("root");
        StubNode outer = entityRef("outer");
        StubNode inner = entityRef("inner");
        StubNode x     = element("x");

        inner.addChild(x);
        outer.addChild(inner);
        root.addChild(outer);

        DocumentNavigator nav = (DocumentNavigator) DocumentNavigator.getInstance();
        List result = iteratorToList(nav.getChildAxisIterator(root));

        assertEquals(1, result.size());
        assertSame(x, result.get(0));
    }

    /**
     * Child axis: DocumentFragment children should appear inline.
     *
     * Tree: root -> a, #fragment(c, d), b
     * Expected child::* of root: a, c, d, b
     */
    public void testChildAxisDocumentFragmentTransparency() {
        StubNode root = element("root");
        StubNode a    = element("a");
        StubNode frag = docFragment();
        StubNode c    = element("c");
        StubNode d    = element("d");
        StubNode b    = element("b");

        root.addChild(a);
        frag.addChild(c);
        frag.addChild(d);
        root.addChild(frag);
        root.addChild(b);

        DocumentNavigator nav = (DocumentNavigator) DocumentNavigator.getInstance();
        List result = iteratorToList(nav.getChildAxisIterator(root));

        assertEquals(4, result.size());
        assertSame(a, result.get(0));
        assertSame(c, result.get(1));
        assertSame(d, result.get(2));
        assertSame(b, result.get(3));
    }

    /**
     * Following-sibling axis: EntityReference siblings should be expanded.
     *
     * Tree: root -> a, &entity;(c, d), b
     * Expected following-sibling::* of a: c, d, b
     */
    public void testFollowingSiblingAxisEntityReferenceTransparency() {
        StubNode root = element("root");
        StubNode a    = element("a");
        StubNode ref  = entityRef("entity");
        StubNode c    = element("c");
        StubNode d    = element("d");
        StubNode b    = element("b");

        root.addChild(a);
        ref.addChild(c);
        ref.addChild(d);
        root.addChild(ref);
        root.addChild(b);

        DocumentNavigator nav = (DocumentNavigator) DocumentNavigator.getInstance();
        List result = iteratorToList(nav.getFollowingSiblingAxisIterator(a));

        assertEquals(3, result.size());
        assertSame(c, result.get(0));
        assertSame(d, result.get(1));
        assertSame(b, result.get(2));
    }

    /**
     * Preceding-sibling axis: EntityReference siblings should be expanded in reverse.
     *
     * Tree: root -> a, &entity;(c, d), b
     * Expected preceding-sibling::* of b (in reverse document order): d, c, a
     */
    public void testPrecedingSiblingAxisEntityReferenceTransparency() {
        StubNode root = element("root");
        StubNode a    = element("a");
        StubNode ref  = entityRef("entity");
        StubNode c    = element("c");
        StubNode d    = element("d");
        StubNode b    = element("b");

        root.addChild(a);
        ref.addChild(c);
        ref.addChild(d);
        root.addChild(ref);
        root.addChild(b);

        DocumentNavigator nav = (DocumentNavigator) DocumentNavigator.getInstance();
        List result = iteratorToList(nav.getPrecedingSiblingAxisIterator(b));

        assertEquals(3, result.size());
        assertSame(d, result.get(0));
        assertSame(c, result.get(1));
        assertSame(a, result.get(2));
    }
}
