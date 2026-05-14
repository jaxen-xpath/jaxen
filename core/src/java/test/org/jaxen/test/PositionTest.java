/*
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
 * Jaxen Project, please see <https://github.com/jaxen-xpath/jaxen/>.
 */

package org.jaxen.test;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Elliotte Rusty Harold
 */
public class PositionTest extends TestCase {

    private Document doc;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);
        root.appendChild(doc.createElement("item"));
        root.appendChild(doc.createElement("item"));
        root.appendChild(doc.createElement("item"));
    }


    public PositionTest(String name) {
        super(name);
    }

    public void testPositionOfNumber() throws JaxenException
    {
        try
        {
            XPath xpath = new DOMXPath( "position(3)" );
            xpath.selectNodes( doc );
            fail("position() function took arguments");
        }
        catch (FunctionCallException e) 
        {
            assertEquals("position() does not take any arguments.", e.getMessage());
        }
    }

    /**
     * Per XPath 1.0 section 2.4, when a predicate evaluates to a number, the
     * predicate is true only if that number equals the context position using
     * IEEE 754 floating-point equality. A non-integer value like 1.5 should
     * never equal any integer position, so no nodes should be selected.
     *
     * The buggy implementation calls intValue() which truncates 1.5 to 1,
     * incorrectly matching position 1.
     */
    public void testNonIntegerNumericPredicateSelectsNoNodes() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//item[1.5]" );
        List<?> result = xpath.selectNodes( doc );
        assertEquals("Predicate [1.5] must not match any node (1.5 != 1 and 1.5 != 2 in IEEE 754)",
                0, result.size());
    }

    /**
     * Verify the symmetric case: [2.5] should likewise select nothing.
     */
    public void testNonIntegerNumericPredicateSelectsNoNodes2() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//item[2.5]" );
        List<?> result = xpath.selectNodes( doc );
        assertEquals("Predicate [2.5] must not match any node (2.5 != 2 and 2.5 != 3 in IEEE 754)",
                0, result.size());
    }

    /**
     * Sanity check: an integer-valued predicate (e.g. [1.0]) must still
     * select the node at that position, because 1.0 == 1.0 under IEEE 754.
     */
    public void testIntegerValuedNumericPredicateSelectsCorrectNode() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//item[1.0]" );
        List<?> result = xpath.selectNodes( doc );
        assertEquals("Predicate [1.0] must select exactly the first node", 1, result.size());
    }

}
