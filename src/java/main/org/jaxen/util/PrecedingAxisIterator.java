// Copyright 2001 Erwin Bolwidt. All rights reserved.
// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

public class PrecedingAxisIterator implements Iterator
{
    private final class ReverseDescendantOrSelfAxisIterator extends StackedIterator
    {
        ReverseDescendantOrSelfAxisIterator(Object contextNode)
            throws UnsupportedAxisException
        {
            pushIterator(navigator.getSelfAxisIterator(contextNode));
            init(contextNode, navigator);
        }
        
        protected Iterator createIterator(Object contextNode) 
        {
            try
            {
                Iterator iter = navigator.getChildAxisIterator(contextNode);

                if (iter == null)
                {
                    return null;
                }

                LinkedList reverse = new LinkedList();

                while ( iter.hasNext() )
                {
                    reverse.addFirst( iter.next() );
                }

                return reverse.iterator();
            }
            catch (UnsupportedAxisException e)
            {
                // okay...
            }
            return null;
        }
    }

    private final static Iterator EMPTY_ITERATOR = Collections.EMPTY_LIST.iterator();

    private Object contextNode;

    private Navigator navigator;

    private Iterator siblings;

    private Iterator currentSibling;

    public PrecedingAxisIterator(Object contextNode,
                                 Navigator navigator) throws UnsupportedAxisException
    {
        this.contextNode    = contextNode;
        this.navigator      = navigator;
        this.siblings       = navigator.getPrecedingSiblingAxisIterator(contextNode);
        this.currentSibling = EMPTY_ITERATOR;
    }

    private boolean goBack()
    {
        while ( ! siblings.hasNext() )
        {
            if (!goUp())
            {
                return false;
            }
        }

        Object prevSibling = siblings.next();

        try
        {
            this.currentSibling = new ReverseDescendantOrSelfAxisIterator( prevSibling );
            return true;
        }
        catch (UnsupportedAxisException e)
        {
            return false;
        }
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
            contextNode = navigator.getParentNode(contextNode);

            if ( contextNode != null
                 &&
                 ! navigator.isDocument(contextNode) )
            {
                siblings = navigator.getPrecedingSiblingAxisIterator(contextNode);

                return true;
            }

            return false;
        }
        catch (UnsupportedAxisException e)
        {
            // Appearantly, the preceding-siblings axis is not supported
            // for the parent node, so the iterator can't go up in the
            // ancestry anymore.
            return false;
        }
    }

    public boolean hasNext()
    {
        while ( ! currentSibling.hasNext() )
        {
            if ( ! goBack() )
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
