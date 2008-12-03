/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
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
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id$
 */


package org.jaxen.test;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
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

    public void testSetNodeSet()
    {
        Context original = new Context( this.support );
        assertEquals(0, original.getNodeSet().size() );
        original.setNodeSet( this.nodeSet );
        assertEquals(4, original.getNodeSet().size() );
    }
    
    public void testShrinkNodeSet()
    {
        
        Context original = new Context( this.support );
        original.setNodeSet( this.nodeSet );
        original.setPosition(3);
        ArrayList list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        original.setNodeSet(list);
        assertEquals(0, original.getPosition());
        
    }
    
    public void testDuplicate()
    {
        Context original = new Context( this.support );

        original.setNodeSet( this.nodeSet );

        original.setSize( 4 );
        original.setPosition( 2 );

        Context dupe = original.duplicate();

        assertEquals(2, dupe.getPosition());
        assertEquals(4, dupe.getSize());
        
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
      throws ParserConfigurationException, SAXException, IOException, JaxenException
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

    
    public void testIsSerializable() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(support);
        oos.close();
        assertTrue(out.toByteArray().length > 0);
        
    }    
    
    
}

