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

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class JDOMXPathTest extends TestCase
{

    private static final String BASIC_XML = "xml/basic.xml";

    public JDOMXPathTest(String name)
    {
        super( name );
    }

    public void testConstruction() throws JaxenException
    {
        new JDOMXPath( "/foo/bar/baz" );
    }

    public void testSelection() throws JaxenException, JDOMException, IOException
    {
        XPath xpath = new JDOMXPath( "/foo/bar/baz" );

        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build( BASIC_XML );

        List results = xpath.selectNodes( doc );

        assertEquals( 3,
                      results.size() );

        Iterator iter = results.iterator();

        assertEquals( "baz",
                      ((Element)iter.next()).getName() );

        assertEquals( "baz",
                      ((Element)iter.next()).getName() );

        assertEquals( "baz",
                      ((Element)iter.next()).getName() );

        assertTrue( ! iter.hasNext() );
    }
    
    
    public void testGetDocumentNode() throws JaxenException, JDOMException, IOException
    {
        XPath xpath = new JDOMXPath( "/" );

        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build( BASIC_XML );

        Element root = doc.getRootElement();
        List results = xpath.selectNodes( root );

        assertEquals( 1,
                      results.size() );

        Iterator iter = results.iterator();

        assertEquals( doc, iter.next());

    }
    
    public void testJaxen148() throws JaxenException, JDOMException, IOException  {
        String xml = "<xml-document><nodes><node>" +
        "\ntest\n" + 
        "</node></nodes></xml-document>";

        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build( new InputSource( new StringReader(xml) ) );
        
        JDOMXPath x = new JDOMXPath("/xml-document/nodes/node/text()");
        Text t = (Text) x.selectSingleNode(document);
        
        assertEquals( "\ntest\n" , t.getText() );
        
    }
    
    
    public void testJaxen53Text() throws JaxenException, JDOMException, IOException
    {
        XPath xpath = new JDOMXPath( "//data/text() " );

        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build( new StringReader("<root>\n<data>1</data>\n</root>") );

        List results = xpath.selectNodes( doc );

        assertEquals( 1,
                      results.size() );

        Iterator iter = results.iterator();

        Text result = (Text) iter.next();
        assertEquals( "1", result.getValue());

    }
    
    public void testJaxen20AttributeNamespaceNodes() throws JaxenException
    {
        Namespace ns1 = Namespace.getNamespace("p1", "www.acme1.org");
        Namespace ns2 = Namespace.getNamespace("p2", "www.acme2.org");
        Element element = new Element("test", ns1);
        Attribute attribute = new Attribute("foo", "bar", ns2);
        element.setAttribute(attribute); 
        Document doc = new Document(element);
        
        XPath xpath = new JDOMXPath( "//namespace::node()" );

        List results = xpath.selectNodes( doc );

        assertEquals( 3,
                      results.size() );

    }
    
    public void testNamespaceNodesAreInherited() throws JaxenException
    {
        Namespace ns0 = Namespace.getNamespace("p0", "www.acme0.org");
        Namespace ns1 = Namespace.getNamespace("p1", "www.acme1.org");
        Namespace ns2 = Namespace.getNamespace("p2", "www.acme2.org");
        Element element = new Element("test", ns1);
        Attribute attribute = new Attribute("foo", "bar", ns2);
        element.setAttribute(attribute);
        Element root = new Element("root", ns0);
        root.addContent(element);
        Document doc = new Document(root);
        
        XPath xpath = new JDOMXPath( "/*/*/namespace::node()" );

        List results = xpath.selectNodes( doc );

        assertEquals( 4, results.size() );

    }
    
}
