
package org.jaxen.dom;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

/** An iterator over W3C DOM children
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class ChildIterator implements Iterator
{
    private Node parent;
    private Node current;
    private boolean first = true;

    public ChildIterator(Node parent)
    {
        this.parent = parent;
    }

    public boolean hasNext()
    {
        while ( true )
        {
            if ( first ) 
            {
                first = false;
                current = parent.getFirstChild();
            }
            else 
            {
                current = current.getNextSibling();
                if ( current == null )
                {
                    return false;
                }
            }
            int type = current.getNodeType();
            if ( type == Node.DOCUMENT_TYPE_NODE || type == Node.NOTATION_NODE ) {
                continue;
            }
            return true;
        }
    }

    public Object next() throws NoSuchElementException
    {
        if ( current == null ) 
        {
            throw new NoSuchElementException();
        }
        return current;
    }

    public void remove()
    {
        if ( current != null )
        {
            parent.removeChild( current );
        }
    }
}
