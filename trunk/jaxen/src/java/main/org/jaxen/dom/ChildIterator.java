
package org.jaxen.dom;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** An iterator over W3C DOM children
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class ChildIterator implements Iterator
{
    private NodeList children;
    private int index = -1;


    public ChildIterator(NodeList children)
    {
        this.children = children;
    }

    public boolean hasNext()
    {
        while ( ++index < children.getLength() )
        {
            Node node = children.item(index);
            int type = node.getNodeType();
            if ( type == Node.DOCUMENT_TYPE_NODE || type == Node.NOTATION_NODE ) {
                continue;
            }
            return true;
        }        
        return false;
    }

    public Object next() throws NoSuchElementException
    {
        if ( index >= children.getLength() )
        {
            throw new NoSuchElementException();
        }
        return children.item(index);
    }

    public void remove()
    {
        throw new UnsupportedOperationException( "remove() is not supported by this iterator" );
    }
}
