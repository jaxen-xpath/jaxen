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

import org.jaxen.BaseXPath;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;

/**
 * @author Elliotte Rusty Harold
 *
 */
public class NotTest extends TestCase {

    private Document doc;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        doc.appendChild(doc.createElement("root"));
    }


    public NotTest(String name) {
        super(name);
    }

    public void testZeroIsFalse() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("not(0)");
        
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        assertEquals(Boolean.TRUE, result.get(0));
        
    }    

    public void testOneIsTrue() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("not(1)");
        
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        assertEquals(Boolean.FALSE, result.get(0));
        
    }    

    public void testEmptyStringIsFalse() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("not('')");
        
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        assertEquals(Boolean.TRUE, result.get(0));
        
    }    

    public void testNaNIsFalse() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("not(0 div 0)");
        Object result = xpath.evaluate(null);
        assertEquals(Boolean.TRUE, result);
        
    }    

    public void testNonEmptyStringIsTrue() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("not('false')");
        
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        assertEquals(Boolean.FALSE, result.get(0));
        
    }    

    public void testNotFunctionRequiresOneArgument() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("not()");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed not() function with no arguments");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }    

    public void testNotFunctionRequiresExactlyOneArgument() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("not('', '')");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed not() function with two arguments");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }    

}
