/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.jaxen.expr.Expr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.TestCase;

/**
 * <p>
 *  Test for function context.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b12
 *
 */
public class DefaultXPathExprTest extends TestCase
{

    
    // http://jira.codehaus.org/browse/JAXEN-40
    public void testJAXEN40() throws JaxenException, ParserConfigurationException {
        
        DOMXPath xpath = new DOMXPath("root/child1/grandchild1 | root/child2/grandchild2");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("root");
        Element child1 = doc.createElement("child1");
        Element child2 = doc.createElement("child2");
        Element grandchild1 = doc.createElement("grandchild1");
        Element grandchild2 = doc.createElement("grandchild2");
        root.appendChild(child1);
        root.appendChild(child2);
        child1.appendChild(grandchild1);
        child2.appendChild(grandchild2);
        
        doc.appendChild(root);
        
        List results = xpath.selectNodes(doc);
        assertEquals(2, results.size());
        assertTrue(results.indexOf(grandchild1) >= 0);
        assertTrue(results.indexOf(grandchild2) >= 0);
        
    }

    
    // http://jira.codehaus.org/browse/JAXEN-160
    public void testJAXEN160GetText()
    throws JaxenException, ParserConfigurationException {
      
      DOMXPath xpath = new DOMXPath("$var1/foo");
      Expr expr = xpath.getRootExpr();
      assertEquals("$var1/child::foo", expr.getText());
      
  }

    public void testJAXEN160ToString()
    throws JaxenException, ParserConfigurationException {
      
      DOMXPath xpath = new DOMXPath("$var1/foo");
      Expr expr = xpath.getRootExpr();
      assertEquals(
        "[(DefaultPathExpr): [(DefaultVariableReferenceExpr): var1], [(DefaultRelativeLocationPath): [(DefaultNameStep): foo]]]", 
        expr.toString()
      );
    }

}
