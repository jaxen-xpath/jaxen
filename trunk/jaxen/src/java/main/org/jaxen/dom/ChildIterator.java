
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
    private Node next;
    private int index = -1;


    public ChildIterator(NodeList children)
    {
        this.children = children;
    }

    public boolean hasNext()
    {
        if ( index < 0 )
        {
            this.next = findNext();
        }
        return ( this.next != null );
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        Node obj = this.next;

        this.next = findNext();
        
        return obj;
    }

    protected Node findNext()
    {
        while ( ++index < children.getLength() )
        {
            Node node = children.item( index );

            int  type = node.getNodeType();

            if ( type == Node.DOCUMENT_TYPE_NODE
                 ||
                 type == Node.NOTATION_NODE )
            {
                continue;
            }
            else
            {
                return node;
            }
        }
        return null;
    }

    public void remove()
    {
        throw new UnsupportedOperationException( "remove() is not supported by this iterator" );
    }
}
