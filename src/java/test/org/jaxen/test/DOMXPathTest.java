/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2003 bob mcwhirter & James Strachan.
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
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id$
 */


package org.jaxen.test;

import junit.framework.TestCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DOMXPathTest extends TestCase
{

    private static final String BASIC_XML = "xml/basic.xml";
    private Document doc;
    private DocumentBuilderFactory factory;

    public DOMXPathTest(String name)
    {
        super( name );
    }
    
    public void setUp() throws ParserConfigurationException {
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        doc = factory.newDocumentBuilder().newDocument();        
    }
    

    public void testConstruction() throws JaxenException
    {
        DOMXPath xpath = new DOMXPath( "/foo/bar/baz" );
        assertEquals("/foo/bar/baz", xpath.toString());
    }
    
    public void testJaxen193() throws JaxenException
    {
        DOMXPath xpath = new DOMXPath( "/*[ * or processing-instruction() ]" );
        assertEquals("/*[ * or processing-instruction() ]", xpath.toString());
    }
    
    public void testJaxen193InReverse() throws JaxenException
    {
        DOMXPath xpath = new DOMXPath( "/*[ processing-instruction() or *]" );
        assertEquals("/*[ processing-instruction() or *]", xpath.toString());
    }
    
    public void testConstructionWithNamespacePrefix() throws JaxenException
    {
        DOMXPath xpath = new DOMXPath( "/p:foo/p:bar/a:baz" );
        assertEquals("/p:foo/p:bar/a:baz", xpath.toString());
    }
    
    public void testNamespaceDeclarationsAreNotAttributes() 
      throws JaxenException {
        
        Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.example.org/");
        
        DOMXPath xpath = new DOMXPath( "count(/*/@*)" );
        
        Number value = xpath.numberValueOf(doc);
        assertEquals(0, value.intValue());
        
    }

    
    // see JAXEN-105
    public void testConsistentNamespaceDeclarations() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element child = doc.createElementNS("http://www.example.org", "foo:child");
        root.appendChild(child);
        // different prefix
        child.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo2", "http://www.contradictory.org");
        
        XPath xpath = new DOMXPath("//namespace::node()");
        List result = xpath.selectNodes(doc);
        assertEquals(4, result.size());
        
    }

    // see JAXEN-105
    public void testInconsistentNamespaceDeclarations() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element child = doc.createElementNS("http://www.example.org", "foo:child");
        root.appendChild(child);
        // same prefix
        child.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://www.contradictory.org");
        
        XPath xpath = new DOMXPath("//namespace::node()");
        List result = xpath.selectNodes(doc);
        assertEquals(3, result.size());
        
    }

    // see JAXEN-105
    public void testIntrinsicNamespaceDeclarationOfElementBeatsContradictoryXmlnsPreAttr() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element child = doc.createElementNS("http://www.example.org", "foo:child");
        root.appendChild(child);
        // same prefix
        child.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://www.contradictory.org");
        
        XPath xpath = new DOMXPath("//namespace::node()[name(.)='foo']");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Node node = (Node) result.get(0);
        assertEquals("http://www.example.org", node.getNodeValue());
        
    }    
    
    // see JAXEN-105
    public void testIntrinsicNamespaceDeclarationOfAttrBeatsContradictoryXmlnsPreAttr() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        root.setAttributeNS("http://www.example.org/", "foo:a", "value");
        // same prefix, different namespace
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://www.contradictory.org");
        
        XPath xpath = new DOMXPath("//namespace::node()[name(.)='foo']");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Node node = (Node) result.get(0);
        assertEquals("http://www.example.org/", node.getNodeValue());
        
    }    
    
    // see JAXEN-105
    public void testIntrinsicNamespaceDeclarationOfElementBeatsContradictoryIntrinsicNamespaceOfAttr() 
      throws JaxenException {
        
        Element root = doc.createElementNS("http://www.example.org", "pre:root");
        doc.appendChild(root);
        // same prefix
        root.setAttributeNS("http://www.contradictory.org", "pre:foo", "value");
        
        XPath xpath = new DOMXPath("//namespace::node()[name(.)='pre']");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Node node = (Node) result.get(0);
        assertEquals("http://www.example.org", node.getNodeValue());
        
    }    
    
    // Jaxen-54
    public void testUpdateDOMNodesReturnedBySelectNodes() 
      throws JaxenException {
        
        Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        root.appendChild(doc.createComment("data"));
        
        DOMXPath xpath = new DOMXPath( "//comment()" );
        
        List results = xpath.selectNodes(doc);
        Node backroot = (Node) results.get(0);
        backroot.setNodeValue("test");
        assertEquals("test", backroot.getNodeValue());
        
    }

    public void testSelection() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
        XPath xpath = new DOMXPath( "/foo/bar/baz" );

        DocumentBuilder builder = factory.newDocumentBuilder();
    
        Document document = builder.parse( BASIC_XML );

        List results = xpath.selectNodes( document );

        assertEquals( 3,
                      results.size() );

        Iterator iter = results.iterator();

        assertEquals( "baz",
                      ((Element)iter.next()).getLocalName() );

        assertEquals( "baz",
                      ((Element)iter.next()).getLocalName() );

        assertEquals( "baz",
                      ((Element)iter.next()).getLocalName() );

        assertTrue( ! iter.hasNext() );

    }
    
    // Jaxen-22
    public void testPrecedingAxisWithPositionalPredicate() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
        
        XPath xpath = new DOMXPath( "//c/preceding::*[1][name()='d']" );
        DocumentBuilder builder = factory.newDocumentBuilder();
    
        Document document = builder.parse( "xml/web2.xml" );
        List result = xpath.selectNodes(document);
        assertEquals(1, result.size());
        
    }
    
     
    // Jaxen-22
    public void testJaxen22() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
        
        XPath xpath = new DOMXPath( "name(//c/preceding::*[1])" );
        DocumentBuilder builder = factory.newDocumentBuilder();
    
        doc = builder.parse("xml/web2.xml");
        Object result = xpath.evaluate(doc);
        assertEquals("d", result);
    }
    
     
    public void testPrecedingAxisInDocumentOrder() 
      throws JaxenException {
        
        XPath xpath = new DOMXPath( "preceding::*" );
    
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        Element a = doc.createElement("a");
        root.appendChild(a);
        Element b = doc.createElement("b");
        root.appendChild(b);
        Element c = doc.createElement("c");
        a.appendChild(c);
        
        List result = xpath.selectNodes(b);
        assertEquals(2, result.size());
        assertEquals(a, result.get(0));
        assertEquals(c, result.get(1));
    }
    
     
}
