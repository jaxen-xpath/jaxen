
package org.jaxen.jdom;

import org.jaxen.JaxenException;

import org.jaxen.BaseXPath;
import org.jaxen.Navigator;
import org.jaxen.XPath;

/** An XPath implementation for the JDOM model
 *
 * <p>This is the main entry point for matching an XPath against a DOM
 * tree.  You create a compiled XPath object, then match it against
 * one or more context nodes using the {@link #selectNodes}
 * method, as in the following example:</p>
 *
 * <pre>
 * Object jdomNode = ...; // Document, Element etc.
 * XPath path = new JDOMXPath("a/b/c");
 * List results = path.selectNodes(jdomNode);
 * </pre>
 *
 * @see BaseXPath
 * @see <a href="http://jdom.org/">The JDOM website</a>
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 * @author <a href="mailto:jstachan@apache.org">James Strachan</a>
 *
 * @version $Revision$
 */
public class JDOMXPath extends BaseXPath
{
    /** Construct given an XPath expression string.
     *
     *  @param xpathExpr The XPath expression.
     *
     *  @throws JaxenException if there is a syntax error while
     *          parsing the expression.
     */
    public JDOMXPath(String xpathExpr) throws JaxenException
    {
        super( xpathExpr, DocumentNavigator.getInstance() );
    }
} 
