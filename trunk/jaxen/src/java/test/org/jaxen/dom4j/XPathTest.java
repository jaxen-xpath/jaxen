
package org.jaxen.dom4j;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.saxpath.SAXPathException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;
import java.util.Iterator;

public class XPathTest extends TestCase
{

    private static final String BASIC_XML = "xml/basic.xml";

    public static void main( String[] args ) 
    {
        TestRunner.run( suite() );
    }
    
    public static Test suite() 
    {
        return new TestSuite( XPathTest.class );
    }
    
    public XPathTest(String name)
    {
        super( name );
    }

    public void setUp()
    {

    }

    public void tearDown()
    {

    }

    public void testConstruction()
    {
        try
        {
            XPath xpath = new XPath( "/foo/bar/baz" );
        }
        catch (SAXPathException e)
        {
            fail( e.getMessage() );
        }
    }

    public void testSelection()
    {
        try
        {
            XPath xpath = new XPath( "/foo/bar/baz" );

            SAXReader reader = new SAXReader();

            Document doc = reader.read( BASIC_XML );

            List results = xpath.selectNodes( doc );

            assertEquals( 3,
                          results.size() );

            Iterator iter = results.iterator();

            assertEquals( "baz",
                          ((Element)iter.next()).getName() );

            assertEquals( "baz",
                          ((Element)iter.next()).getName() );

            assertEquals( "baz",
                          ((Element)iter.next()).getName() );

            assertTrue( ! iter.hasNext() );

        }
        catch (Exception e)
        {
            fail( e.getMessage() );
        }
    }
    
    public void testAsBoolean()
    {
        try
        {
            XPath xpath = new XPath( "/root/a = 'a'" );

            SAXReader reader = new SAXReader();

            Document doc = reader.read( "xml/simple.xml" );

            boolean answer = xpath.booleanValueOf( doc );

            assertTrue( "Xpath worked: " + xpath, answer );

            xpath = new XPath( "'a' = 'b'" );
            
            answer = xpath.booleanValueOf( doc );

            assertTrue( "XPath should return false: " + xpath, ! answer );

        }
        catch (Exception e)
        {
            fail( e.getMessage() );
        }
    }
}
