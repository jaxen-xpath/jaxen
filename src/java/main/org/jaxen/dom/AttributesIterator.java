
package org.jaxen.dom;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.NamedNodeMap;

/** An iterator over W3C DOM attributes
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class AttributesIterator implements Iterator
{
    private NamedNodeMap attributes;
    private int index = -1;


    public AttributesIterator(NamedNodeMap attributes)
    {
        this.attributes = attributes;
    }

    public boolean hasNext()
    {
        return ++index < attributes.getLength();
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
}
