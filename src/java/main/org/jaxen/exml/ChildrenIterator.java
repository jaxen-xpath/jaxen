
package org.jaxen.exml;

import electric.xml.Children;
import electric.xml.XMLDecl;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChildrenIterator implements Iterator
{
    private Children children;

    public ChildrenIterator(Children children)
    {
        this.children = children;
        stepAhead();
    }

    public boolean hasNext()
    {
        return ( this.children.current() != null );
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        return this.children.next();
    }

    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    private void stepAhead()
    {
        if ( this.children.current() instanceof XMLDecl )
        {
            next();
        }
    }
}
