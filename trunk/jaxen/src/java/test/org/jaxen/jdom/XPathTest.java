/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2003 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows 
 *    these conditions in the documentation and/or other materials 
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 * 
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project (http://www.jaxen.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
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


package org.jaxen.jdom;

import junit.framework.TestCase;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jaxen.XPath;
import org.jaxen.saxpath.SAXPathException;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

public class XPathTest extends TestCase
{

    private static final String BASIC_XML = "xml/basic.xml";

    public XPathTest(String name)
    {
        super( name );
    }

    public void setUp()
    {

    }

    public void tearDown()
    {

    }

    public void testConstruction()
    {
        try
        {
            new JDOMXPath( "/foo/bar/baz" );
        }
        catch (SAXPathException e)
        {
            fail( e.getMessage() );
        }
    }

    public void testSelection()
    {
        try
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
        catch (Exception e)
        {
            fail( e.getMessage() );
        }
    }
    
    
    public void testGetDocumentNode()
    {
        try
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
        catch (Exception e)
        {
            fail( e.getMessage() );
        }
    }
    
    public void testJaxen53Text()
    {
        try
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
        catch (Exception e)
        {
            fail( e.getMessage() );
        }
    }
    
    public void testJaxen20AttributeNamespaceNodes()
    {
        try
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
        catch (Exception e)
        {
            fail( e.getMessage() );
        }
    }
    
    public void testNamespaceNodesAreInherited()
    {
        try
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

            assertEquals( 4,
                          results.size() );

        }
        catch (Exception e)
        {
            fail( e.getMessage() );
        }
    }
    
}
