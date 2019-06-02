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

import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;

/**
 * @author Elliotte Rusty Harold
 *
 */
public class FalseTest extends TestCase {

    private Document doc;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        doc.appendChild(doc.createElement("root"));
    }


    public FalseTest(String name) {
        super(name);
    }

    public void testFalseOfNumber() throws JaxenException
    {
        try
        {
            XPath xpath = new DOMXPath( "false(3)" );
            xpath.selectNodes( doc );
            fail("false() function took arguments");
        }
        catch (FunctionCallException e) 
        {
            assertEquals("false() requires no arguments.", e.getMessage());
        }
    }
    
    // https://github.com/jaxen-xpath/jaxen/issues/68
    public void testFalseLessThanOrEqualToFalse() throws JaxenException
    {
        XPath xpath = new DOMXPath( "false() <= false()" );
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    } 

    public void testEmptyNodeSetLessThanOrEqualToFalse() throws JaxenException
    {
        XPath xpath = new DOMXPath( "/nonexistent<=false()" );
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    } 

    public void testEmptyNodeSetLessThanFalse() throws JaxenException
    {
        XPath xpath = new DOMXPath( "/nonexistent<false()" );
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    } 

    public void testFalseLessThanOrEqualToEmptyNodeSet() throws JaxenException
    {
        XPath xpath = new DOMXPath( "false()<=/nonexistent" );
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    public void testFalseGreaterThanOrEqualToEmptyNodeSet() throws JaxenException
    {
        XPath xpath = new DOMXPath( "false()>=/nonexistent" );
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    } 
    
    public void testFalseGreaterThaEmptyNodeSet() throws JaxenException
    {
        XPath xpath = new DOMXPath( "false()>/nonexistent" );
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    } 

    public void testFalse() throws JaxenException
    {
        XPath xpath = new DOMXPath( "false()" );
        Object result = xpath.evaluate( doc );
        assertEquals(Boolean.FALSE, result);
    }    

}
