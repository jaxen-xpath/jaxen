/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2003 bob mcwhirter & James Strachan.
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

import junit.framework.TestCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jaxen.JaxenException;
import org.jaxen.SimpleVariableContext;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DOMXPathTest extends TestCase
{

    private static final String BASIC_XML = "xml/basic.xml";
    private Document doc;
    private DocumentBuilderFactory factory;

    public DOMXPathTest(String name)
    {
        super( name );
    }
    
    public void setUp() throws ParserConfigurationException {
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        doc = factory.newDocumentBuilder().newDocument();        
    }
    
    // Jaxen-221
    public void  testTimSort() throws ParserConfigurationException, JaxenException {
        Element root = doc.createElement("root");
        doc.appendChild(root);

        // need at least 32 of each to trigger the bug
        for (int i = 0; i < 32; i++) {
            root.setAttribute("att" + i, "one");
        }
        for (int i = 0; i < 32; i++) {
            root.appendChild(doc.createElement("child"));
        }

        XPath xp = new DOMXPath("//@*[string() = 'one'] | //* ");
        xp.evaluate(doc);
    }
    
    public void testNamespaceNodesPrecedeAttributeNodes() throws ParserConfigurationException, JaxenException {
        Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        root.setAttribute("att", "one");

        XPath xp = new DOMXPath("//@* | //namespace::* ");
        List result = (List) xp.evaluate(doc);
        assertEquals(3, result.size());
        Node third = (Node) result.get(2);
        assertEquals(Node.ATTRIBUTE_NODE, third.getNodeType());
    }
    
    public void testNamespaceNodesPrecedeAttributeNodes2() throws ParserConfigurationException, JaxenException {
        Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        root.setAttribute("att", "one");

        XPath xp = new DOMXPath("//namespace::* | //@* ");
        List result = (List) xp.evaluate(doc);
        assertEquals(3, result.size());
        Node third = (Node) result.get(2);
        assertEquals(Node.ATTRIBUTE_NODE, third.getNodeType());
    }
    
    public void testConstruction() throws JaxenException
    {
        DOMXPath xpath = new DOMXPath( "/foo/bar/baz" );
        assertEquals("/foo/bar/baz", xpath.toString());
    }
    
    public void testJaxen193() throws JaxenException
    {
        DOMXPath xpath = new DOMXPath( "/*[ * or processing-instruction() ]" );
        assertEquals("/*[ * or processing-instruction() ]", xpath.toString());
    }
    
    public void testJaxen193InReverse() throws JaxenException
    {
        DOMXPath xpath = new DOMXPath( "/*[ processing-instruction() or *]" );
        assertEquals("/*[ processing-instruction() or *]", xpath.toString());
    }
    
    public void testConstructionWithNamespacePrefix() throws JaxenException
    {
        DOMXPath xpath = new DOMXPath( "/p:foo/p:bar/a:baz" );
        assertEquals("/p:foo/p:bar/a:baz", xpath.toString());
    }
    
    public void testNamespaceDeclarationsAreNotAttributes() 
      throws JaxenException {
        
        Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.example.org/");
        
        DOMXPath xpath = new DOMXPath( "count(/*/@*)" );
        
        Number value = xpath.numberValueOf(doc);
        assertEquals(0, value.intValue());
        
    }

    public void testFunctionWithNamespace() throws IOException, JaxenException {
        Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        XPath xpath = new DOMXPath("//*[not(self::pre:test)]");
        xpath.addNamespace("pre", "http://www.example.org/");
        xpath.addNamespace("", "http://www.example.org/");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
    }
    
    public void testVariableWithNamespace() throws IOException, JaxenException {
        Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        XPath xpath = new DOMXPath("//*[not($foo)]");
        SimpleVariableContext variables = new SimpleVariableContext();
        variables.setVariableValue("foo", "bar");
        xpath.setVariableContext(variables);
        xpath.addNamespace("", "http://www.example.org/");
        List result = xpath.selectNodes(doc);
        assertEquals(0, result.size());
    }

    public void testElementWithoutNamespace() throws IOException, JaxenException {
        Element root = doc.createElement("root");
        doc.appendChild(root);
        XPath xpath = new DOMXPath("//root");
        xpath.addNamespace("", "http://www.example.org/");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
    }

    // see JAXEN-214
    public void testAttributeNodesDontHaveChildren() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        root.setAttribute("name", "value");
        
        XPath xpath = new DOMXPath("//@*/text()");
        List result = xpath.selectNodes(doc);
        assertEquals(0, result.size());
    }
    
    // see JAXEN-105
    public void testConsistentNamespaceDeclarations() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element child = doc.createElementNS("http://www.example.org", "foo:child");
        root.appendChild(child);
        // different prefix
        child.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo2", "http://www.contradictory.org");
        
        XPath xpath = new DOMXPath("//namespace::node()");
        List result = xpath.selectNodes(doc);
        assertEquals(4, result.size());
        
    }

    // see JAXEN-105
    public void testInconsistentNamespaceDeclarations() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element child = doc.createElementNS("http://www.example.org", "foo:child");
        root.appendChild(child);
        // same prefix
        child.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://www.contradictory.org");
        
        XPath xpath = new DOMXPath("//namespace::node()");
        List result = xpath.selectNodes(doc);
        assertEquals(3, result.size());
        
    }

    // see JAXEN-105
    public void testIntrinsicNamespaceDeclarationOfElementBeatsContradictoryXmlnsPreAttr() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element child = doc.createElementNS("http://www.example.org", "foo:child");
        root.appendChild(child);
        // same prefix
        child.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://www.contradictory.org");
        
        XPath xpath = new DOMXPath("//namespace::node()[name(.)='foo']");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Node node = (Node) result.get(0);
        assertEquals("http://www.example.org", node.getNodeValue());
        
    }    
    
    // see JAXEN-105
    public void testIntrinsicNamespaceDeclarationOfAttrBeatsContradictoryXmlnsPreAttr() 
      throws JaxenException {
        
        Element root = doc.createElement("root");
        doc.appendChild(root);
        root.setAttributeNS("http://www.example.org/", "foo:a", "value");
        // same prefix, different namespace
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://www.contradictory.org");
        
        XPath xpath = new DOMXPath("//namespace::node()[name(.)='foo']");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Node node = (Node) result.get(0);
        assertEquals("http://www.example.org/", node.getNodeValue());
        
    }    
    
    // see JAXEN-105
    public void testIntrinsicNamespaceDeclarationOfElementBeatsContradictoryIntrinsicNamespaceOfAttr() 
      throws JaxenException {
        
        Element root = doc.createElementNS("http://www.example.org", "pre:root");
        doc.appendChild(root);
        // same prefix
        root.setAttributeNS("http://www.contradictory.org", "pre:foo", "value");
        
        XPath xpath = new DOMXPath("//namespace::node()[name(.)='pre']");
        List result = xpath.selectNodes(doc);
        assertEquals(1, result.size());
        Node node = (Node) result.get(0);
        assertEquals("http://www.example.org", node.getNodeValue());
        
    }    
    
    // Jaxen-54
    public void testUpdateDOMNodesReturnedBySelectNodes() 
      throws JaxenException {
        
        Element root = doc.createElementNS("http://www.example.org/", "root");
        doc.appendChild(root);
        root.appendChild(doc.createComment("data"));
        
        DOMXPath xpath = new DOMXPath( "//comment()" );
        
        List results = xpath.selectNodes(doc);
        Node backroot = (Node) results.get(0);
        backroot.setNodeValue("test");
        assertEquals("test", backroot.getNodeValue());
        
    }

    public void testSelection() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
        XPath xpath = new DOMXPath( "/foo/bar/baz" );

        DocumentBuilder builder = factory.newDocumentBuilder();
    
        Document document = builder.parse( BASIC_XML );

        List results = xpath.selectNodes( document );

        assertEquals( 3,
                      results.size() );

        Iterator iter = results.iterator();

        assertEquals( "baz",
                      ((Element)iter.next()).getLocalName() );

        assertEquals( "baz",
                      ((Element)iter.next()).getLocalName() );

        assertEquals( "baz",
                      ((Element)iter.next()).getLocalName() );

        assertTrue( ! iter.hasNext() );

    }
    
    // Jaxen-22
    public void testPrecedingAxisWithPositionalPredicate() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
        
        XPath xpath = new DOMXPath( "//c/preceding::*[1][name()='d']" );
        DocumentBuilder builder = factory.newDocumentBuilder();
    
        Document document = builder.parse( "xml/web2.xml" );
        List result = xpath.selectNodes(document);
        assertEquals(1, result.size());
        
    }
    
     
    // Jaxen-22
    public void testJaxen22() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
        
        XPath xpath = new DOMXPath( "name(//c/preceding::*[1])" );
        DocumentBuilder builder = factory.newDocumentBuilder();
    
        doc = builder.parse("xml/web2.xml");
        Object result = xpath.evaluate(doc);
        assertEquals("d", result);
    }
    

    public void testJaxen207() 
      throws JaxenException {
        XPath xpath = new DOMXPath( "contains($FinResp, \"NS_Payables_Associate\") or"
                + "contains($FinResp, \"NS_Payables_Manager\") or"
                + "contains($FinResp, \"NS_Payment_Processing\") or"
                + "contains($FinResp, \"NS_Vendor_Maintenance\") or"
                + "contains($FinResp, \"NS_IB_Receivables_Manager\") or"
                + "contains($FinResp, \"NS_IB_Receivables_User\") or"
                + "contains($FinResp, \"NS_Receivables_Manager\") or"
                + "contains($FinResp, \"NS_Receivables_User\") or"
                + "contains($FinResp, \"NS_Cash_Management_User\") or"
                + "contains($FinResp, \"NS_Cost_Management\") or"
                + "contains($FinResp, \"NS_Fixed_Assets_Manager\") or"
                + "contains($FinResp, \"NS_Fixed_Asset_User\") or"
                + "contains($FinResp, \"NS_General_Ledger_Inquiry\") or"
                + "contains($FinResp, \"NS_General_Ledger_User\") or"
                + "contains($FinResp, \"NS_General_Ledger_Supervisor\") or"
                + "contains($FinResp, \"NS_IB_General_Ledger_User\") or"
                + "contains($FinResp, \"NS_IB_Oracle_Web_ADI\") or"
                + "contains($FinResp, \"NS_Oracle_Web_ADI\") or"
         + "contains($FinResp, \"NS_CRM_Resource_Manager\") or"
                + "contains($FinResp, \"NS_Distributor_Manager\") or"
                + "contains($FinResp, \"NS_OIC_User\") or"
                + "contains($FinResp, \" NS_Operations_Buyer\") or"
                + "contains($FinResp, \"NS_Purchasing_Buyer\") or"
                + "contains($FinResp, \"NS_Vendor_Maintenance\") or "
              + "contains($FinResp, \"SL_Payables_Manager\") or"
                + "contains($FinResp, \"SL_Payables_Super_User\") or"
                + "contains($FinResp, \"SL_Payment_Processing\") or"
                + "contains($FinResp, \"SL_Vendor_Maintenance\") or"
                + "contains($InvResp, \"SL_Inventory_Super_User\") or"
                         + "contains($FinResp, \"\") or"
                + "contains($FinResp, \"SL_Receivables_Supervisor\") or"
                + "contains($FinResp, \"SL_Receivables_User\") or"
                + "contains($FinResp, \"NS_Cost_Management_Inquiry\") or"
                + "contains($FinResp, \"SL_Fixed_Asset_User\") or"
                + "contains($FinResp, \"SL_Fixed_Assets_Manager\") or"
                + "contains($FinResp, \"SL_General_Ledger_Inquiry\") or"
                + "contains($FinResp, \"SL_General_Ledger_Supervisor\") or"
                + "contains($FinResp, \"SL_General_Ledger_User\") or"
                + "contains($FinResp, \"SL_Oracle_Web_ADI\") or"
                + "contains($FinResp, \"SL_Buyer\") or"
                + "contains($FinResp, \"SL_Purchasing_Inquiry\") or"
                + "contains($FinResp, \"SL_Payables_Manager\") or"
                + "contains($FinResp, \"SL_Payables_Super_User\") or"
                + "contains($FinResp, \"SL_Payment_Processing\") or"
                + "contains($FinResp, \"SL_Vendor_Maintenance\") or"
                + "contains($InvResp, \"SL_Inventory_Super_User\") or"
                + "contains($FinResp, \"\") or"
                + "contains($FinResp, \"SL_Receivables_Supervisor\") or"
                + "contains($FinResp, \"SL_Receivables_User\") or"
                + "contains($FinResp, \"NS_Cost_Management_Inquiry\") or"
                + "contains($FinResp, \"SL_Fixed_Asset_User\") or"
                + "contains($FinResp, \"SL_Fixed_Assets_Manager\") or"
                + "contains($FinResp, \"SL_General_Ledger_Inquiry\") or"
                + "contains($FinResp, \"SL_General_Ledger_Supervisor\") or"
                + "contains($FinResp, \"SL_General_Ledger_User\") or"
                + "contains($FinResp, \"SL_Oracle_Web_ADI\") or"
                + "contains($FinResp, \"SL_Buyer\") or"
                + "contains($FinResp, \"SL_Purchasing_Inquiry\")");
    }


    public void testImplictCastFromTextInARelationalExpression() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
        XPath implicitCast = new DOMXPath("//lat[(text() >= 37)]");
        XPath explicitCast = new DOMXPath("//lat[(number(text()) >= 37)]");
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream in = new ByteArrayInputStream("<geo><lat>39</lat></geo>".getBytes("UTF-8"));
        Document document = builder.parse(in);
        List result = explicitCast.selectNodes(document);
        assertEquals(1, result.size());
        result = implicitCast.selectNodes(document);
        assertEquals(1, result.size());
    }
    
     
    public void testImplictCastFromCommentInARelationalExpression() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
          XPath implicitCast = new DOMXPath("//lat[(comment() >= 37)]");
          XPath explicitCast = new DOMXPath("//lat[(number(comment()) >= 37)]");
          DocumentBuilder builder = factory.newDocumentBuilder();
          ByteArrayInputStream in = new ByteArrayInputStream("<geo><lat><!--39--></lat></geo>".getBytes("UTF-8"));
          Document document = builder.parse(in);
          List result = explicitCast.selectNodes(document);
          assertEquals(1, result.size());
          result = implicitCast.selectNodes(document);
          assertEquals(1, result.size());
    }
  

    public void testImplictCastFromProcessingInstructionInARelationalExpression() 
      throws JaxenException, ParserConfigurationException, SAXException, IOException {
        XPath implicitCast = new DOMXPath("//lat[(processing-instruction() >= 37)]");
        XPath explicitCast = new DOMXPath("//lat[(number(processing-instruction()) >= 37)]");
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream in = new ByteArrayInputStream("<geo><lat><?test 39?></lat></geo>".getBytes("UTF-8"));
        Document document = builder.parse(in);
        List result = explicitCast.selectNodes(document);
        assertEquals(1, result.size());
        result = implicitCast.selectNodes(document);
        assertEquals(1, result.size());
    }
    
    public void testPrecedingAxisInDocumentOrder() 
      throws JaxenException {
        
        XPath xpath = new DOMXPath( "preceding::*" );
    
        Element root = doc.createElement("root");
        doc.appendChild(root);
        
        Element a = doc.createElement("a");
        root.appendChild(a);
        Element b = doc.createElement("b");
        root.appendChild(b);
        Element c = doc.createElement("c");
        a.appendChild(c);
        
        List result = xpath.selectNodes(b);
        assertEquals(2, result.size());
        assertEquals(a, result.get(0));
        assertEquals(c, result.get(1));
    }
    
     
}
