
package org.jaxen.dom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.jaxen.Navigator;
import org.jaxen.XPathTestBase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DocumentNavigatorTest extends XPathTestBase
{    
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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse( url );
    }
}
