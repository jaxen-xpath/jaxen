/* $Header$
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

import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.*;
import org.jaxen.dom.DOMXPath;
import org.jaxen.dom.NamespaceNode;
import org.w3c.dom.*;

import junit.framework.TestCase;

public class DOM3NamespaceTest extends TestCase {
    
    
    private NamespaceNode xmlNS;
    private NamespaceNode rootNS;
    private NamespaceNode attributeNS;
    

    public DOM3NamespaceTest(String name) {
        super(name);
    }
    
    protected void setUp() throws ParserConfigurationException, JaxenException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        org.w3c.dom.Element root = doc.createElementNS("http://www.root.com/", "root");
        doc.appendChild(root);
        Attr a = doc.createAttributeNS("http://www.example.org/", "pre:a");
        a.setNodeValue("value");
        root.setAttributeNode(a);
        
        XPath xpath = new DOMXPath("namespace::node()");
        List result = xpath.selectNodes(root);
        
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            NamespaceNode node = (NamespaceNode) iterator.next();
            if (node.getLocalName() == null || "".equals(node.getLocalName())) rootNS = node;
            else if (node.getLocalName().equals("xml")) xmlNS = node;
            else if (node.getLocalName().equals("pre")) attributeNS = node;
        }
        
    }     
    
    
    public void testGetTextContent() {
        assertEquals("http://www.w3.org/XML/1998/namespace", xmlNS.getTextContent());
    }
    
    public void testSetTextContent() {
        
        try {
            rootNS.setTextContent("http://www.a.com");
            fail("set text content");
        }
        catch (DOMException ex) {
            assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, ex.code);
        }
    }
    
    
    public void testGetFeature() {
        assertNull(attributeNS.getFeature("name", "value"));
    }
    
    
    // XXX need to distinguish these two cases
    public void testIsEqualNode() {
        assertFalse(rootNS.isEqualNode(xmlNS));
        assertTrue(rootNS.isEqualNode(rootNS));
    }
    
    public void testIsSameNode() {
        assertFalse(rootNS.isSameNode(xmlNS));
        assertTrue(rootNS.isSameNode(rootNS));
    }
    
}
