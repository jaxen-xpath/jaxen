// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SelfAxisIterator implements Iterator
{
    private boolean hasNext;
    private Object  node;

    public SelfAxisIterator(Object node)
    {
        this.hasNext = true;
        this.node    = node;
    }

    public boolean hasNext()
    {
        return this.hasNext;
    }

    public Object next() throws NoSuchElementException
    {
        if ( hasNext() )
        {
            this.hasNext = false;
            return this.node;
        }

        throw new NoSuchElementException();
    }

    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
        

}
