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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jaxen.JaxenException;
import org.jaxen.pattern.Pattern;
import org.jaxen.pattern.PatternParser;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

/** Tests the use of priority in the Pattern implementations.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class PriorityTest extends TestCase
{
    public PriorityTest(String name)
    {
        super( name );
    }

    public static void main(String[] args) 
    {
        TestRunner.run( suite() );
    }
    
    public static Test suite() 
    {
        return new TestSuite( PriorityTest.class );
    }
    
    public void setUp()
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "" );
    }

    public void testDocumentNode() throws Exception
    {
        testPriority( "/", -0.5, Pattern.DOCUMENT_NODE );
    }
    
    public void testNameNode() throws Exception
    {
        testPriority( "foo", 0, Pattern.ELEMENT_NODE );
    }
    
    public void testQNameNode() throws Exception
    {
        testPriority( "foo:bar", 0, Pattern.ELEMENT_NODE );
    }
    
    public void testFilter() throws Exception
    {
        testPriority( "foo[@id='123']", 0.5, Pattern.ELEMENT_NODE );
    }
    
    public void testURI() throws Exception
    {
        testPriority( "foo:*", -0.25, Pattern.ELEMENT_NODE );
    }

    public void testNodeType() throws Exception
    {
        testPriority( "text()", -0.5, Pattern.TEXT_NODE );
    }
    
    public void testAttribute() throws Exception
    {
        testPriority( "@*", -0.5, Pattern.ATTRIBUTE_NODE );
    }
    
    public void testAnyNode() throws Exception
    {
        testPriority( "*", -0.5, Pattern.ELEMENT_NODE );
    }
    
    protected void testPriority(String expr, double priority, short nodeType) 
     throws JaxenException, SAXPathException  
    {
        
        Pattern pattern = PatternParser.parse( expr );
        double d = pattern.getPriority();
        short nt = pattern.getMatchType();
        
        assertEquals( "expr: " + expr,
                      new Double( priority ),
                      new Double( d ) );

        assertEquals( "nodeType: " + expr,
                      nodeType, 
                      nt );
    }
}
