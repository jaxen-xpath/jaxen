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


package org.jaxen.saxpath.base;

import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.jaxen.saxpath.Axis;
import org.jaxen.saxpath.Operator;
import org.jaxen.saxpath.XPathSyntaxException;
import org.jaxen.saxpath.conformance.ConformanceXPathHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XPathReaderTest extends TestCase
{
    private ConformanceXPathHandler expected;
    private ConformanceXPathHandler actual;

    private XPathReader reader;
    private String text;

    private String[] paths = {
        "/foo/bar[@a='1' and @b='2']",
        "/foo/bar[@a='1' and @b!='2']",
        "//attribute::*[.!='crunchy']",
        "'//*[contains(string(text()),\"yada yada\")]'",
    };

    private String[][] bogusPaths = {
        new String[]{"chyld::foo", "Expected valid axis name instead of [chyld]"},
        new String[]{"foo/tacos()", "Expected node-type"},
        new String[]{"foo/tacos()", "Expected node-type"},
        new String[]{"*:foo", "Unexpected ':'"},
        new String[]{"/foo/bar[baz", "Expected: ]"},
        new String[]{"/cracker/cheese[(mold > 1) and (sense/taste", "Expected: )"},
        new String[]{"//", "Location path cannot end with //"}
    };

    public XPathReaderTest( String name )
    {
        super( name );
    }

    public void setUp()
    {
        setReader( new XPathReader() );
        setText( null );

        this.actual = new ConformanceXPathHandler();
        this.expected = new ConformanceXPathHandler();

        getReader().setXPathHandler( actual() );
    }

    public void tearDown()
    {
        setReader( null );
        setText( null );
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------


    public void testPaths()
    {
        XPathReader reader = new XPathReader();

        System.out.println( "Valid Expressions" );

        for( int i = 0; i < paths.length; ++i )
        {
            System.out.println( "----------------------------------------" );
            System.out.println( paths[i] );
            System.out.println( "----------------------------------------" );
            try
            {
                reader.parse( paths[i] );
            }
            catch( org.jaxen.saxpath.SAXPathException e )
            {
                e.printStackTrace();
                fail( e.getMessage() );
            }
            catch( Exception e )
            {
                fail( e.getMessage() );
            }
        }
    }

    public void testBogusPaths()
    {
        XPathReader reader = new XPathReader();

        System.out.println( "Bogus Expressions" );

        for( int i = 0; i < bogusPaths.length; ++i )
        {
            final String[] bogusPath = bogusPaths[i];
            System.out.println( "----------------------------------------" );
            System.out.println( bogusPath[0] );
            System.out.println( "----------------------------------------" );

            try
            {
                reader.parse( bogusPath[0] );

                fail( "Should have thrown XPathSyntaxException for " + bogusPath[0]);
            }
            catch( XPathSyntaxException e )
            {
                assertEquals( bogusPath[1], e.getMessage() );
            }
            catch( org.jaxen.saxpath.SAXPathException e )
            {
                fail( e.getMessage() );
            }
            catch( Exception e )
            {
                fail( e.getMessage() );
            }
        }
    }

    public void testChildrenOfNumber()
    {
        XPathReader reader = new XPathReader();
        try
        {
            reader.parse( "1/child::test" );
            fail( "Should have thrown XPathSyntaxException for 1/child::test");
        }
        catch( XPathSyntaxException e )
        {
            assertEquals( "Node-set expected", e.getMessage() );
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
        catch( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    public void testChildIsNumber()
    {
        XPathReader reader = new XPathReader();
        try
        {
            reader.parse( "jane/3" );
            fail( "Should have thrown XPathSyntaxException for jane/3");
        }
        catch( XPathSyntaxException e )
        {
            assertEquals( "Expected one of '.', '..', '@', '*', <QName>", e.getMessage() );
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
        catch( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    public void testNumberOrNumber()
    {

        try
        {
            XPath xpath = new DOMXPath( "4 | 5" );

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
        
            Document doc = builder.parse( "xml/basic.xml" );

            xpath.selectNodes( doc );
            fail( "Should have thrown XPathSyntaxException for 4 | 5");
        }
        catch( JaxenException e )
        {
            assertEquals( "Unions are only allowed over node-sets", e.getMessage() );
        }
        catch( Exception e )
        {
            fail( e.getMessage() );
        }
    }

    
    
    public void testValidAxis()
    {
        XPathReader reader = new XPathReader();

        try
        {
            reader.parse( "child::foo" );
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testInvalidAxis()
    {
        XPathReader reader = new XPathReader();

        try
        {
            reader.parse( "chyld::foo" );
            fail( "Should have thrown XPathSyntaxException" );
        }
        catch( XPathSyntaxException e )
        {
            // expected and correct
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }

    }

    public void testSimpleNameStep()
    {
        try
        {
            setText( "foo" );

            getReader().setUpParse( getText() );

            getReader().step( );

            expected().startNameStep( Axis.CHILD,
                                      "",
                                      "foo" );
            expected().endNameStep();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }

    }

    public void testNameStepWithAxisAndPrefix()
    {
        try
        {
            setText( "parent::foo:bar" );

            getReader().setUpParse( getText() );

            getReader().step( );

            expected().startNameStep( Axis.PARENT,
                                      "foo",
                                      "bar" );
            expected().endNameStep();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testNodeStepWithAxis()
    {
        try
        {
            setText( "parent::node()" );

            getReader().setUpParse( getText() );

            getReader().step();

            expected().startAllNodeStep( Axis.PARENT );

            expected().endAllNodeStep();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testProcessingInstructionStepWithName()
    {
        try
        {
            setText( "parent::processing-instruction('cheese')" );

            getReader().setUpParse( getText() );

            getReader().step( );

            expected().startProcessingInstructionNodeStep( Axis.PARENT,
                                                           "cheese" );

            expected().endProcessingInstructionNodeStep();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testProcessingInstructionStepNoName()
    {
        try
        {
            setText( "parent::processing-instruction()" );

            getReader().setUpParse( getText() );

            getReader().step( );

            expected().startProcessingInstructionNodeStep( Axis.PARENT,
                                                           "" );

            expected().endProcessingInstructionNodeStep();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testAllNodeStep()
    {
        try
        {
            setText( "parent::node()" );

            getReader().setUpParse( getText() );

            getReader().step( );

            expected().startAllNodeStep( Axis.PARENT );

            expected().endAllNodeStep();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testTextNodeStep()
    {
        try
        {
            setText( "parent::text()" );

            getReader().setUpParse( getText() );

            getReader().step( );

            expected().startTextNodeStep( Axis.PARENT );

            expected().endTextNodeStep();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testCommentNodeStep()
    {
        try
        {
            setText( "parent::comment()" );

            getReader().setUpParse( getText() );

            getReader().step( );

            expected().startCommentNodeStep( Axis.PARENT );

            expected().endCommentNodeStep();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testRelativeLocationPath()
    {
        try
        {
            setText( "foo/bar/baz" );

            getReader().setUpParse( getText() );

            getReader().locationPath( false );

            expected().startRelativeLocationPath();

            expected().startNameStep( Axis.CHILD,
                                      "",
                                      "foo" );
            expected().endNameStep();

            expected().startNameStep( Axis.CHILD,
                                      "",
                                      "bar" );
            expected().endNameStep();

            expected().startNameStep( Axis.CHILD,
                                      "",
                                      "baz" );
            expected().endNameStep();

            expected().endRelativeLocationPath();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testAbsoluteLocationPath()
    {
        try
        {
            setText( "/foo/bar/baz" );

            getReader().setUpParse( getText() );

            getReader().locationPath( true );

            expected().startAbsoluteLocationPath();

            expected().startNameStep( Axis.CHILD,
                                      "",
                                      "foo" );
            expected().endNameStep();

            expected().startNameStep( Axis.CHILD,
                                      "",
                                      "bar" );
            expected().endNameStep();

            expected().startNameStep( Axis.CHILD,
                                      "",
                                      "baz" );
            expected().endNameStep();

            expected().endAbsoluteLocationPath();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    public void testNumberPredicate()
    {
        try
        {
            setText( "[1]" );

            getReader().setUpParse( getText() );

            getReader().predicate();

            expected().startPredicate();

            expected().startOrExpr();
            expected().startAndExpr();
            expected().startEqualityExpr();
            expected().startEqualityExpr();
            expected().startRelationalExpr();
            expected().startRelationalExpr();
            expected().startAdditiveExpr();
            expected().startAdditiveExpr();
            expected().startMultiplicativeExpr();
            expected().startMultiplicativeExpr();
            expected().startUnaryExpr();
            expected().startUnionExpr();
            expected().startPathExpr();
            expected().startFilterExpr();

            expected().number( 1 );

            expected().endFilterExpr();
            expected().endPathExpr();
            expected().endUnionExpr( false );
            expected().endUnaryExpr( Operator.NO_OP );
            expected().endMultiplicativeExpr( Operator.NO_OP );
            expected().endMultiplicativeExpr( Operator.NO_OP );
            expected().endAdditiveExpr( Operator.NO_OP );
            expected().endAdditiveExpr( Operator.NO_OP );
            expected().endRelationalExpr( Operator.NO_OP );
            expected().endRelationalExpr( Operator.NO_OP );
            expected().endEqualityExpr( Operator.NO_OP );
            expected().endEqualityExpr( Operator.NO_OP );
            expected().endAndExpr( false );
            expected().endOrExpr( false );

            expected().endPredicate();

            compare();
        }
        catch( org.jaxen.saxpath.SAXPathException e )
        {
            fail( e.getMessage() );
        }
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    private void setText( String text )
    {
        this.text = text;
    }

    private String getText()
    {
        return this.text;
    }

    private void setReader( XPathReader reader )
    {
        this.reader = reader;
    }

    private XPathReader getReader()
    {
        return this.reader;
    }

    private void compare()
    {
        assertEquals( expected(),
                      actual() );
    }

    private ConformanceXPathHandler expected()
    {
        return this.expected;
    }

    private ConformanceXPathHandler actual()
    {
        return this.actual;
    }
}
