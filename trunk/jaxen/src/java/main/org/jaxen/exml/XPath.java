
package org.jaxen.exml;

import org.jaxen.Navigator;
import org.jaxen.BaseXPath;
import org.saxpath.SAXPathException;

public class XPath extends BaseXPath
{
    public XPath(String xpathExpr) throws SAXPathException
    {
        super( xpathExpr );
    }

    public Navigator getNavigator()
    {
        return DocumentNavigator.getInstance();
    }
} 
