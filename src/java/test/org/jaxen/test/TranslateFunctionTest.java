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
public class TranslateFunctionTest extends TestCase {

    private Document doc;
    
    protected void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        doc.appendChild(doc.createElement("root"));
    }

    public void testTranslate() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('abc', 'b', 'd')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("adc", result);
    }    
  
    public void testTranslateIgnoresExtraArguments() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('abc', 'b', 'dghf')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("adc", result);
    }    
  
    public void testTranslateFunctionRequiresAtLeastThreeArguments() 
      throws JaxenException {
        
        XPath xpath = new DOMXPath("translate('a', 'b')");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed translate function with two arguments");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }    

    public void testTranslateRequiresAtMostThreeArguments() 
      throws JaxenException {
        
        XPath xpath = new DOMXPath("substring-after('a', 'a', 'a', 'a')");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed translate function with four arguments");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }   
    
    public void testTranslateStringThatContainsNonBMPChars() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('ab\uD834\uDD00b', 'b', 'd')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("ad\uD834\uDD00d", result);
    }
    
    public void testTranslateStringThatContainsPrivateUseChars() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('ab\uE000\uE001', '\uE000', '\uE001')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("ab\uE001\uE001", result);
    }
    
    public void testTranslateNonBMPChars() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('ab\uD834\uDD00b', '\uD834\uDD00', 'd')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("abdb", result);
    }   
    

    public void testTranslateNonBMPChars2() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('ab\uD834\uDD00b', '\uD834\uDD00', 'da')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("abdb", result);
    }   
    

    public void testTranslateWithNonBMPChars() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('abc', 'c', '\uD834\uDD00')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("ab\uD834\uDD00", result);
    }   
    

    public void testTranslateWithNonBMPChars2() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('abc', 'c', '\uD834\uDD00b')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("ab\uD834\uDD00", result);
    }   
    

    public void testTranslateWithMalformedSurrogatePair() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('abc', 'c', '\uD834X\uDD00b')" );
        try {
            xpath.evaluate( doc );
            fail("Allowed malformed surrogate pair");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
    }   
    

    public void testTranslateWithMissingLowSurrogate() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('abc', 'c', 'AB\uD834X')" );
        try {
            xpath.evaluate( doc );
            fail("Allowed malformed surrogate pair");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
    }   
    

    public void testTranslateWithExtraCharsInReplacementString() throws JaxenException
    {
        XPath xpath = new DOMXPath( "translate('abc', 'c', 'def')" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("abd", result);
    }   
    

}
