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

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.XPath;
import org.jaxen.dom4j.DocumentNavigator;
import org.jaxen.dom4j.Dom4jXPath;

public class DOM4JNavigatorTest extends XPathTestBase
{
    private SAXReader reader; 

    public DOM4JNavigatorTest(String name)
    {
        super( name );
        this.reader = new SAXReader();
        this.reader.setMergeAdjacentText( true );
    }
    
    public Navigator getNavigator()
    {
        return new DocumentNavigator();
    }

    public Object getDocument(String url) throws Exception
    {
        return reader.read( url );
    }
    
    /**
     * reported as JAXEN-104.
     * @throws FunctionCallException
     * @throws UnsupportedAxisException
     */
    public void testConcurrentModification() throws FunctionCallException, UnsupportedAxisException
    {
        Navigator nav = new DocumentNavigator();
        Object document = nav.getDocument("xml/testNamespaces.xml");
        Iterator descendantOrSelfAxisIterator = nav.getDescendantOrSelfAxisIterator(document);
        while (descendantOrSelfAxisIterator.hasNext()) {
            Object node = descendantOrSelfAxisIterator.next();
            Iterator namespaceAxisIterator = nav.getNamespaceAxisIterator(node);
            while (namespaceAxisIterator.hasNext()) {
                namespaceAxisIterator.next();
            }
        }
    }
    
    public void testNullPointerException() throws JaxenException {
        Document doc = DocumentHelper.createDocument();
        XPath xpath = new Dom4jXPath("/foo");
        xpath.selectSingleNode(doc);
    }    
    
    
}
