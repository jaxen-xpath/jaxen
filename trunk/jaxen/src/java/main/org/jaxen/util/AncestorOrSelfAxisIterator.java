// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.util;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

import java.util.Iterator;

public class AncestorOrSelfAxisIterator extends AncestorAxisIterator
{
    public AncestorOrSelfAxisIterator(Object contextNode,
                                      Navigator navigator)
    {
        try
        {
            pushIterator( navigator.getSelfAxisIterator( contextNode ) );
        }
        catch (UnsupportedAxisException e)
        {

        }

        init( contextNode,
              navigator );
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
