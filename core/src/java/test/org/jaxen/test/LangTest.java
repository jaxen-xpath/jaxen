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

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.jaxen.BaseXPath;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Elliotte Rusty Harold
 *
 */
public class LangTest extends TestCase {

    private Document doc;
    private DocumentBuilder builder;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
    }

    public LangTest(String name) {
        super(name);
    }

    public void testLangFunction() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//*[lang('en')]");
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "en");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("2"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        List result = xpath.selectNodes(doc);
        assertEquals(3, result.size());
        assertEquals(b, result.get(0));
        assertEquals(x2, result.get(1));
        assertEquals(x3, result.get(2));
        
    }    

    public void testLangFunctionSelectsNothing() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//*[lang('fr')]");
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "en");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("2"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        List result = xpath.selectNodes(doc);
        assertEquals(0, result.size());
        
    }    

    public void testLangFunctionSelectsSubcode() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//*[lang('fr')]");
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "fr-CA");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("2"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        List result = xpath.selectNodes(doc);
        assertEquals(3, result.size());
        assertEquals(b, result.get(0));
        assertEquals(x2, result.get(1));
        assertEquals(x3, result.get(2));
        
    }    

    public void testHyphenRequiredAtEnd() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//*[lang('f')]");
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "fr-CA");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("2"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        List result = xpath.selectNodes(doc);
        assertEquals(0, result.size());
        
    }    

    public void testLangFunctionSelectsEmptyNodeSet() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//*[lang(d)]");
        // This node-set will be empty. Therefore it's the same as the
        // empty string. Therefore it matches all languages.
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "fr-CA");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("2"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        List result = xpath.selectNodes(doc);
        assertEquals(0, result.size());
        
    }    

    public void testLangFunctionSelectsNonEmptyNodeSet() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//*[lang(x)]");
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "fr-CA");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("fr"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        assertEquals(b, result.get(0));
        
    }    

    public void testLangFunctionAppliedToNonElement() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//text()[lang('fr')]");
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "fr-CA");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("fr"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        List result = xpath.selectNodes(doc);
        assertEquals(2, result.size());
        assertEquals(x2.getFirstChild(), result.get(0));
        assertEquals(x3.getFirstChild(), result.get(1));
        
    }    

    public void testLangFunctionAppliedToDocument() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("lang('fr')");
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "fr-CA");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("fr"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertEquals(Boolean.FALSE, result);
        
    }    

    public void testLangFunctionSelectsNumber() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//*[lang(3)]");
        
        Element a = doc.createElementNS("", "a");
        Element b = doc.createElementNS("", "b");
        b.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "fr-CA");
        doc.appendChild(a);
        a.appendChild(b);
        Element x2 = doc.createElementNS("", "x");
        Element x3 = doc.createElementNS("", "x");
        Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        x2.appendChild(doc.createTextNode("2"));
        x3.appendChild(doc.createTextNode("3"));
        x4.appendChild(doc.createTextNode("4"));
        
        List result = xpath.selectNodes(doc);
        assertEquals(0, result.size());
        
    }    

    public void testLangFunctionRequiresOneArgument() 
      throws JaxenException {
        
        try {
            BaseXPath xpath = new DOMXPath("lang()");
            org.w3c.dom.Element a = doc.createElementNS("", "a");
            doc.appendChild(a);
            xpath.selectNodes(doc);
            fail("Allowed empty lang() function");
        }
        catch (FunctionCallException success) {
            assertNotNull(success.getMessage());
        }
        
    }    

    public void testLangFunctionRequiresAtMostOneArgument() 
      throws JaxenException {
        
        try {
            BaseXPath xpath = new DOMXPath("lang('en', 'fr')");
            org.w3c.dom.Element a = doc.createElementNS("", "a");
            doc.appendChild(a);
            xpath.selectNodes(doc);
            fail("Allowed empty lang() function");
        }
        catch (FunctionCallException success) {
            assertNotNull(success.getMessage());
        }
        
    }    


}
