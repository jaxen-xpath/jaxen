// XPath.java - top-level entry point for DOM XPath matching.

package org.jaxen.dom;

import org.jaxen.JaxenException;

import org.jaxen.Navigator;
import org.jaxen.BaseXPath;

/**
 * An XPath implementation for the W3C DOM model
 *
 * <p>This is the main entry point for matching an XPath against a DOM
 * tree.  You create a compiled XPath object, then match it against
 * one or more context nodes using the {@link #selectNodes}
 * method, as in the following example:</p>
 *
 * <pre>
 * XPath path = new XPath("a/b/c");
 * List results = path.selectNodes(domContextNode);
 * </pre>
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class XPath extends BaseXPath
{

    /**
     * Constructor.
     *
     * @param xpathExpr A string containing an XPath expression.
     * @exception JaxenException If there is a syntactic error in
     *            the XPath expression.
     */
    public XPath(String xpathExpr) throws JaxenException
    {
        super( xpathExpr );
    }


    /**
     * Generate a navigator object for a DOM tree.
     *
     * <p>This method is used internally by the superclass to get
     * the right kind of navigator.</p>
     *
     * @return An instance of a navigator for a DOM2 object tree.
     */
    public Navigator getNavigator()
    {
        return DocumentNavigator.getInstance();
    }

} 

// end of XPath.java
