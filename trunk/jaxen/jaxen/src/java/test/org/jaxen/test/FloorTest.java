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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.jaxen.BaseXPath;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;

/**
 * @author Elliotte Rusty Harold
 *
 */
public class FloorTest extends TestCase {

    private Document doc;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        doc.appendChild(a);
    }


    public FloorTest(String name) {
        super(name);
    }



    public void testFloor() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("floor(1.5)");
        
        Object result = xpath.evaluate(doc);
        assertEquals(1, ((Double) result).doubleValue(), 0.0001);
        
    }    

    public void testNegativeFloor() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("floor(-1.5)");
        
        Object result = xpath.evaluate(doc);
        assertEquals(-2, ((Double) result).doubleValue(), 0.0001);
        
    }    

    public void testNaNFloorIsNaN() throws JaxenException {
        BaseXPath xpath = new DOMXPath("floor(1.0 div 0.0 - 2.0 div 0.0)");
        double result = ((Double) xpath.evaluate(doc)).doubleValue();
        assertTrue(Double.isNaN(result));
    }    

    public void testInfFloorIsInf() throws JaxenException {
        BaseXPath xpath = new DOMXPath("floor(1.0 div 0.0)");
        double result = ((Double) xpath.evaluate(doc)).doubleValue();
        assertTrue(Double.isInfinite(result));
        assertTrue(result > 0);
    }    

    public void testNegativeInfFloorIsNegativeInf() throws JaxenException {
        BaseXPath xpath = new DOMXPath("floor(-1.0 div 0.0)");
        double result = ((Double) xpath.evaluate(doc)).doubleValue();
        assertTrue(Double.isInfinite(result));
        assertTrue(result < 0);
    }    

    public void testFloorFunctionRequiresAtLeastArgument() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("floor()");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed floor function with no arguments");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }    

    public void testBooleanFunctionRequiresExactlyOneArgument() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("floor(2.2, 1.2)");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed floor function with two arguments");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }    

}
