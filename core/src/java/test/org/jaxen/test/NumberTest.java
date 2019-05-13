/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2005 Elliotte Rusty Harold.
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

import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.jaxen.dom.DocumentNavigator;
import org.jaxen.function.NumberFunction;
import org.w3c.dom.Document;

/**
 * @author Elliotte Rusty Harold
 *
 */
public class NumberTest extends TestCase {

    private Document doc;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
    }


    public NumberTest(String name) {
        super(name);
    }

    // test case for JAXEN-55
    public void testNumberFunctionOperatesOnFirstNodeInDocumentOrder() 
      throws JaxenException {
        
        XPath xpath = new DOMXPath("number(//x)");
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
        
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        assertEquals(Double.valueOf("2.0"), result.get(0));
        
    }    

    public void testNumberFunctionOperatesOnContextNode() 
      throws JaxenException {
        
        XPath xpath = new DOMXPath("number()");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        doc.appendChild(a);
        a.appendChild(doc.createTextNode("2"));
        
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(2, result.intValue());
        
    }   
    
    public void testBooleanConvertedToNumber() {
        double tolerance = 0.0000001;
        Navigator navigator = new DocumentNavigator();
        assertEquals(0.0, NumberFunction.evaluate(Boolean.FALSE, navigator ).doubleValue(), tolerance);
        assertEquals(1.0, NumberFunction.evaluate(Boolean.TRUE, navigator).doubleValue(), tolerance);
        assertEquals(0.0, NumberFunction.evaluate(new Boolean(false), navigator).doubleValue(), tolerance);
        assertEquals(1.0, NumberFunction.evaluate(new Boolean(true), navigator).doubleValue(), tolerance);
    }


    public void testNumberFunctionRequiresAtMostOneArgument() 
      throws JaxenException {
        
        XPath xpath = new DOMXPath("number('2.2', '1.2')");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed number function with two arguments");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }     
    
    
    public void testNumberFunctionAppliedToNonNumericString() 
      throws JaxenException {
        
        XPath xpath = new DOMXPath("number('Marx')");
        
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(new Double(Double.NaN), result);
        
    }     
    
    public void testIsNan() {
        assertTrue(NumberFunction.isNaN(0.0 / 0.0));
    }     
    
}
