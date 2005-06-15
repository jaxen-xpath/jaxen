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

import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.function.StringFunction;
import org.jaxen.saxpath.helpers.XPathReaderFactory;
import org.jaxen.dom4j.DocumentNavigator;
import org.jaxen.pattern.Pattern;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XPathTestBase extends TestCase
{
    protected static String VAR_URI   = "http://jaxen.org/test-harness/var";
    protected static String TESTS_XML = "xml/test/tests.xml";

    protected static boolean verbose = true;

    private DocumentBuilder builder;
    private ContextSupport  contextSupport;

    private Stack executionContext = new Stack();

    public XPathTestBase(String name)
    {
        super( name );
    }

    public void setUp() throws ParserConfigurationException
    {
        this.contextSupport = null;
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "" );
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
        
    }

    public void log(String text)
    {
        log( verbose,
             text );
    }

    public void log(boolean actualVerbose,
                    String text)
    {
        if ( ! actualVerbose )
        {
            return;
        }
            
        System.out.println( text );
    }

    public void testXPaths() throws Exception
    {
        log( "Loading test file: " + TESTS_XML );
        
        File f = new File( TESTS_XML);
        Document doc = builder.parse( f );

        NodeList documents = doc.getElementsByTagName("document");
        for (int i = 0; i < documents.getLength(); i++)
        {
            Element eachDocElem = (Element) documents.item(i);
            testDocument( eachDocElem );
        }
    }

    protected void testDocument(Element docElem) throws Exception
    {
        String url = docElem.getAttribute( "url" );

        Object testDoc = getDocument( url );

        log( "-----------------------------" );
        log( "Document [" + url + "]" );

        this.executionContext.push( url );

        NodeList contexts = docElem.getElementsByTagName( "context" );

        for (int i = 0; i < contexts.getLength(); i++)
        {
            Element context = (Element) contexts.item(i);

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

        String xpathStr = contextElem.getAttribute( "select" );

        log( "Initial Context :: " + xpathStr );

        this.executionContext.push( xpathStr );

        BaseXPath xpath = new BaseXPath( xpathStr );

        List list = xpath.selectNodes( getContext( testDoc ) );

        Iterator iter = list.iterator();
        Object   contextNode = null;

        while ( iter.hasNext() )
        {
            contextNode = iter.next();
            runTests( contextElem, contextNode );
        }

        this.executionContext.pop();
    }

    protected void runTests(Element contextElem,
                            Object context) throws Exception
    {
        NodeList countTests = contextElem.getElementsByTagName( "test" );

        for (int i = 0; i < countTests.getLength(); i++)
        {
            Element test = (Element) countTests.item(i);
            runTest( context, test );
        }

        NodeList valueOfs = contextElem.getChildNodes();

        for (int i = 0; i < valueOfs.getLength(); i++)
        { 
            Node node = valueOfs.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element valueOf = (Element) valueOfs.item(i);
                if (valueOf.getNodeName().equals("valueOf")) {
                    testValueOf( context, valueOf );
                }
            }
        }
    }

    protected void runTest(Object context,
                           Element test) throws Exception
    {
        String xpathStr = test.getAttribute( "select" );
        String debugStr  = test.getAttribute( "debug" );
        boolean debug = verbose;

        if ( ! "".equals(debugStr) )
        {
            if ( "true".equals( debugStr ) || "on".equals( debugStr ) )
            {
                debug = true;
            }
        }

        log( debug, "  Select :: " + xpathStr );

        this.executionContext.push( xpathStr );

        String count = test.getAttribute( "count" );
        String exception = test.getAttribute( "exception" );

        try 
        {
            BaseXPath xpath = new BaseXPath( xpathStr );
            List results = xpath.selectNodes( getContext( context ) );
            if ( ! "".equals(count) )
            {
                int expectedSize = Integer.parseInt( count );
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

                if (exception !=null && (exception.equals("on") || exception.equals("true"))) {
                    fail("An exception was expected.");
                }

            }
            
            // XXX Now test any valueOf children of the test element
            NodeList valueOfs = test.getElementsByTagName("valueOf");
            for (int i = 0; i < valueOfs.getLength(); i++) {
                Element valueOf = (Element) valueOfs.item(i);
                testValueOf( results.get(0), valueOf);
            }
            
        }
        catch (UnsupportedAxisException e)
        {
            log ( debug,
                  "      ## SKIPPED -- Unsupported Axis" );
        }
        catch (JaxenException e) {
            // If an exception attribute was switched on, this is the desired behavior.
            if (exception.equals("on") || exception.equals("true")) {
                log (debug, "    Caught expected exception "+e.getMessage());
            } 
            else throw e;
        }

        this.executionContext.pop();
    }

    protected void testValueOf(Object context,
                               Element valueOf) throws Exception
    {
        String xpathStr = valueOf.getAttribute( "select" );
        String debugStr = valueOf.getAttribute( "debug" );
        boolean debug = verbose;

        if ( ! "".equals(debugStr) )
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
            Object node = xpath.evaluate( getContext( context ) );
            
            String expected = getFullText(valueOf);
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

    private void setupNamespaceContext(Element contextElem)
    {
        SimpleNamespaceContext nsContext = new SimpleNamespaceContext();

        NamedNodeMap attributes = contextElem.getAttributes();

        for (int i = 0; i < attributes.getLength(); i++ )
        {
            Attr attr = (Attr) attributes.item(i);
            if ("http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI())) {

                String prefix = attr.getLocalName();
                String uri    = attr.getNodeValue();
    
                nsContext.addNamespace( prefix, uri );
    
                System.out.println( " ---> " + prefix + " == " + uri );
            }
        }

        getContextSupport().setNamespaceContext( nsContext );
    }

    private void setupVariableContext(Element contextElem)
    {
        SimpleVariableContext varContext = new SimpleVariableContext();

        NamedNodeMap varIter = contextElem.getAttributes();
        
        for (int i = 0; i < varIter.getLength(); i++ )
        {
            Attr eachVar = (Attr) varIter.item(i);

            if (  VAR_URI.equals(eachVar.getNamespaceURI()) )
            {
                String varName  = eachVar.getLocalName();
                String varValue = eachVar.getNodeValue();

                varContext.setVariableValue( null,
                                             varName,
                                             varValue );
            }
        }

        getContextSupport().setVariableContext( varContext );
    }
    
    private static String getFullText(Node node) {
        StringBuffer result = new StringBuffer(12);
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
          Node child = children.item(i);
          int type = child.getNodeType();
          if (type == Node.TEXT_NODE) {
            result.append(child.getNodeValue());
          }
          else if (type == Node.CDATA_SECTION_NODE) {
            result.append(child.getNodeValue());
          }
          else if (type == Node.ELEMENT_NODE) {
            result.append(getFullText(child));
          }
          else if (type == Node.ENTITY_REFERENCE_NODE) {
            result.append(getFullText(child));
          }
        }
        return result.toString();
    }



    public void testGetNodeType() throws FunctionCallException, UnsupportedAxisException
    {
        Navigator nav = getNavigator();
        Object document = nav.getDocument("xml/testNamespaces.xml");
        int count = 0;
        Iterator descendantOrSelfAxisIterator = nav.getDescendantOrSelfAxisIterator(document);
        while (descendantOrSelfAxisIterator.hasNext()) {
            Object node = descendantOrSelfAxisIterator.next();
            Iterator namespaceAxisIterator = nav.getNamespaceAxisIterator(node);
            while (namespaceAxisIterator.hasNext()) {
                count++;
                assertEquals("Node type mismatch", Pattern.NAMESPACE_NODE, nav.getNodeType(namespaceAxisIterator.next()));
            }
        }
        assertEquals(25, count);
    }

}
