
package org.jaxen.dom;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/** An iterator over W3C DOM namespaces
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class NamespaceIterator extends AttributeIterator
{
    public NamespaceIterator(NamedNodeMap attributes)
    {
        super( attributes );
    }

    /** @return true if this node is valid - i.e. the attribute starts with xmlns */
    protected boolean validNode(Node node)
    {
        String name = node.getNodeName();
        return name != null && name.startsWith( "xmlns" );
    }
}
