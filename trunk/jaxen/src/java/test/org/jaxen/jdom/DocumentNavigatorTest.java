
package org.jaxen.jdom;

import org.jaxen.XPathTestBase;

import junit.framework.TestCase;

import org.jaxen.Navigator;

import org.jdom.input.SAXBuilder;

public class DocumentNavigatorTest extends XPathTestBase
{
    private SAXBuilder builder = new SAXBuilder();

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
        return this.builder.build( url );
    }
}
