// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.util;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class FollowingSiblingAxisIterator implements Iterator
{
    private Object    contextNode;
    private Navigator navigator;

    private Iterator  siblingIter;

    private Object    nextObj;

    public FollowingSiblingAxisIterator(Object contextNode,
                                        Navigator navigator) throws UnsupportedAxisException
    {
        this.contextNode = contextNode;
        this.navigator   = navigator;

        init();

        stepAhead();
    }

    private void init() throws UnsupportedAxisException
    {
        Object parent = this.navigator.getParentNode( this.contextNode );

        List siblings = Collections.EMPTY_LIST;

        boolean foundSelf = false;

        if ( parent != null )
        {
            Iterator childIter = this.navigator.getChildAxisIterator( parent );
            Object   eachChild = null;
            
            siblings = new ArrayList();
            
            while ( childIter.hasNext() )
            {
                eachChild = childIter.next();

                if ( foundSelf )
                {
                    siblings.add( eachChild );
                }
                else if ( eachChild == this.contextNode )
                {
                    foundSelf = true;
                }
            }
        }

        this.siblingIter = siblings.iterator();
    }

    public boolean hasNext()
    {
        return ( this.nextObj != null );
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        Object obj = this.nextObj;

        this.nextObj = null;

        stepAhead();

        return obj;
    }

    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    private void stepAhead()
    {
        if ( this.nextObj != null )
        {
            return;
        }

        Object obj = null;

        while ( siblingIter.hasNext() )
        {
            obj = siblingIter.next();

            if ( this.navigator.isElement( obj ) )
            {
                this.nextObj = obj;
                break;
            }
        }
    }
}
