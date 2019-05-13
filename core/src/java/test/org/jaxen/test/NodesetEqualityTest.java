/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2008 Andrew Sales
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

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>Tests comparison of nodesets using the = and != operators.</p>
 * 
 * <blockquote>If both objects to be compared are node-sets, then the comparison 
 * will be true if and only if there is a node in the first node-set and a node 
 * in the second node-set such that the result of performing the comparison 
 * on the string-values of the two nodes is true</blockquote>
 * 
 * @author Andrew Sales
 *
 * $Id$
 */
public class NodesetEqualityTest extends TestCase {
    private Document doc;

    public void setUp() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();

        /*
         <a><b>foo</b><b>bar</b><b>blort</b><c/><c>12.0</c><c>blort</c></a> 
         */
        
        Element a = doc.createElementNS( "", "a" );
        doc.appendChild( a );
        Element b1 = doc.createElementNS( "", "b" );
        b1.appendChild( doc.createTextNode( "foo" ) );
        Element b2 = doc.createElementNS( "", "b" );
        b2.appendChild( doc.createTextNode( "bar" ) );
        Element b3 = doc.createElementNS( "", "b" );
        b3.appendChild( doc.createTextNode( "blort" ) );

        a.appendChild( b1 );
        a.appendChild( b2 );
        a.appendChild( b3 );

        Element c1 = doc.createElementNS( "", "c" );
        Element c2 = doc.createElementNS( "", "c" );
        Element c3 = doc.createElementNS( "", "c" );

        c2.appendChild( doc.createTextNode( " 12.0 " ) );
        c3.appendChild( doc.createTextNode( "bar" ) );

        a.appendChild( c1 );
        a.appendChild( c2 );
        a.appendChild( c3 );

    }

    public void testEqualsTwoNodesets() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//b = //c" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue( result.booleanValue() );
    }

    public void testNotEqualsTwoNodesets() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//a != //b" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue( result.booleanValue() );
    }

    public void testEqualsStringNodeset() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//b = 'blort'" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue(result.booleanValue());
    }

    public void testNotEqualsStringNodeset() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//b != 'phooey'" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue(result.booleanValue());
    }

    public void testEqualsNumberNodeset() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//c = 12" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue(result.booleanValue());
    }

    public void testNotEqualsNumberNodeset() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//c != 256" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue(result.booleanValue());
    }

    public void testEqualsBooleanNodeset1() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//c = true()" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue(result.booleanValue());
    }

    public void testEqualsBooleanNodeset2() throws JaxenException
    {
        //an empty nodeset should be equal to false()
        XPath xpath = new DOMXPath( "//d = false()" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue(result.booleanValue());
    }


    public void testNotEqualsBooleanNodeset1() throws JaxenException
    {
        XPath xpath = new DOMXPath( "//c != false()" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue(result.booleanValue());
    }

    public void testNotEqualsBooleanNodeset2() throws JaxenException
    {
        //an empty nodeset should be not equal to true()  
        XPath xpath = new DOMXPath( "//d != true()" );
        Boolean result = (Boolean) xpath.evaluate( doc );
        assertTrue(result.booleanValue());
    }

}
