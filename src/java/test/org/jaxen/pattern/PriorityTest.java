// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.pattern;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jaxen.pattern.Pattern;
import org.jaxen.pattern.PatternParser;

import org.saxpath.XPathSyntaxException;

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
    }

    public void tearDown()
    {
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
    
    protected void testPriority(String expr, double priority, short nodeType) throws Exception 
    {
        System.out.println( "parsing: " + expr );
        
        Pattern pattern = PatternParser.parse( expr );
        double d = pattern.getPriority();
        short nt = pattern.getMatchType();
        
        System.out.println( "expr: " + expr + " has priority: " + d + " nodeType: " + nt );
        System.out.println( "pattern: " + pattern );
        
        assertEquals( "expr: " + expr, new Double(priority), new Double(d) );
        assertEquals( "nodeType: " + expr, new Short(nodeType), new Short(nt) );
    }
}
