/*
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
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
 */


package org.jaxen.test;

import java.util.Iterator;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.JaxenException;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.dom.DOMXPath;
import org.jaxen.dom.DocumentNavigator;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMNavigatorTest extends XPathTestBase
{    
    private static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    
    
    private DocumentBuilder builder;
    
    
    public DOMNavigatorTest(String name)
    {
        super( name );
    }
    
    public Navigator getNavigator()
    {
        return new DocumentNavigator();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
    }

    public Object getDocument(String url) throws Exception
    {
        return builder.parse( url );
    }

    protected XPath createXPath(String xpath) throws JaxenException
    {
        return new DOMXPath(xpath);
    }
    
    public void testGetAttributeQNameOnElement() {
        Navigator nav = getNavigator();
        Document doc = builder.newDocument();
        Element a = doc.createElement("a");
        String qname = nav.getAttributeQName(a);
        assertNull(qname);
    }
    
    public void testGetElementQNameOnAttr() {
        Navigator nav = getNavigator();
        Document doc = builder.newDocument();
        Attr a = doc.createAttribute("a");
        String qname = nav.getElementQName(a);
        assertNull(qname);
    }
    
    public void testGetAttributeLocalNameOnElement() {
        Navigator nav = getNavigator();
        Document doc = builder.newDocument();
        Element a = doc.createElementNS("http://www.ex.com", "pre:a");
        String name = nav.getAttributeName(a);
        assertNull(name);
    }
    
    public void testGetElementLocalNameOnAttr() {
        Navigator nav = getNavigator();
        Document doc = builder.newDocument();
        Attr a = doc.createAttributeNS("http://www.ex.com", "a");
        String name = nav.getElementName(a);
        assertNull(name);
    }
    
    public void testGetAttributeNamespaceURIOnElement() {
        Navigator nav = getNavigator();
        Document doc = builder.newDocument();
        Element a = doc.createElementNS("http://www.example.org/", "a");
        String qname = nav.getAttributeNamespaceUri(a);
        assertNull(qname);
    }
    
    public void testGetElementNamespaceURIOnAttr() {
        Navigator nav = getNavigator();
        Document doc = builder.newDocument();
        Attr a = doc.createAttributeNS("http://www.element.org/", "a");
        String qname = nav.getElementNamespaceUri(a);
        assertNull(qname);
    }
    
    public void testGetTargetOfNonPI() {
        Navigator nav = getNavigator();
        Document doc = builder.newDocument();
        Attr a = doc.createAttributeNS("http://www.element.org/", "a");
        try {
            nav.getProcessingInstructionTarget(a);
            fail("got target of non processing instruction");
        }
        catch (ClassCastException ex) {
            assertNotNull(ex.getMessage());
        }
    }
    
    public void testGetDataOfNonPI() {
        Navigator nav = getNavigator();
        Document doc = builder.newDocument();
        Attr a = doc.createAttributeNS("http://www.element.org/", "a");
        try {
            nav.getProcessingInstructionData(a);
            fail("got data of non processing instruction");
        }
        catch (ClassCastException ex) {
            assertNotNull(ex.getMessage());
        }
    }

    public void testGetAttributeAxisIteratorSkipsManyLeadingNamespaceDeclarations() throws UnsupportedAxisException {
        Navigator nav = getNavigator();
        final int xmlnsCount = 50000;
        final Node[] attributes = new Node[xmlnsCount + 1];
        for (int i = 0; i < xmlnsCount; i++) {
            attributes[i] = createAttributeNode(XMLNS_URI);
        }
        final Node realAttribute = createAttributeNode(null);
        attributes[xmlnsCount] = realAttribute;
        final NamedNodeMap map = createNamedNodeMap(attributes);
        Element root = createElementWithAttributes(map);

        Iterator iterator = nav.getAttributeAxisIterator(root);
        try {
            assertTrue(iterator.hasNext());
            assertSame(realAttribute, iterator.next());
            assertFalse(iterator.hasNext());
        }
        catch (StackOverflowError err) {
            fail("Stack overflow while skipping xmlns:* attributes");
        }
    }

    private static Element createElementWithAttributes(final NamedNodeMap attributes) {
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("getAttributes".equals(method.getName())) {
                    return attributes;
                }
                if ("getNodeType".equals(method.getName())) {
                    return Short.valueOf(Node.ELEMENT_NODE);
                }
                return null;
            }
        };
        return (Element) Proxy.newProxyInstance(Element.class.getClassLoader(),
                new Class[] {Element.class}, handler);
    }

    private static Node createAttributeNode(final String namespaceUri) {
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("getNamespaceURI".equals(method.getName())) {
                    return namespaceUri;
                }
                return null;
            }
        };
        return (Node) Proxy.newProxyInstance(Node.class.getClassLoader(),
                new Class[] {Node.class}, handler);
    }

    private static NamedNodeMap createNamedNodeMap(final Node[] attributes) {
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) {
                if ("getLength".equals(method.getName())) {
                    return Integer.valueOf(attributes.length);
                }
                if ("item".equals(method.getName())) {
                    int index = ((Integer) args[0]).intValue();
                    if (index < 0 || index >= attributes.length) {
                        return null;
                    }
                    return attributes[index];
                }
                return null;
            }
        };
        return (NamedNodeMap) Proxy.newProxyInstance(NamedNodeMap.class.getClassLoader(),
                new Class[] {NamedNodeMap.class}, handler);
    }
     
     
}
