
package org.jaxen.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

public class LinkedIterator implements Iterator
{
    private List iterators;
    private int  cur;

    public LinkedIterator()
    {
        this.iterators = new ArrayList();
        this.cur       = 0;
    }

    public void addIterator(Iterator i)
    {
        this.iterators.add( i );
    }

    public boolean hasNext()
    {
        boolean has = false;

        if ( this.cur < this.iterators.size() )
        {
            has = ((Iterator)this.iterators.get( this.cur )).hasNext();

            if ( ! has
                 &&
                 this.cur < this.iterators.size() )
            {
                ++this.cur;
                has = hasNext();
            }
        }
        else
        {
            has = false;
        }

        return has;
    }

    public Object next()
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        return ((Iterator)this.iterators.get( this.cur )).next();
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
