
package org.jaxen.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleObjectIterator implements Iterator
{
    private Object  object;
    private boolean seen;

    public SingleObjectIterator(Object object)
    {
        this.object = object;
        this.seen   = false;
    }

    public boolean hasNext()
    {
        return ! this.seen;
    }

    public Object next()
    {
        if ( hasNext() )
        {
            this.seen = true;
            return this.object;
        }

        throw new NoSuchElementException();
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
