
package org.jaxen;

import junit.framework.TestCase;

import org.saxpath.SAXPathException;

public class AddNamespaceTest extends TestCase
{
    public AddNamespaceTest(String name)
    {
        super( name );
    }

    public void setUp()
    {

    }

    public void tearDown()
    {

    }

    public void testDefaultContext()
    {
        try
        {
            MockXPath xpath = new MockXPath("foo");
            
            xpath.addNamespace("cheese",
                               "http://cheese.org");
            
            xpath.addNamespace("squeeze",
                               "http://squeeze.org");

            NamespaceContext nsContext = xpath.getNamespaceContext();

            assertEquals( "http://cheese.org",
                          nsContext.translateNamespacePrefixToUri( "cheese" ) );

            assertEquals( "http://squeeze.org",
                          nsContext.translateNamespacePrefixToUri( "squeeze" ) );
                          

        }
        catch (JaxenException e)
        {
            fail( e.getMessage() );
        }
        catch (SAXPathException e)
        {
            fail( e.getMessage() );
        }
    }
}

class MockXPath extends BaseXPath
{

    public MockXPath(String expr) throws SAXPathException
    {
        super( expr );
    }

    public Navigator getNavigator()
    {
        return null;
    }
}
