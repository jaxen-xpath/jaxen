// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.dom4j;

import org.jaxen.JaxenException;

import org.jaxen.Navigator;
import org.jaxen.BaseXPath;

/** An XPath implementation for the dom4j model
 *
 * <p>This is the main entry point for matching an XPath against a DOM
 * tree.  You create a compiled XPath object, then match it against
 * one or more context nodes using the {@link #selectNodes}
 * method, as in the following example:</p>
 *
 * <pre>
 * Node node = ...;
 * XPath path = new Dom4jXPath("a/b/c");
 * List results = path.selectNodes(node);
 * </pre>
 *
 * @see BaseXPath
 * @see <a href="http://dom4j.org/">The dom4j website</a>
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 * @author <a href="mailto:jstachan@apache.org">James Strachan</a>
 *
 * @version $Revision$
 */
public class Dom4jXPath extends BaseXPath
{
    /** Construct given an XPath expression string.
     *
     *  @param xpathExpr The XPath expression.
     *
     *  @throws JaxenException if there is a syntax error while
     *          parsing the expression.
     */
    public Dom4jXPath(String xpathExpr) throws JaxenException
    {
        super( xpathExpr, DocumentNavigator.getInstance() );
    }
} 
