
package org.jaxen.exml;

import org.jaxen.XPathTestBase;
import org.jaxen.Navigator;

import junit.framework.TestCase;

import electric.xml.Document;

import java.io.File;

public class DocumentNavigatorTest extends XPathTestBase
{
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
        return new Document( new File( url ) );
    }
}
