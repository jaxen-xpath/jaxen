// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.util;

import org.jaxen.Navigator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java.util.LinkedList;

import java.util.Set;
import java.util.HashSet;

public abstract class StackedIterator implements Iterator
{
    private Object     contextNode;
    private LinkedList iteratorStack;
    private Navigator  navigator;

    private Set        created;

    public StackedIterator(Object contextNode,
                           Navigator navigator)
    {
        this.iteratorStack = new LinkedList();
        this.created       = new HashSet();

        init( contextNode,
              navigator );
    }

    protected StackedIterator()
    {
        this.iteratorStack = new LinkedList();
        this.created       = new HashSet();
    }

    protected void init(Object contextNode,
                        Navigator navigator)
    {
        this.contextNode   = contextNode;
        this.navigator     = navigator;
        
        pushIterator( internalCreateIterator( contextNode ) );
    }

    private Iterator internalCreateIterator(Object contextNode)
    {
        if ( this.created.contains( contextNode ) )
        {
            return null;
        }

        this.created.add( contextNode );

        return createIterator( contextNode );
    }

    public boolean hasNext()
    {
        Iterator curIter = currentIterator();

        if ( curIter == null )
        {
            return false;
        }

        return curIter.hasNext();
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        Iterator curIter = currentIterator();
        Object   object  = curIter.next();

        pushIterator( internalCreateIterator( object ) );

        return object;
    }

    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    abstract protected Iterator createIterator(Object contextNode);

    protected void pushIterator(Iterator iter)
    {
        if ( iter != null )
        {
            this.iteratorStack.addLast( iter );
        }
    }

    private Iterator currentIterator()
    {
        while ( iteratorStack.size() > 0 )
        {
            Iterator curIter = (Iterator) iteratorStack.getFirst();

            if ( curIter.hasNext() )
            {
                return curIter;
            }

            iteratorStack.removeFirst();
        }

        return null;
    }

    protected Navigator getNavigator()
    {
        return this.navigator;
    }
}
