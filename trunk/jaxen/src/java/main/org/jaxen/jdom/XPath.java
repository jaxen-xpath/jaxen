
package org.jaxen.jdom;

import org.jaxen.JaxenException;

import org.jaxen.Navigator;
import org.jaxen.BaseXPath;

public class XPath extends BaseXPath
{
    public XPath(String xpathExpr) throws JaxenException
    {
        super( xpathExpr );
    }

    public Navigator getNavigator()
    {
        return DocumentNavigator.getInstance();
    }
} 
