
package org.jaxen.exml;

import electric.xml.Attributes;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AttributesIterator implements Iterator
{
    private Attributes elements;

    public AttributesIterator(Attributes elements)
    {
        this.elements = elements;
    }

    public boolean hasNext()
    {
        return ( this.elements.current() != null );
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        return this.elements.next();
    }

    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
