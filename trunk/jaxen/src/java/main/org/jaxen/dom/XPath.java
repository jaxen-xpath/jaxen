package org.jaxen.dom;

import org.jaxen.Navigator;
import org.jaxen.BaseXPath;
import org.saxpath.SAXPathException;

/** An XPath implementation for the W3C DOM model
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
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
