// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.dom4j;

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
