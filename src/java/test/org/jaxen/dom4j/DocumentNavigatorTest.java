
package org.jaxen.dom4j;

import junit.framework.TestCase;

import org.jaxen.XPathTestBase;

import org.jaxen.Navigator;

import org.dom4j.io.SAXReader;

public class DocumentNavigatorTest extends XPathTestBase
{
    private SAXReader reader = new SAXReader();

    public DocumentNavigatorTest(String name)
    {
        super( name );
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
