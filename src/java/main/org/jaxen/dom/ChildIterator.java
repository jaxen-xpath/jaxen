
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

    private Node next;


    public ChildIterator(NodeList children)
    {
        this.children = children;
        this.next     = null;
        stepAhead();
    }

    public boolean hasNext()
    {
        return ( this.next != null );
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        Object obj = this.next;

        this.next = null;

        stepAhead();

        return obj;
    }

    private void stepAhead()
    {
        if ( this.next != null )
        {
            return;
        }

        while ( ++index < children.getLength() )
        {
            this.next = children.item( index );

            int  type = this.next.getNodeType();

            if ( type == Node.DOCUMENT_TYPE_NODE
                 ||
                 type == Node.NOTATION_NODE )
            {
                this.next = null;
                continue;
            }
            else
            {
                break;
            }
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException( "remove() is not supported by this iterator" );
    }
}
