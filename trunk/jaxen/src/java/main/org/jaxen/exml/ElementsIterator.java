
package org.jaxen.exml;

import electric.xml.Elements;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ElementsIterator implements Iterator
{
    private Elements elements;

    public ElementsIterator(Elements elements)
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
