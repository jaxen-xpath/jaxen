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

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.XPathSyntaxException;
import org.jaxen.dom4j.Dom4jXPath;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

public class DOM4JXPathTest extends TestCase
{

    private static final String BASIC_XML = "xml/basic.xml";
    
    public DOM4JXPathTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "" );
    }

    public void testConstruction() throws JaxenException
    {
        new Dom4jXPath( "/foo/bar/baz" );
    }

    public void testSelection() throws JaxenException, DocumentException
    {

        XPath xpath = new Dom4jXPath( "/foo/bar/baz" );
        SAXReader reader = new SAXReader();
        Document doc = reader.read( BASIC_XML );
        List results = xpath.selectNodes( doc );
        assertEquals( 3, results.size() );
        Iterator iter = results.iterator();
        assertEquals( "baz",
                      ((Element)iter.next()).getName() );
        assertEquals( "baz",
                      ((Element)iter.next()).getName() );
        assertEquals( "baz",
                      ((Element)iter.next()).getName() );
        assertTrue( ! iter.hasNext() );

    }
    
    public void testAsBoolean() throws JaxenException, DocumentException
    {
        XPath xpath = new Dom4jXPath( "/root/a = 'a'" );
        SAXReader reader = new SAXReader();
        Document doc = reader.read( "xml/simple.xml" );
        boolean answer = xpath.booleanValueOf( doc );
        assertTrue( "Xpath worked: " + xpath, answer );
        xpath = new Dom4jXPath( "'a' = 'b'" );
        answer = xpath.booleanValueOf( doc );
        assertTrue( "XPath should return false: " + xpath, ! answer );
    }
    
    public void testJaxen20AttributeNamespaceNodes() throws JaxenException
    {

        Namespace ns1 = Namespace.get("p1", "www.acme1.org");
        Namespace ns2 = Namespace.get("p2", "www.acme2.org");
        Element element = new DefaultElement("test", ns1);
        Attribute attribute = new DefaultAttribute("pre:foo", "bar", ns2);
        element.add(attribute); 
        Document doc = new DefaultDocument(element);
        
        XPath xpath = new Dom4jXPath( "//namespace::node()" );
        List results = xpath.selectNodes( doc );
        assertEquals( 3, results.size() );

    }
    
    public void testJaxen16() throws JaxenException, DocumentException
    {

        String document = "<a xmlns:b=\"...\"/>";
        SAXReader reader = new SAXReader();
        Document doc = reader.read( new StringReader(document) );
        
        XPath xpath = new Dom4jXPath( "/a/b" );
        List results = xpath.selectNodes( doc );
        assertEquals( 0, results.size() );

    }
    
    public void testNamespaceNodesAreInherited() throws JaxenException
    {
            Namespace ns0 = Namespace.get("p0", "www.acme0.org");
            Namespace ns1 = Namespace.get("p1", "www.acme1.org");
            Namespace ns2 = Namespace.get("p2", "www.acme2.org");
            Element element = new DefaultElement("test", ns1);
            Attribute attribute = new DefaultAttribute("pre:foo", "bar", ns2);
            element.add(attribute);
            Element root = new DefaultElement("root", ns0);
            root.add(element);
            Document doc = new DefaultDocument(root);
            
            XPath xpath = new Dom4jXPath( "/*/*/namespace::node()" );

            List results = xpath.selectNodes( doc );

            assertEquals( 4,
                          results.size() );
    }
    
    public void testSyntaxException() throws JaxenException {
        
        String path = "/row/[some_node='val1']|[some_node='val2']";
        try {
            new Dom4jXPath(path);
            fail("Allowed union of non-node-sets");
        }
        catch (XPathSyntaxException success) {
            assertNotNull(success.getMessage());
        }
        
    }
    
    
}
