// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.util;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

import java.util.Iterator;

public class DescendantOrSelfAxisIterator extends DescendantAxisIterator
{
    public DescendantOrSelfAxisIterator(Object contextNode,
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
}
