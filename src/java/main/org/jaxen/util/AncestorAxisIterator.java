// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.util;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

import java.util.Iterator;

public class AncestorAxisIterator extends StackedIterator
{
    public AncestorAxisIterator(Object contextNode,
                                Navigator navigator)
    {
        super( contextNode,
               navigator );
    }

    protected AncestorAxisIterator()
    {

    }

    protected Iterator createIterator(Object contextNode) 
    {
        try
        {
            return getNavigator().getParentAxisIterator( contextNode );
        }
        catch (UnsupportedAxisException e)
        {
            // okay...
        }

        return null;
    }
}
