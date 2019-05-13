/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2005 bob mcwhirter & James Strachan.
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;
import org.jaxen.NamespaceContext;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.jaxen.dom.DocumentNavigator;
import org.jaxen.dom.NamespaceNode;
import org.jaxen.pattern.Pattern;
import org.jaxen.saxpath.helpers.XPathReaderFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * <p>
 * Tests for org.jaxen.BaseXPath.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b10
 *
 */
public class BaseXPathTest extends TestCase {

    private org.w3c.dom.Document doc;
    private DocumentBuilder builder;

    public BaseXPathTest(String name) {
        super(name);
    }
    
    protected void setUp() throws ParserConfigurationException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        doc = factory.newDocumentBuilder().newDocument();
        builder = factory.newDocumentBuilder();
        
    }
    
    public void testSelectSingleNodeForContext() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("1 + 2");
        
        String stringValue = xpath.stringValueOf(xpath);
        assertEquals("3", stringValue);
        
        Number numberValue = xpath.numberValueOf(xpath);
        assertEquals(3, numberValue.doubleValue(), 0.00001);
        
    }
    
    
    public void testParentOfSelection() throws JaxenException {
        /*
        html
            a
                img
            a        <- return that node
                img   <- select this node
        */
        XPath xpath = new DOMXPath("(/html/a/img[contains(@src,'gif')])[2]/..");
        org.w3c.dom.Element html = doc.createElementNS("", "html");
        org.w3c.dom.Element a1 = doc.createElementNS("", "a");
        org.w3c.dom.Element a2 = doc.createElementNS("", "a");
        org.w3c.dom.Element img1 = doc.createElementNS("", "img");
          org.w3c.dom.Attr img1_src = doc.createAttributeNS("", "src");
        img1_src.setValue("1.gif");
        org.w3c.dom.Element img2 = doc.createElementNS("", "img");
        org.w3c.dom.Attr img2_src = doc.createAttributeNS("", "src");
        img2_src.setValue("2.gif");

        img1.setAttributeNode(img1_src);
        img2.setAttributeNode(img2_src);
        a1.appendChild(img1);
        a2.appendChild(img2);
        html.appendChild(a1);
        html.appendChild(a2);
        doc.appendChild(html);

        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        assertEquals(a2, result.get(0));
    }


    
    
    public void testEvaluateString() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("string(/*)");
        
        doc.appendChild(doc.createElement("root"));
        String stringValue = (String) xpath.evaluate(doc);
        assertEquals("", stringValue);
        
    }
    
    
    public void testNumberValueOfEmptyNodeSetIsNaN() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/x");
        
        doc.appendChild(doc.createElement("root"));
        Double numberValue = (Double) xpath.numberValueOf(doc);
        assertTrue(numberValue.isNaN());
        
    }
    
    
    public void testPathWithParentheses() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("(/root)/child");
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element child = doc.createElement("child");
        root.appendChild(child);
        
        assertEquals(child, xpath.selectSingleNode(doc));
        
    }
    
    
    public void testEvaluateWithMultiNodeAnswer() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("(/descendant-or-self::node())");
        
        doc.appendChild(doc.createElement("root"));
        List result = (List) xpath.evaluate(doc);
        assertEquals(2, result.size());
        
    }
    
    
    public void testValueOfEmptyListIsEmptyString() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/element");
        doc.appendChild(doc.createElement("root"));
        
        String stringValue = xpath.stringValueOf(doc);
        assertEquals("", stringValue);
        
    }

    public void testAllNodesQuery() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("//. | /");
        org.w3c.dom.Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        
        String stringValue = xpath.stringValueOf(doc);
        assertEquals("", stringValue);
        
    }

    
    public void testAncestorAxis() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("ancestor::*");
        org.w3c.dom.Element root = doc.createElementNS("", "root");
        org.w3c.dom.Element parent = doc.createElementNS("", "parent");
        doc.appendChild(root);
        org.w3c.dom.Element child = doc.createElementNS("", "child");
        root.appendChild(parent);
        parent.appendChild(child);
        
        List result = xpath.selectNodes(child);
        assertEquals(2, result.size());
        assertEquals(root, result.get(0));   
        assertEquals(parent, result.get(1));
        
    }    
    
    
    public void testPrecedingSiblingAxisIsInDocumentOrder() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("preceding-sibling::*");
        org.w3c.dom.Element root = doc.createElementNS("", "root");
        doc.appendChild(root);
        org.w3c.dom.Element child1 = doc.createElementNS("", "child1");
        root.appendChild(child1);
        org.w3c.dom.Element child2 = doc.createElementNS("", "child2");
        root.appendChild(child2);
        org.w3c.dom.Element child3 = doc.createElementNS("", "child3");
        root.appendChild(child3);
        
        List result = xpath.selectNodes(child3);
        assertEquals(2, result.size());
        assertEquals(child1, result.get(0));   
        assertEquals(child2, result.get(1));
        
    }    
    
    
    public void testPrecedingAxisIsInDocumentOrder() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("preceding::*");
        org.w3c.dom.Element root = doc.createElementNS("", "root");
        doc.appendChild(root);
        org.w3c.dom.Element parent1 = doc.createElementNS("", "parent1");
        root.appendChild(parent1);
        org.w3c.dom.Element parent2 = doc.createElementNS("", "parent2");
        root.appendChild(parent2);
        org.w3c.dom.Element child1 = doc.createElementNS("", "child1");
        parent2.appendChild(child1);
        org.w3c.dom.Element child2 = doc.createElementNS("", "child2");
        parent2.appendChild(child2);
        org.w3c.dom.Element child3 = doc.createElementNS("", "child3");
        parent2.appendChild(child3);
        
        List result = xpath.selectNodes(child3);
        assertEquals(3, result.size());
        assertEquals(parent1, result.get(0));   
        assertEquals(child1, result.get(1));   
        assertEquals(child2, result.get(2));
        
    }    
    
    
    public void testPrecedingAxisWithPositionalPredicate() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("preceding::*[1]");
        org.w3c.dom.Element root = doc.createElementNS("", "root");
        doc.appendChild(root);
        org.w3c.dom.Element child1 = doc.createElementNS("", "child1");
        root.appendChild(child1);
        org.w3c.dom.Element child2 = doc.createElementNS("", "child2");
        root.appendChild(child2);
        org.w3c.dom.Element child3 = doc.createElementNS("", "child3");
        root.appendChild(child3);
        
        List result = xpath.selectNodes(child3);
        assertEquals(1, result.size());  
        assertEquals(child2, result.get(0));
        
    }    
    
    
    public void testAncestorAxisWithPositionalPredicate() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("ancestor::*[1]");
        org.w3c.dom.Element root = doc.createElementNS("", "root");
        doc.appendChild(root);
        org.w3c.dom.Element child1 = doc.createElementNS("", "child1");
        root.appendChild(child1);
        org.w3c.dom.Element child2 = doc.createElementNS("", "child2");
        child1.appendChild(child2);
        org.w3c.dom.Element child3 = doc.createElementNS("", "child3");
        child2.appendChild(child3);
        
        List result = xpath.selectNodes(child3);
        assertEquals(1, result.size());  
        assertEquals(child2, result.get(0));
        
    }    
    
    
    public void testAncestorOrSelfAxis() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("ancestor-or-self::*");
        org.w3c.dom.Element root = doc.createElementNS("", "root");
        org.w3c.dom.Element parent = doc.createElementNS("", "parent");
        doc.appendChild(root);
        org.w3c.dom.Element child = doc.createElementNS("", "child");
        root.appendChild(parent);
        parent.appendChild(child);
        
        List result = xpath.selectNodes(child);
        assertEquals(3, result.size());
        assertEquals(root, result.get(0));   
        assertEquals(parent, result.get(1));
        assertEquals(child, result.get(2));
        
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
    
    public void testDescendantAxisWithAttributes() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/descendant::x/@*");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        a.appendChild(x1);
        a.appendChild(b);
        org.w3c.dom.Element x2 = doc.createElementNS("", "x");
        org.w3c.dom.Element x3 = doc.createElementNS("", "x");
        org.w3c.dom.Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        
        Attr a1 = doc.createAttribute("name");
        a1.setNodeValue("1");
        x1.setAttributeNode(a1);
        Attr a2 = doc.createAttribute("name");
        a2.setNodeValue("2");
        x2.setAttributeNode(a2);
        Attr a3 = doc.createAttribute("name");
        a3.setNodeValue("3");
        x3.setAttributeNode(a3);
        Attr a4 = doc.createAttribute("name");
        a4.setNodeValue("4");
        x4.setAttributeNode(a4);
        
        List result = xpath.selectNodes(doc);
        assertEquals(4, result.size());
        assertEquals(a1, result.get(0));   
        assertEquals(a2, result.get(1));   
        assertEquals(a3, result.get(2));   
        assertEquals(a4, result.get(3));
        
    }    
    
    public void testDescendantAxisWithNamespaceNodes() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/descendant::x/namespace::node()");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        a.appendChild(x1);
        a.appendChild(b);
        org.w3c.dom.Element x2 = doc.createElementNS("", "x");
        org.w3c.dom.Element x3 = doc.createElementNS("", "x");
        org.w3c.dom.Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        
        Attr a1 = doc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:a");
        a1.setNodeValue("http://www.example.org/");
        x1.setAttributeNode(a1);
        Attr a2 = doc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:b");
        a2.setNodeValue("http://www.example.org/");
        x2.setAttributeNode(a2);
        Attr a3 = doc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:c");
        a3.setNodeValue("http://www.example.org/");
        x3.setAttributeNode(a3);
        Attr a4 = doc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:d");
        a4.setNodeValue("http://www.example.org/");
        x4.setAttributeNode(a4);
        
        List result = xpath.selectNodes(doc);
        assertEquals(8, result.size());
        Iterator iterator = result.iterator();
        StringBuffer sb = new StringBuffer(4);
        while (iterator.hasNext()) {
            NamespaceNode ns = (NamespaceNode) iterator.next();
            if (ns.getNodeValue().equals("http://www.example.org/")) {
                String name = ns.getNodeName();
                sb.append(name);
            }
        }
        assertEquals("abcd", sb.toString());
        
    }    
    
    public void testMultipleAttributesOnElement() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/descendant::x/@*");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        a.appendChild(x1);
        a.appendChild(b);
        
        Attr a1 = doc.createAttribute("name1");
        a1.setNodeValue("1");
        x1.setAttributeNode(a1);
        Attr a2 = doc.createAttribute("name2");
        a2.setNodeValue("2");
        x1.setAttributeNode(a2);
        Attr a3 = doc.createAttribute("name3");
        a3.setNodeValue("3");
        x1.setAttributeNode(a3);
        Attr a4 = doc.createAttribute("name4");
        a4.setNodeValue("4");
        x1.setAttributeNode(a4);
        
        List result = xpath.selectNodes(doc);
        assertEquals(4, result.size());
        assertTrue(result.contains(a1));
        assertTrue(result.contains(a2));
        assertTrue(result.contains(a3));
        assertTrue(result.contains(a4));
        
    }   
    
    public void testXMLNamespaceAttributeOrderOnAncestorAxis() 
      throws JaxenException {
     
        org.w3c.dom.Element superroot = doc.createElement("superroot");
        doc.appendChild(superroot);
        org.w3c.dom.Element root = doc.createElement("root");
        superroot.appendChild(root);
        
        org.w3c.dom.Attr p0 = doc.createAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:id");
        p0.setValue("p0");
        superroot.setAttributeNodeNS(p0);
        org.w3c.dom.Attr p1 = doc.createAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:id");
        p1.setValue("p1");
        root.setAttributeNodeNS(p1);
        
        org.w3c.dom.Element child = doc.createElement("child312");
        root.appendChild(child);
        
        BaseXPath xpath = new DOMXPath("ancestor::*/@xml:*");
        List result = xpath.selectNodes(child);
        assertEquals(2, result.size());
        assertEquals(p0, result.get(0));
        assertEquals(p1, result.get(1));
        
    }
    
    public void testDescendantAxisWithAttributesAndChildren() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/descendant::x/@* | /descendant::x");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        org.w3c.dom.Element x1 = doc.createElementNS("", "x");
        a.appendChild(x1);
        a.appendChild(b);
        org.w3c.dom.Element x2 = doc.createElementNS("", "x");
        org.w3c.dom.Element x3 = doc.createElementNS("", "x");
        org.w3c.dom.Element x4 = doc.createElementNS("", "x");
        a.appendChild(x4);
        b.appendChild(x2);
        b.appendChild(x3);
        
        Attr a1 = doc.createAttribute("name");
        a1.setNodeValue("1");
        x1.setAttributeNode(a1);
        Attr a2 = doc.createAttribute("name");
        a2.setNodeValue("2");
        x2.setAttributeNode(a2);
        Attr a3 = doc.createAttribute("name");
        a3.setNodeValue("3");
        x3.setAttributeNode(a3);
        Attr a4 = doc.createAttribute("name");
        a4.setNodeValue("4");
        x4.setAttributeNode(a4);
        
        List result = xpath.selectNodes(doc);
        assertEquals(8, result.size());
        assertEquals(x1, result.get(0));   
        assertEquals(a1, result.get(1));   
        assertEquals(x2, result.get(2));   
        assertEquals(a2, result.get(3));
        assertEquals(x3, result.get(4));   
        assertEquals(a3, result.get(5));   
        assertEquals(x4, result.get(6));   
        assertEquals(a4, result.get(7));
        
    }    
    
    public void testAncestorAxisWithAttributes() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("ancestor::*/@*");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
        a.appendChild(b);
        org.w3c.dom.Element x3 = doc.createElementNS("", "x");
        b.appendChild(x3);
        
        Attr a1 = doc.createAttribute("name");
        a1.setNodeValue("1");
        a.setAttributeNode(a1);
        Attr a2 = doc.createAttribute("name");
        a2.setNodeValue("2");
        b.setAttributeNode(a2);
        Attr a3 = doc.createAttribute("name");
        x3.setNodeValue("3");
        x3.setAttributeNode(a3);
        
        List result = xpath.selectNodes(x3);
        assertEquals(2, result.size());
        assertEquals(a1, result.get(0));   
        assertEquals(a2, result.get(1)); 
        
    }    
    
    // test for Jaxen-83
    public void testPrincipalNodeTypeOfSelfAxisIsElement() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("child/@*[self::test]");
        org.w3c.dom.Element a = doc.createElementNS("", "child");
        org.w3c.dom.Attr test = doc.createAttributeNS("", "test");
        test.setValue("value");
        a.setAttributeNode(test);
        doc.appendChild(a);
        
        List result = xpath.selectNodes(doc);
        assertEquals(0, result.size()); 
        
    }
    
    // test to make sure Jaxen-83 fix doesn't go too far
    public void testSelfAxisWithNodeTestCanReturnNonPrincipalNodeType() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("child/@*[self::node()]");
        org.w3c.dom.Element a = doc.createElementNS("", "child");
        org.w3c.dom.Attr test = doc.createAttributeNS("", "test");
        test.setValue("value");
        a.setAttributeNode(test);
        doc.appendChild(a);
        
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());   
        
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
    
    public void testSelectSingleNodeSelectsNothing() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("id('p1')");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        doc.appendChild(a);
        Object result = xpath.selectSingleNode(doc);
        assertNull(result);
        
    }  
    
    public void testSAXPathExceptionThrownFromConstructor() {
        
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "java.lang.String" );
        
        try {
            new DOMXPath("id('p1')");
        }
        catch (JaxenException e) {
            assertNotNull(e.getMessage());
        }
        finally {
            System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "" );
        }
        
    }  
    
    public void testBooleanValueOfEmptyNodeSetIsFalse() 
      throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/b/c");
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        doc.appendChild(a);
        List result = xpath.selectNodes(doc);
        assertTrue(! xpath.booleanValueOf(result));
        
    } 
    
    public void testAddNamespaceWithNonSimpleNamespaceContext() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/b/c");
        xpath.setNamespaceContext(new NamespaceContext() {

            public String translateNamespacePrefixToUri(String prefix) {
                return prefix;
            }
            
        });
        try {
            xpath.addNamespace("pre", "foo");            
            fail("Added namespace");
        }
        catch (JaxenException ex) {
            assertNotNull(ex.getMessage());
        }
        
    } 
    
    public void testDebug() throws JaxenException {
        BaseXPath xpath = new DOMXPath("/b/c");
        assertEquals(
          "[(DefaultXPath): [(DefaultAbsoluteLocationPath): [(DefaultNameStep): b]/[(DefaultNameStep): c]]]", 
          xpath.debug()
        );
        
    } 
    
    public void testGetRootExpr() throws JaxenException {
        BaseXPath xpath = new DOMXPath("/b/c");
        assertTrue(xpath.getRootExpr() instanceof org.jaxen.expr.LocationPath);
    } 
    
    public void testUnionUsesDocumentOrder() throws JaxenException {
        
        BaseXPath xpath = new DOMXPath("/descendant::x | /a | /a/b");
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
        assertEquals(a, result.get(0));   
        assertEquals(x1, result.get(1));   
        assertEquals(b, result.get(2));   
        assertEquals(x2, result.get(3));   
        assertEquals(x3, result.get(4));   
        assertEquals(x4, result.get(5));
        
    }
    
    public void testArithmeticAssociativity() throws JaxenException {
        XPath xpath = new DOMXPath("2+1-1+1");
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(3, result.intValue());
    }
    
    public void testLogicalAssociativity() throws JaxenException {
        XPath xpath = new DOMXPath("false() or true() and true() and false()");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testRelationalAssociativity3() throws JaxenException {
        XPath xpath = new DOMXPath("3 > 2 > 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testRelationalAssociativity4() throws JaxenException {
        XPath xpath = new DOMXPath("4 > 3 > 2 > 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testRelationalGTAssociativity5() throws JaxenException {
        XPath xpath = new DOMXPath("5 > 4 > 3 > 2 > 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testRelationalLTAssociativity5() throws JaxenException {
        XPath xpath = new DOMXPath("1 < 2 < 3 < 4 < 5");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    public void testRelationalLEAssociativity5() throws JaxenException {
        XPath xpath = new DOMXPath("1 <= 2 <= 3 <= 4 <= 5");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    public void testRelationalGEAssociativity5() throws JaxenException {
        XPath xpath = new DOMXPath("5 >= 4 >= 3 >= 2 >= 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testRelationalGEAssociativity3() throws JaxenException {
        XPath xpath = new DOMXPath("3 >= 2 >= 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    public void testRelationalGEAssociativity2() throws JaxenException {
        XPath xpath = new DOMXPath("2 >= 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    public void testRelationalGEAssociativity4() throws JaxenException {
        XPath xpath = new DOMXPath("4 >= 3 >= 2 >= 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    // This is the same test but with parentheses to make explicit
    // how the previous test should be evaluated.
    public void testRelationalAssociativity5P() throws JaxenException {
        XPath xpath = new DOMXPath("((((5 > 4) > 3) > 2) > 1)");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testInequalityAssociativity5() throws JaxenException {
        XPath xpath = new DOMXPath("2 != 3 != 1 != 4 != 0");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    // This is the same test but with parentheses to make explicit
    // how the previous test should be evaluated.
    public void testInequalityAssociativity5P() throws JaxenException {
        XPath xpath = new DOMXPath("(((2 != 3) != 1) != 4) != 0");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    public void testInequalityAssociativity5B() throws JaxenException {
        XPath xpath = new DOMXPath("2 != 3 != 1 != 4 != 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    // This is the same test but with parentheses to make explicit
    // how the previous test should be evaluated.
    public void testInequalityAssociativity5BP() throws JaxenException {
        XPath xpath = new DOMXPath("(((2 != 3) != 1) != 4) != 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testEqualityAssociativity5() throws JaxenException {
        XPath xpath = new DOMXPath("2 = 3 = 1 = 4 = 0");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    // This is the same test but with parentheses to make explicit
    // how the previous test should be evaluated.
    public void testEqualityAssociativity5P() throws JaxenException {
        XPath xpath = new DOMXPath("(((2 = 3) = 1) = 4) = 0");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    public void testEqualityAssociativity5B() throws JaxenException {
        XPath xpath = new DOMXPath("2 = 3 = 1 = 4 = 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    // This is the same test but with parentheses to make explicit
    // how the previous test should be evaluated.
    public void testEqualityAssociativity5BP() throws JaxenException {
        XPath xpath = new DOMXPath("(((2 = 3) = 1) = 4) = 1");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testMoreComplexArithmeticAssociativity() throws JaxenException {
        XPath xpath = new DOMXPath("1+2+1-1+1");
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(4, result.intValue());
    }
    
    
    public void testMostComplexArithmeticAssociativity() throws JaxenException {
        XPath xpath = new DOMXPath("1+1+2+1-1+1");
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(5, result.intValue());
    }
    
    
    public void testSimplerArithmeticAssociativity() throws JaxenException {
        XPath xpath = new DOMXPath("1-1+1");
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(1, result.intValue());
    }
    
    
    public void testNamespaceNodesComeBeforeAttributeNodesInDocumentOrder() throws JaxenException {
        
        org.w3c.dom.Element root = doc.createElementNS("http://www.example.org", "pre:b");
        doc.appendChild(root);
        root.setAttribute("name", "value");
        XPath xpath = new DOMXPath("/*/attribute::* | /*/namespace::node()");
        List result = xpath.selectNodes(doc);
        assertTrue(((org.w3c.dom.Node) result.get(0)).getNodeType() == Pattern.NAMESPACE_NODE);
        assertTrue(((org.w3c.dom.Node) result.get(1)).getNodeType() == Pattern.NAMESPACE_NODE);
        assertTrue(((org.w3c.dom.Node) result.get(2)).getNodeType() == Node.ATTRIBUTE_NODE);
        
        // now flip the order of the statement and retest
        xpath = new DOMXPath("/*/namespace::node() | /*/attribute::* ");
        result = xpath.selectNodes(doc);
        assertTrue(((org.w3c.dom.Node) result.get(0)).getNodeType() == Pattern.NAMESPACE_NODE);
        assertTrue(((org.w3c.dom.Node) result.get(1)).getNodeType() == Pattern.NAMESPACE_NODE);
        assertTrue(((org.w3c.dom.Node) result.get(2)).getNodeType() == Node.ATTRIBUTE_NODE);
   
    }

    public void testJaxen97() throws JaxenException {
        // jaxen 97 claims this expression throws an exception.
        new DOMXPath("/aaa:element/text()");
    }

    public void testAttributeNodesOnParentComeBeforeNamespaceNodesInChildInDocumentOrder() 
     throws JaxenException {
        
        org.w3c.dom.Element root = doc.createElement("root");
        doc.appendChild(root);
        root.setAttribute("name", "value");
        Element child = doc.createElementNS("http://www.example.org", "pre:child");
        root.appendChild(child);
        
        XPath xpath = new DOMXPath("/*/*/namespace::node() | //attribute::* ");
        List result = xpath.selectNodes(doc);
        assertEquals(3, result.size());
        assertTrue(((org.w3c.dom.Node) result.get(0)).getNodeType() == Node.ATTRIBUTE_NODE);
        assertTrue(((org.w3c.dom.Node) result.get(1)).getNodeType() == Pattern.NAMESPACE_NODE);
   
    }

    public void testJaxen107() throws JaxenException {
        
        org.w3c.dom.Element a = doc.createElementNS("http://www.a.com/", "a:foo");
        doc.appendChild(a);
        Element b = doc.createElementNS("http://www.b.com/", "b:bar");
        a.appendChild(b);
        
        XPath xpath = new DOMXPath("/a:foo/b:bar/namespace::*/parent::*");
        SimpleNamespaceContext context1 = new SimpleNamespaceContext();
        context1.addNamespace("a", "http://www.a.com/");
        context1.addNamespace("b", "http://www.b.com/");
        xpath.setNamespaceContext(context1);
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        assertEquals(b, result.get(0));
   
    }
    
    
    public void testJaxen107FromFile() throws JaxenException, SAXException, IOException {
        
        doc = builder.parse(new File("xml/testNamespaces.xml"));
        XPath xpath = new DOMXPath("/Template/Application2/namespace::*/parent::*");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
   
    }
    
    public void testSelectNodesReturnsANonNodeSet() throws JaxenException {
        XPath xpath = new DOMXPath("1 + 2 + 3");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
    }
    
    public void testNonElementContextNode() throws JaxenException {
        
        org.w3c.dom.Element a = doc.createElementNS("http://www.a.com/", "a:foo");
        doc.appendChild(a);
        Text b = doc.createTextNode("ready");
        a.appendChild(b);
        
        XPath xpath = new DOMXPath("..");
        List result = (List) xpath.evaluate(b);
        assertEquals(1, result.size());
        assertEquals(a, result.get(0));
   
    }
    
    public void testNonNodeContext() throws JaxenException {
        
        org.w3c.dom.Element a = doc.createElementNS("http://www.a.com/", "a:foo");
        doc.appendChild(a);
        Text b = doc.createTextNode("ready");
        a.appendChild(b);
        
        XPath xpath = new DOMXPath("..");
        try {
            xpath.evaluate("String");
            fail("Allowed String as context");
        }
        catch (ClassCastException ex) {
            // success
        }
   
    }
    
    public void testIsSerializable() throws JaxenException, IOException {
        
        BaseXPath path = new BaseXPath("//foo", new DocumentNavigator());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(path);
        oos.close();
        assertTrue(out.toByteArray().length > 0);
        
    }
    
    
    // JAXEN-206
    public void testMismatchedDepthsInContext()
      throws JaxenException {
        XPath xpath = new DOMXPath("parent::*");
        org.w3c.dom.Element z = doc.createElementNS("", "z");
        doc.appendChild(z);
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        z.appendChild(a);
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        a.appendChild(b);
        org.w3c.dom.Element c = doc.createElementNS("", "c");
        z.appendChild(c);

        List context = new ArrayList();
        context.add(b);
        context.add(c);
        List result = xpath.selectNodes(context);
        assertEquals(z, result.get(0));
    }
    
}
