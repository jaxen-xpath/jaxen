
package org.jaxen.dom;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/** An iterator over W3C DOM attributes
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class AttributeIterator implements Iterator
{
    private NamedNodeMap attributes;
    private int index = -1;


    public AttributeIterator(NamedNodeMap attributes)
    {
        this.attributes = attributes;
    }

    public boolean hasNext()
    {
        while ( ++index < attributes.getLength() )
        {
            Node node = attributes.item(index);
            if ( validNode( node ) )
            {
                return true;
            }
        }        
        return false;
    }

    public Object next() throws NoSuchElementException
    {
        if ( index >= attributes.getLength() )
        {
            throw new NoSuchElementException();
        }
        return attributes.item(index);
    }

    public void remove()
    {
        throw new UnsupportedOperationException( "remove() is not supported by this iterator" );
    }
    
    /** @return true if this node is valid - i.e. the attribute does not start with xmlns */
    protected boolean validNode(Node node)
    {
        String name = node.getNodeName();
        return name == null || ! name.startsWith( "xmlns" );
    }
}
