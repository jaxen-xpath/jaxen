/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2005, 2006 Elliotte Rusty Harold
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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.SimpleNamespaceContext;
import org.jaxen.UnsupportedAxisException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.TestCase;

/**
 * <p>
 *  Test for namespace context.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b10
 *
 */
public class SimpleNamespaceContextTest extends TestCase
{

    /**
     * Need to make sure that changing the map after it's used to create the 
     * namespace context does not affect the context. i.e.
     * data encapsulation is not violated.
     */
    public void testMapCopy() {
        Map map = new HashMap();
        SimpleNamespaceContext context = new SimpleNamespaceContext(map);
        map.put("pre", "http://www.example.org/");
        assertNull(context.translateNamespacePrefixToUri("pre"));
    }
 
    public void testCantUseNonStringsAsValues() {
        Map map = new HashMap();
        map.put("key", new Object());
        try {
            new SimpleNamespaceContext(map);
            fail("added non String value to namespace context");
        }
        catch (Exception ex) {
            assertNotNull(ex.getMessage());
        }
    }
 
    public void testCantUseNonStringsAsKeys() {
        Map map = new HashMap();
        map.put(new Object(), "value");
        try {
            new SimpleNamespaceContext(map);
            fail("added non String key to namespace context");
        }
        catch (Exception ex) {
            assertNotNull(ex.getMessage());
        }
    }
 
    public void testContextFromElement() throws ParserConfigurationException, UnsupportedAxisException { 
        SimpleNamespaceContext context = new SimpleNamespaceContext();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElementNS("http://www.example.org/", "pre:root");
        doc.appendChild(root);
        context.addElementNamespaces(new org.jaxen.dom.DocumentNavigator(), root);
        
        assertEquals("http://www.example.org/", context.translateNamespacePrefixToUri("pre"));
    }
 
    public void testSerialization() throws IOException, ClassNotFoundException {
        
        // construct test object
        SimpleNamespaceContext original = new SimpleNamespaceContext();
        original.addNamespace("a", "http://www.a.com");
        original.addNamespace("b", "http://www.b.com");
        
        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();
        
        //deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        SimpleNamespaceContext copy = (SimpleNamespaceContext) o;
        
        // test the result
        assertEquals("http://www.a.com", copy.translateNamespacePrefixToUri("a"));
        assertEquals("http://www.b.com", copy.translateNamespacePrefixToUri("b"));
        assertEquals("", "");
        
    }
    
}
