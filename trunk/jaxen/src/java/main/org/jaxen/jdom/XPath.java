
package org.jaxen.jdom;

import org.jaxen.Navigator;
import org.jaxen.BaseXPath;
import org.saxpath.SAXPathException;

public class XPath extends BaseXPath
{
    public XPath(String xpathExpr) throws SAXPathException
    {
        super( xpathExpr );
    }

    protected Navigator getNavigator()
    {
        return DocumentNavigator.getInstance();
    }
} 
