/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2005 bob mcwhirter & James Strachan.
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


package org.jaxen;

import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import nu.xom.Document;
import nu.xom.Element;

import org.jaxen.dom.DOMXPath;
import org.jaxen.xom.XOMXPath;

import junit.framework.TestCase;

/**
 * <p>
 * Tests for org.jaxen.BaseXPath.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b4
 *
 */
public class BaseXPathTest extends TestCase {

    private org.w3c.dom.Document doc;

    public BaseXPathTest(String name) {
        super(name);
    }
    
    protected void setUp() throws ParserConfigurationException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        doc = factory.newDocumentBuilder().newDocument();
        
    }
    
    public void testSelectSingleNodeForContext() throws JaxenException {
        
        BaseXPath xpath = new BaseXPath("1 + 2");
        
        String stringValue = xpath.stringValueOf(xpath);
        assertEquals("3", stringValue);
        
        Number numberValue = xpath.numberValueOf(xpath);
        assertEquals(3, numberValue.doubleValue(), 0.00001);
        
    }
    
    
    public void testEvaluateString() throws JaxenException {
        
        BaseXPath xpath = new XOMXPath("string(/*)");
        
        Document doc = new Document(new Element("root"));
        String stringValue = (String) xpath.evaluate(doc);
        assertEquals("", stringValue);
        
    }
    
    
    public void testEvaluateWithMultiNodeAnswer() throws JaxenException {
        
        BaseXPath xpath = new XOMXPath("(/descendant-or-self::node())");
        
        Document doc = new Document(new Element("root"));
        List result = (List) xpath.evaluate(doc);
        assertEquals(2, result.size());
        
    }
    
    
    public void testValueOfEmptyListIsEmptyString() throws JaxenException {
        
        BaseXPath xpath = new XOMXPath("/element");
        Document doc = new Document(new Element("root"));
        
        String stringValue = xpath.stringValueOf(doc);
        assertEquals("", stringValue);
        
    }

    public void testAllNodesQuery() throws JaxenException, ParserConfigurationException {
        
        BaseXPath xpath = new DOMXPath("//. | /");
        org.w3c.dom.Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        
        String stringValue = xpath.stringValueOf(doc);
        assertEquals("", stringValue);
        
    }

    
    public void testAncestorAxis() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("ancestor::*");
        org.w3c.dom.Element root = doc.createElementNS("", "Test");
        org.w3c.dom.Element parent = doc.createElementNS("", "Test");
        doc.appendChild(root);
        org.w3c.dom.Element child = doc.createElementNS("", "child");
        root.appendChild(parent);
        parent.appendChild(child);
        
        List result = xpath.selectNodes(child);
        assertEquals(2, result.size());
        assertEquals(root, result.get(0));   
        assertEquals(child, result.get(1));
        
    }    
    
    
    // test case for JAXEN-55
    public void testAbbreviatedDoubleSlashAxis() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//x");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        x1.appendChild(doc.createTextNode("1"));
        a.appendChild(x1);
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
        assertEquals(4, result.size());
        assertEquals(x1, result.get(0));   
        assertEquals(x2, result.get(1));   
        assertEquals(x3, result.get(2));   
        assertEquals(x4, result.get(3));
        
    }    
    
    
    // test case for JAXEN-55
    public void testAncestorFollowedByChildren() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/a/b/x/ancestor::*/child::x");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        x1.appendChild(doc.createTextNode("1"));
        a.appendChild(x1);
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
        assertEquals(4, result.size());
        assertEquals(x1, result.get(0));   
        assertEquals(x2, result.get(1));   
        assertEquals(x3, result.get(2));   
        assertEquals(x4, result.get(3));
        
    }    
    
    
    // test case for JAXEN-55
    public void testDescendantAxis() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/descendant::x");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        x1.appendChild(doc.createTextNode("1"));
        a.appendChild(x1);
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
        assertEquals(4, result.size());
        assertEquals(x1, result.get(0));   
        assertEquals(x2, result.get(1));   
        assertEquals(x3, result.get(2));   
        assertEquals(x4, result.get(3));
        
    }    
    
    
    // another Jaxen-55 test to try to pin down exactly what does
    // and doesn't work
    public void testDescendantOrSelfAxis() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/descendant-or-self::x");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        x1.appendChild(doc.createTextNode("1"));
        a.appendChild(x1);
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
        assertEquals(4, result.size());
        assertEquals(x1, result.get(0));   
        assertEquals(x2, result.get(1));   
        assertEquals(x3, result.get(2));   
        assertEquals(x4, result.get(3));
        
    }    
    
    
    public void testDuplicateNodes() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//x | //*");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        x1.appendChild(doc.createTextNode("1"));
        a.appendChild(x1);
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
        assertEquals(6, result.size());
        
    }    
       
    public void testUnionOfNodesWithNonNodes() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("count(//*) | //x ");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        x1.appendChild(doc.createTextNode("1"));
        a.appendChild(x1);
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
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed union with non-node-set");
        }
        catch (JaxenException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }    
    
    public void testUnionOfEmptyNodeSetWithNonNodes() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//y | count(//*)");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        x1.appendChild(doc.createTextNode("1"));
        a.appendChild(x1);
        a.appendChild(b);
        org.w3c.dom.Element x2 = doc.createElementNS("", "x");
        b.appendChild(x2);
        x2.appendChild(doc.createTextNode("2"));
        
        try {
            xpath.selectNodes(doc);
            fail("Allowed union with non-node-set");
        }
        catch (JaxenException ex) {
            assertNotNull(ex.getMessage());
        }
        
    }    
    
    
}
