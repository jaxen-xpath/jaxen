
package org.jaxen.dom4j;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.jaxen.Navigator;
import org.jaxen.XPathTestBase;

import org.dom4j.io.SAXReader;

public class DocumentNavigatorTest extends XPathTestBase
{
    private SAXReader reader = new SAXReader();

    public DocumentNavigatorTest(String name)
    {
        super( name );
    }

    public static void main(String[] args) 
    {
        verbose = true;
        if ( args.length > 0 ) 
        {
            TESTS_XML = args[0];
        }
        TestRunner.run( suite() );
    }
    
    public static Test suite() 
    {
        return new TestSuite( DocumentNavigatorTest.class );
    }
    
    public Navigator getNavigator()
    {
        return new DocumentNavigator();
    }

    public Object getDocument(String url) throws Exception
    {
        return reader.read( url );
    }
}
