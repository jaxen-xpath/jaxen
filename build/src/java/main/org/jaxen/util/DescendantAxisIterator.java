// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.util;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

import java.util.Iterator;

public class DescendantAxisIterator extends StackedIterator
{
    public DescendantAxisIterator(Object contextNode,
                                  Navigator navigator)
    {
        super( contextNode,
               navigator );
    }

    protected DescendantAxisIterator()
    {

    }

    protected Iterator createIterator(Object contextNode) 
    {
        try
        {
            return getNavigator().getChildAxisIterator( contextNode );
        }
        catch (UnsupportedAxisException e)
        {
            // okay...
        }

        return null;
    }
}
