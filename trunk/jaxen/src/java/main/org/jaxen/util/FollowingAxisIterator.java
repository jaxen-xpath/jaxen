// Copyright 2001 Erwin Bolwidt. All rights reserved.

package org.jaxen.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

public class FollowingAxisIterator implements Iterator
{
    private final static Iterator EMPTY_ITERATOR = Collections.EMPTY_LIST.iterator();
    
    private Object contextNode;
    
    private Navigator navigator;

    private Iterator siblings;

    private Iterator currentSibling;

    public FollowingAxisIterator(Object contextNode,
                                 Navigator navigator) throws UnsupportedAxisException
    {
        this.contextNode = contextNode;
        this.navigator = navigator;
        this.siblings = navigator.getFollowingSiblingAxisIterator(contextNode);
        this.currentSibling = EMPTY_ITERATOR;
    }

    private boolean goForward()
    {
        while ( ! siblings.hasNext() )
        {
            if ( !goUp() )
            {
                return false;
            }
        }

        Object nextSibling = siblings.next();

        this.currentSibling = new DescendantOrSelfAxisIterator(nextSibling, navigator);

        return true;
    }

    private boolean goUp()
    {
        if ( contextNode == null
             ||
             navigator.isDocument(contextNode) )
        {
            return false;
        }

        try
        {
            contextNode = navigator.getParentNode( contextNode );

            if ( contextNode != null
                 &&
                 !navigator.isDocument(contextNode) )
            {
                siblings = navigator.getFollowingSiblingAxisIterator(contextNode);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (UnsupportedAxisException e)
        {
            // Appearantly, the parent/following-siblings axis is not supported
            // for the parent node, so the iterator can't go up in the
            // ancestry anymore.
            return false;
        }
    }

    public boolean hasNext()
    {
        while ( ! currentSibling.hasNext() )
        {
            if ( ! goForward() )
            {
                return false;
            }
        }

        return true;
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        return currentSibling.next();
    }

    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
