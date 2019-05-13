/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2005 Elliotte Rusty Harold
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

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.jaxen.BaseXPath;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Elliotte Rusty Harold
 *
 */
public class IdTest extends TestCase {

    private Document doc;
    private DocumentBuilder builder;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
    }

    public IdTest(String name) {
        super(name);
    }

    public void testIDFunctionSelectsNothingInDocumentWithNoIds() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("id('p1')");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        a.appendChild(b);
        org.w3c.dom.Element x2 = doc.createElementNS("", "x");
        org.w3c.dom.Element x3 = doc.createElementNS("", "x");
        org.w3c.dom.Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("2"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        Attr id = doc.createAttribute("id");
        id.setNodeValue("p1");
        x2.setAttributeNode(id);
        
        List result = xpath.selectNodes(doc);
        assertEquals(0, result.size());
        
    }    

    public void testIDFunctionRequiresAtLeastOneArgument() 
      throws JaxenException {
        
        try {
            BaseXPath xpath = new DOMXPath("id()");
            org.w3c.dom.Element a = doc.createElementNS("", "a");
            doc.appendChild(a);
            xpath.selectNodes(doc);
            fail("Allowed empty id() function");
        }
        catch (FunctionCallException success) {
            assertNotNull(success.getMessage());
        }
        
    }    

    public void testIDFunctionRequiresAtMostOneArgument() 
      throws JaxenException {
        
        try {
            BaseXPath xpath = new DOMXPath("id('p', 'q')");
            org.w3c.dom.Element a = doc.createElementNS("", "a");
            doc.appendChild(a);
            xpath.selectNodes(doc);
            fail("Allowed two-argument id() function");
        }
        catch (FunctionCallException success) {
            assertNotNull(success.getMessage());
        }
        
    }    

    public void testFindElementById() 
      throws JaxenException, SAXException, IOException {
        
        BaseXPath xpath = new DOMXPath("id('p1')");
        String text = "<!DOCTYPE root [<!ATTLIST a id ID #REQUIRED>]><root><a id='p1'/></root>";
        StringReader reader = new StringReader(text);
        InputSource in = new InputSource(reader);
        Document doc = builder.parse(in);
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Element a = (Element) result.get(0);
        assertEquals("a", a.getNodeName());
        
    }    

    /* public void testFindElementByXMLId() 
      throws JaxenException, SAXException, IOException {
        
        BaseXPath xpath = new DOMXPath("id('p1')");
        String text = "<root><a xml:id='p1'/></root>";
        StringReader reader = new StringReader(text);
        InputSource in = new InputSource(reader);
        Document doc = builder.parse(in);
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Element a = (Element) result.get(0);
        assertEquals("a", a.getNodeName());
        
    }    */

    public void testFindMultipleElementsByMultipleIDs() 
      throws JaxenException, SAXException, IOException {
        
        BaseXPath xpath = new DOMXPath("id(//id)");
        String text = "<!DOCTYPE root [<!ATTLIST a id ID #REQUIRED>]><root><id>p1</id><id>p2</id><id>p3</id><a id='p1'/><a id='p2'/></root>";
        StringReader reader = new StringReader(text);
        InputSource in = new InputSource(reader);
        Document doc = builder.parse(in);
        List result = xpath.selectNodes(doc);
        assertEquals(2, result.size());
        Element a1 = (Element) result.get(0);
        Element a2 = (Element) result.get(1);
        assertEquals("a", a1.getNodeName());
        assertEquals("a", a2.getNodeName());
        
    }    

    public void testIdReturnsFirstElementWithMatchingId() 
      throws JaxenException, SAXException, IOException {
        
        BaseXPath xpath = new DOMXPath("id('p1')");
        String text = "<!DOCTYPE root [<!ATTLIST a id ID #REQUIRED>" +
                "<!ATTLIST b id ID #REQUIRED>]><root><a id='p1'/><b id='p1'/></root>";
        StringReader reader = new StringReader(text);
        InputSource in = new InputSource(reader);
        Document doc = builder.parse(in);
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Element a = (Element) result.get(0);
        assertEquals("a", a.getNodeName());
        
    }    

}
