/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
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

import org.jaxen.function.StringFunction;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Attribute;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

public abstract class XPathTestBase extends TestCase
{
    protected static String    VAR_URI   = "http://jaxen.org/test-harness/var";
    protected static String    TESTS_XML = "xml/test/tests.xml";

    protected static boolean            verbose         = true;

    private          SAXReader          xmlReader       = new SAXReader();
    private          ContextSupport     contextSupport;

    private Stack executionContext = new Stack();

    public XPathTestBase(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.contextSupport = null;
    }

    public void tearDown()
    {

    }

    public void log(String text)
    {
        log( this.verbose,
             text );
    }

    public void log(boolean actualVerbose,
                    String text)
    {
        if ( ! actualVerbose )
        {
            return;
        }
            
        System.err.println( text );
    }

    public void testXPaths() throws Exception
    {
        log( "Loading test file: " + TESTS_XML );
        
        Document doc = xmlReader.read( TESTS_XML );

        Element root = doc.getRootElement();

        Iterator iter = root.elementIterator( "document" );

        Element eachDocElem = null;

        while ( iter.hasNext() )
        {
            eachDocElem = (Element) iter.next();

            testDocument( eachDocElem );
        }
    }

    protected void testDocument(Element docElem) throws Exception
    {
        String url = docElem.attributeValue( "url" );

        Object testDoc = getDocument( url );

        log( "-----------------------------" );
        log( "Document [" + url + "]" );

        this.executionContext.push( url );

        Iterator iter    = docElem.elementIterator( "context" );
        Element  context = null;

        while ( iter.hasNext() )
        {
            context = (Element) iter.next();

            testContext( testDoc,
                         context );
        }

        this.executionContext.pop();

        log( "-----------------------------" );
    }

    protected void testContext(Object testDoc,
                               Element contextElem) throws Exception
    {
        setupNamespaceContext( contextElem );
        setupVariableContext( contextElem );

        String xpathStr = contextElem.attributeValue( "select" );

        log( "Initial Context :: " + xpathStr );

        this.executionContext.push( xpathStr );

        BaseXPath xpath = new BaseXPath( xpathStr );

        List list = xpath.selectNodes( getContext( testDoc ) );

        Iterator iter = list.iterator();
        Object   contextNode = null;

        while ( iter.hasNext() )
        {
            contextNode = iter.next();

            runTests( testDoc,
                      contextElem,
                      contextNode );
        }

        this.executionContext.pop();
    }

    protected void runTests(Object testDoc,
                            Element contextElem,
                            Object context) throws Exception
    {
        Iterator iter = contextElem.elementIterator( "test" );

        Element test = null;

        while ( iter.hasNext() )
        {
            test = (Element) iter.next();

            runTest( testDoc,
                     context,
                     test );
        }

        iter = contextElem.elementIterator( "valueOf" );

        Element valueOf = null;

        while ( iter.hasNext() )
        {
            valueOf = (Element) iter.next();

            testValueOf( testDoc,
                         context,
                         valueOf );
        }
    }

    protected void runTest(Object testDoc,
                           Object context,
                           Element test) throws Exception
    {
        String xpathStr = test.attributeValue( "select" );

        String debugStr  = test.attributeValue( "debug" );

        boolean debug = this.verbose;

        if ( debugStr != null )
        {
            if ( "true".equals( debugStr )
                 ||
                 "on".equals( debugStr ) )
            {
                debug = true;
            }
        }

        log( debug,
             "  Select :: " + xpathStr );

        this.executionContext.push( xpathStr );

        String count = test.attributeValue( "count" );

        BaseXPath xpath = new BaseXPath( xpathStr );

        if ( count != null )
        {
            int expectedSize = Integer.parseInt( count );

            try
            {
                List results = xpath.selectNodes( getContext( context ) );
                
                log ( debug,
                      "    Expected Size :: " + expectedSize );
                log ( debug,
                      "    Result Size   :: " + results.size() );
                
                if ( expectedSize != results.size() )
                {
                    log ( debug,
                          "      ## FAILED" );
                    log ( debug,
                          "      ## xpath: " + xpath + " = " + xpath.debug() );
                    
                    Iterator resultIter = results.iterator();
                    
                    while ( resultIter.hasNext() )
                    {
                        log ( debug,
                              "      --> " + resultIter.next() );
                    }
                }
                assertEquals( this.executionContext.toString(),
                              expectedSize,
                              results.size() );
            }
            catch (UnsupportedAxisException e)
            {
                log ( debug,
                      "      ## SKIPPED -- Unsupported Axis" );
            }
        }

        Iterator valueOfIter = test.elementIterator( "valueOf" );

        while ( valueOfIter.hasNext() )
        {
        
            //Element valueOf = test.element( "valueOf" );
            Element valueOf = (Element) valueOfIter.next();

            if ( valueOf != null )
            {
                debugStr = valueOf.attributeValue( "debug" );
                
                if ( debugStr != null )
                {
                    if ( "true".equals( debugStr )
                         ||
                         "on".equals( debugStr ) )
                    {
                        debug = true;
                    }
                }
                
                try
                {
                    Object newContext = xpath.selectSingleNode( getContext( context ) );
                    
                    log ( debug,
                          "    New Context :: " + abbreviate( newContext ) );
                    
                    
                    String valueOfXPathStr = valueOf.attributeValue( "select" );
                    
                    log( debug,
                         "  Select :: " + valueOfXPathStr );
                    
                    this.executionContext.push( valueOfXPathStr );
                    
                    BaseXPath valueOfXPath = new BaseXPath( valueOfXPathStr );
                    
                    Object node = valueOfXPath.selectSingleNode( getContext( newContext ) );
                
                    String expected = valueOf.getText();
                    String result =   StringFunction.evaluate( node,
                                                               getNavigator() );
                    
                    log ( debug,
                          "    Expected :: " + expected );
                    
                    log ( debug,
                          "    Result   :: " + result );
                    
                    if ( ! expected.equals( result ) )
                    {
                        log ( debug,
                              "      ## FAILED" );
                    }
                    
                    assertEquals( this.executionContext.toString(),
                                  expected,
                                  result );

                    this.executionContext.pop();
                }
                catch (UnsupportedAxisException e)
                {
                    log ( debug,
                          "      ## SKIPPED -- Unsupported Axis" );
                    
                }
                
            }
        }
        
        this.executionContext.pop();
    }

