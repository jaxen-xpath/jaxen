/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2005, 2006 Elliotte Rusty Harold.
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.jaxen.*;
import org.jaxen.dom.DOMXPath;
import org.jaxen.function.NumberFunction;
import org.jaxen.saxpath.SAXPathException;
import org.w3c.dom.Document;

/**
 * @author Elliotte Rusty Harold
 *
 */
public class ExtensionFunctionTest extends TestCase {

    private Document doc;
    
    public void setUp() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
    }


    public ExtensionFunctionTest(String name) {
        super(name);
    }
    
    class MinFunction implements Function {

        public Object call(Context context, List args) {
            
            if (args.isEmpty()) return Double.valueOf(Double.NaN);
            
            Navigator navigator = context.getNavigator();
            double min = Double.MAX_VALUE;
            Iterator iterator = args.iterator();
            while (iterator.hasNext()) {
                double next = NumberFunction.evaluate(iterator.next(), navigator).doubleValue();
                min = Math.min(min, next);
            }
            return Double.valueOf(min);
        }
        
    }
    
    
    public void testRegisterExtensionFunction() throws JaxenException {
        
        SimpleFunctionContext fc = new XPathFunctionContext(false);
        fc.registerFunction("http://exslt.org/math", "min", new MinFunction());
        
        SimpleNamespaceContext nc = new SimpleNamespaceContext();
        nc.addNamespace("math", "http://exslt.org/math");
        
        BaseXPath xpath = new DOMXPath("math:min(//x)");
        
        xpath.setFunctionContext(fc);
        xpath.setNamespaceContext(nc);
        
        org.w3c.dom.Element a = doc.createElementNS("", "a");
        org.w3c.dom.Element b = doc.createElementNS("", "b");
        doc.appendChild(a);
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
        
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(Double.valueOf(2), result);
       
        
    }
    
    
    public void testJaxen47() throws SAXPathException {
       org.jaxen.dom.DocumentNavigator.getInstance().parseXPath("a:b()");
    }
    
}
