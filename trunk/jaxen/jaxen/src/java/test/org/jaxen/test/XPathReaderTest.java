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

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.base.XPathReader;
import org.jaxen.saxpath.XPathSyntaxException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XPathReaderTest extends TestCase
{
    private ConformanceXPathHandler actual;
    private Document doc;

    private XPathReader reader;

    private String[] paths = {
        "/foo/bar[@a='1' and @b='2']",
        "/foo/bar[@a='1' and @b!='2']",
        "$varname[@a='1']",
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
        new String[]{"//", "Location path cannot end with //"},
        new String[]{"foo/$variable/foo", "Expected one of '.', '..', '@', '*', <QName>"}
    };

    public XPathReaderTest( String name )
    {
        super( name );
    }

    public void setUp() throws ParserConfigurationException, SAXException, IOException
    {
        this.reader = new XPathReader();

        this.actual = new ConformanceXPathHandler();

        this.reader.setXPathHandler( this.actual );
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse( "xml/basic.xml" );

    }

    public void tearDown()
    {
        this.reader = null;
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------


    public void testPaths() throws SAXPathException
    {

        for( int i = 0; i < paths.length; ++i )
        {
            reader.parse( paths[i] );
        }
    }

    public void testBogusPaths() throws SAXPathException
    {

        for( int i = 0; i < bogusPaths.length; ++i )
        {
            final String[] bogusPath = bogusPaths[i];

            try
            {
                reader.parse( bogusPath[0] );
                fail( "Should have thrown XPathSyntaxException for " + bogusPath[0]);
            }
            catch( XPathSyntaxException e )
            {
                assertEquals( bogusPath[1], e.getMessage() );
            }
        }
    }

    public void testChildrenOfNumber() throws SAXPathException
    {
        try
        {
            reader.parse( "1/child::test" );
            fail( "Should have thrown XPathSyntaxException for 1/child::test");
        }
        catch( XPathSyntaxException e )
        {
            assertEquals( "Node-set expected", e.getMessage() );
        }
    }

    public void testChildIsNumber() throws SAXPathException
    {
        try
        {
            reader.parse( "jane/3" );
            fail( "Should have thrown XPathSyntaxException for jane/3");
        }
        catch( XPathSyntaxException e )
        {
            assertEquals( "Expected one of '.', '..', '@', '*', <QName>", e.getMessage() );
        }
        
    }

    public void testNumberOrNumber()
    {

        try
        {
            XPath xpath = new DOMXPath( "4 | 5" );
            xpath.selectNodes( doc );
            fail( "Should have thrown XPathSyntaxException for 4 | 5");
        }
        catch( JaxenException e )
        {
            assertEquals( "Unions are only allowed over node-sets", e.getMessage() );
        }
    }

    public void testStringOrNumber()
    {

        try
        {
            XPath xpath = new DOMXPath( "\"test\" | 5" );
            xpath.selectNodes( doc );
            fail( "Should have thrown XPathSyntaxException for \"test\" | 5");
        }
        catch( JaxenException e )
        {
            assertEquals( "Unions are only allowed over node-sets", e.getMessage() );
        }
    }    
    
    public void testStringOrString() 
    {

        try
        {
            XPath xpath = new DOMXPath( "\"test\" | \"festival\"" );
            xpath.selectNodes( doc );
            fail( "Should have thrown XPathSyntaxException for \"test\" | 5");
        }
        catch( JaxenException e )
        {
            assertEquals( "Unions are only allowed over node-sets", e.getMessage() );
        }
        
    }    
    
    public void testUnionofNodesAndNonNodes() 
    {

        try
        {
            XPath xpath = new DOMXPath( "count(//*) | //* " );
            xpath.selectNodes( doc );
            fail( "Should have thrown XPathSyntaxException for \"count(//*) | //* ");
        }
        catch( JaxenException e )
        {
            assertEquals( "Unions are only allowed over node-sets", e.getMessage() );
        }
    }    
    
    public void testValidAxis() throws SAXPathException
    {
        reader.parse( "child::foo" );
    }

    public void testInvalidAxis() throws SAXPathException
    {

        try
        {
            reader.parse( "chyld::foo" );
            fail( "Should have thrown XPathSyntaxException" );
        }
        catch( XPathSyntaxException ex )
        {
            assertNotNull(ex.getMessage());
        }

    }

/*    public void testSimpleNameStep() throws SAXPathException
    {
        this.text = "foo";
        this.reader.setUpParse( this.text );
        this.reader.step( );
        this.expected.startNameStep( Axis.CHILD,
                                  "",
                                  "foo" );
        this.expected.endNameStep();
        assertEquals( this.expected,
          this.actual );

    } 

    public void testNameStepWithAxisAndPrefix() throws SAXPathException
    {
        this.text = "parent::foo:bar";
        this.reader.setUpParse( this.text );
        this.reader.step( );
        this.expected.startNameStep( Axis.PARENT,
                                  "foo",
                                  "bar" );
        this.expected.endNameStep();
        assertEquals( this.expected,
          this.actual );

    }

    public void testNodeStepWithAxis() throws SAXPathException
    {

        this.text = "parent::node()";
        this.reader.setUpParse( this.text );
        this.reader.step();
        this.expected.startAllNodeStep( Axis.PARENT );
        this.expected.endAllNodeStep();
        assertEquals( this.expected,
          this.actual );

    }

    public void testProcessingInstructionStepWithName() throws SAXPathException
    {
        this.text = "parent::processing-instruction('cheese')";
        this.reader.setUpParse( this.text );
        this.reader.step( );
        this.expected.startProcessingInstructionNodeStep( Axis.PARENT,
                                                           "cheese" );
        this.expected.endProcessingInstructionNodeStep();
        assertEquals( this.expected,
          this.actual );
    }

    public void testProcessingInstructionStepNoName() throws SAXPathException
    {
        this.text = "parent::processing-instruction()";
        this.reader.setUpParse( this.text );
        this.reader.step( );
        this.expected.startProcessingInstructionNodeStep( Axis.PARENT,
                                                       "" );
        this.expected.endProcessingInstructionNodeStep();
        assertEquals( this.expected,
          this.actual );

    }

    public void testAllNodeStep() throws SAXPathException
    {

        this.text = "parent::node()";
        this.reader.setUpParse( this.text );
        this.reader.step( );
        this.expected.startAllNodeStep( Axis.PARENT );
        this.expected.endAllNodeStep();
        assertEquals( this.expected,
          this.actual );

    }

    public void testTextNodeStep() throws SAXPathException
    {

        this.text = "parent::text()";
        this.reader.setUpParse( this.text );
        this.reader.step( );
        this.expected.startTextNodeStep( Axis.PARENT );
        this.expected.endTextNodeStep();
        assertEquals( this.expected,
          this.actual );

    }

    public void testCommentNodeStep() throws SAXPathException
    {

        this.text = "parent::comment()";
        this.reader.setUpParse( this.text );
        this.reader.step( );
        this.expected.startCommentNodeStep( Axis.PARENT );
        this.expected.endCommentNodeStep();
        assertEquals( this.expected,
          this.actual );

    }*/

    public void testLocationPathStartsWithVariable() throws SAXPathException
    {

        reader.parse( "$variable/foo" );

    }

    public void testLocationPathStartsWithParentheses() throws SAXPathException
    {

        reader.parse( "(//x)/foo" );

    }

    /*public void testRelativeLocationPath() throws SAXPathException
    {

        this.text = "foo/bar/baz";
        this.reader.setUpParse( this.text );
        this.reader.locationPath( false );
        this.expected.startRelativeLocationPath();
        this.expected.startNameStep( Axis.CHILD,
                                  "",
                                  "foo" );
        this.expected.endNameStep();
        this.expected.startNameStep( Axis.CHILD,
                                  "",
                                  "bar" );
        this.expected.endNameStep();
        this.expected.startNameStep( Axis.CHILD,
                                  "",
                                  "baz" );
        this.expected.endNameStep();
        this.expected.endRelativeLocationPath();
        assertEquals( this.expected,
          this.actual );

    }

    public void testAbsoluteLocationPath() throws SAXPathException
    {
        
        this.text = "/foo/bar/baz";
        this.reader.setUpParse( this.text );
        this.reader.locationPath( true );
        this.expected.startAbsoluteLocationPath();
        this.expected.startNameStep( Axis.CHILD,
                                  "",
                                  "foo" );
        this.expected.endNameStep();
        this.expected.startNameStep( Axis.CHILD,
                                  "",
                                  "bar" );
        this.expected.endNameStep();
        this.expected.startNameStep( Axis.CHILD,
                                  "",
                                  "baz" );
        this.expected.endNameStep();
        this.expected.endAbsoluteLocationPath();
        assertEquals( this.expected,
          this.actual );

    }*/

    public void testNoSpaceAfterDiv() throws JaxenException 
    {
        XPath xpath = new DOMXPath( "105 div10" );
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(10.5, result.doubleValue(), 0.000001);
    }


    public void testNoSpaceAfterMod() throws JaxenException 
    {
        XPath xpath = new DOMXPath( "105 mod10" );
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(5, result.intValue());
    }


    public void testNoSpaceAfterPlus() throws JaxenException 
    {
        XPath xpath = new DOMXPath( "105 +10" );
        Double result = (Double) xpath.evaluate(doc);
        assertEquals(115, result.intValue());
    }
    
    public void testNoSpaceAfterAnd() throws JaxenException 
    {
        XPath xpath = new DOMXPath("true() andfalse()");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertFalse(result.booleanValue());
    }
    
    public void testNoSpaceAfterOr() throws JaxenException 
    {
        XPath xpath = new DOMXPath("true() orfalse()");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
    public void testAndImmediatelyFollowedByRelativeLocationPath() throws JaxenException  
    {
        XPath xpath = new DOMXPath("true() andfoo");
        Boolean result = (Boolean) xpath.evaluate(doc);
        assertTrue(result.booleanValue());
    }
    
}
