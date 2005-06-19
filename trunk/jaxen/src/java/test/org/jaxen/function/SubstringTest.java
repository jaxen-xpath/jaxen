/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2005 Elliotte Rusty Harold.
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

package org.jaxen.function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.jaxen.BaseXPath;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;

/**
 * @author Elliotte Rusty Harold
 *
 */
public class SubstringTest extends TestCase {

    private Document doc;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        doc.appendChild(doc.createElement("root"));
    }


    public SubstringTest(String name) {
        super(name);
    }

    public void testSubstringOfNumber() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring(1234, 3)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("34", result);
    }    
  
    public void testSubstringOfNumber2() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring(1234, 2, 3)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("234", result);
    }    
    
    // Unusual tests from XPath spec
    
    public void testUnusualSubstring1() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', 1.5, 2.6)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("234", result);
    }    


    public void testUnusualSubstring2() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', 0, 3)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("12", result);
    }    


    public void testUnusualSubstring3() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', 0 div 0, 3)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("", result);
    }    

    public void testUnusualSubstring4() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', 1, 0 div 0)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("", result);
    }    

    public void testUnusualSubstring5() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', -42, 1 div 0)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("12345", result);
    }    

    public void testUnusualSubstring6() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', -1 div 0, 1 div 0)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("", result);
    }    

  
    public void testSubstringOfNaN() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring(0 div 0, 2)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("aN", result);
    }    
 
  
    public void testSubstringOfEmptyString() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('', 2)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("", result);
    }    
 
  
    public void testSubstringWithNegativeLength() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', 2, -3)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("", result);
    }    
 
  
    public void testSubstringWithExcessiveLength() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', 2, 32)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("2345", result);
    }    
 
  
    public void testSubstringWithNegativeLength2() throws JaxenException
    {
        XPath xpath = new DOMXPath( "substring('12345', 2, -1)" );
        String result = (String) xpath.evaluate( doc );
        assertEquals("", result);
    }    
 
  
    public void testSubstringFunctionRequiresAtLeastTwoArguments() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("substring('a')");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed substring function with one argument");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }    

    public void testSubstringFunctionRequiresAtMostThreeArguments() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("substring('a', 1, 1, 4)");
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed substring function with four arguments");
        }
        catch (FunctionCallException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }

}
