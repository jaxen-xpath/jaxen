/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
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


package org.jaxen;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ContextTest extends TestCase
{
    private List           nodeSet;
    private ContextSupport support;

    public ContextTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.nodeSet = new ArrayList();

        this.nodeSet.add( "one" );
        this.nodeSet.add( "two" );
        this.nodeSet.add( "three" );
        this.nodeSet.add( "four" );

        this.support = new ContextSupport();
    }

    public void tearDown()
    {
        this.nodeSet = null;
    }

    public void testDuplicate()
    {
        Context original = new Context( this.support );

        original.setNodeSet( this.nodeSet );

        original.setSize( 4 );
        original.setPosition( 2 );

        Context dupe = original.duplicate();

        assertTrue( original != dupe );

        List dupeNodeSet = dupe.getNodeSet();

        assertTrue( original.getNodeSet() != dupe.getNodeSet() );

        dupeNodeSet.clear();

        assertSame( dupeNodeSet,
                    dupe.getNodeSet() );

        assertEquals( 0,
                      dupe.getNodeSet().size() );


        assertEquals( 4,
                      original.getNodeSet().size() );

        dupe.setSize( 0 );
        dupe.setPosition( 0 );

        assertEquals( 0,
                      dupe.getSize() );

        assertEquals( 0,
                      dupe.getPosition() );

        assertEquals( 4,
                      original.getSize() );

        assertEquals( 2,
                      original.getPosition() );
    }

    public void testXMLPrefixIsAlwaysBound()
    {  
         
        try
        {
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           factory.setNamespaceAware(true);
           DocumentBuilder builder = factory.newDocumentBuilder();
           Document doc = builder.parse( "xml/basic.xml" );
           Element root = doc.getDocumentElement();
           root.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "en");
           XPath xpath = new DOMXPath( "/*/@xml:lang" );
           List result = xpath.selectNodes( doc );
           assertEquals(1, result.size());
       }
       catch (Exception e)
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }    

    
}