    protected void testValueOf(Object testDoc,
                               Object context,
                               Element valueOf) throws Exception
    {
        String xpathStr = valueOf.attributeValue( "select" );

        String debugStr  = valueOf.attributeValue( "debug" );

        boolean debug = this.verbose;

        if ( debugStr != null )
        {
            if ( "true".equals( debugStr )
                 ||
                 "on".equals( debugStr ) )
            {
                debug = true;
            }
        }

        BaseXPath xpath = new BaseXPath( xpathStr );

        this.executionContext.push( xpathStr );

        try
        {
            Object node = xpath.selectSingleNode( getContext( context ) );
            
            String expected = valueOf.getText();
            String result = StringFunction.evaluate( node,
                                                     getNavigator() );
            
            log ( debug,
                  "  Select :: " + xpathStr );
            log ( debug,
                  "    Expected :: " + expected );
            log ( debug,
                  "    Result   :: " + result );
            
            if ( ! expected.equals( result ) )
            {
                log ( debug,
                      "      ## FAILED" );
                log ( debug,
                      "      ## xpath: " + xpath + " = " + xpath.debug() );
            }
            
            assertEquals( this.executionContext.toString(),
                          expected,
                          result );
        }
        catch (UnsupportedAxisException e)
        {
            log ( debug,
                  "      ## SKIPPED -- Unsupported Axis " );

        }

        this.executionContext.pop();
    }
                               
    protected Context getContext(Object contextNode)
    {
        Context context = new Context( getContextSupport() );

        List list = new ArrayList( 1 );

        list.add( contextNode );

        context.setNodeSet( list );

        return context;
    }

    public ContextSupport getContextSupport()
    {
        if ( this.contextSupport == null )
        {
            this.contextSupport = new ContextSupport( new SimpleNamespaceContext(),
                                                      XPathFunctionContext.getInstance(),
                                                      new SimpleVariableContext(),
                                                      getNavigator() );
        }

        return this.contextSupport;
    }

    public abstract Navigator getNavigator();

    public abstract Object getDocument(String url) throws Exception;

    private String abbreviate(Object obj)
    {
        if ( obj == null )
        {
            return "null";
        }

        String str = obj.toString();

        int nl = str.indexOf( "\n" );

        if ( nl >= 0
             &&
             nl < 80 )
        {
            return str.substring( 0, nl );
        }

        if ( str.length() < 80 )
        {
            return str;
        }

        return str.substring(0, 80) + "...";
    }

    private void setupNamespaceContext(Element contextElem)
    {
        SimpleNamespaceContext nsContext = new SimpleNamespaceContext();

        List namespaces = contextElem.additionalNamespaces();
        Iterator nsIter = namespaces.iterator();

        String prefix = null;;
        String uri    = null;

        Namespace eachNs = null;

        while ( nsIter.hasNext() )
        {
            eachNs = (Namespace) nsIter.next();

            prefix = eachNs.getPrefix();
            uri    = eachNs.getURI();

            nsContext.addNamespace( prefix, uri );

            System.err.println( " ---> " + prefix + " == " + uri );
        }

        getContextSupport().setNamespaceContext( nsContext );
    }

    private void setupVariableContext(Element contextElem)
    {
        SimpleVariableContext varContext = new SimpleVariableContext();

        Iterator  varIter = contextElem.attributeIterator();
        Attribute eachVar = null;

        String varName  = null;
        String varValue = null;

        while ( varIter.hasNext() )
        {
            eachVar = (Attribute) varIter.next();

            if ( eachVar.getNamespace().getURI().equals( VAR_URI ) )
            {
                varName  = eachVar.getName();
                varValue = eachVar.getValue();

                varContext.setVariableValue( null,
                                             varName,
                                             varValue );
            }
        }

        getContextSupport().setVariableContext( varContext );
    }
}
