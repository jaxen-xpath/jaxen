// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.util.SingleObjectIterator;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class DefaultExpr implements Expr
{
    public Expr simplify()
    {
        return this;
    }

    static public Iterator convertToIterator(Object obj)
    {
        if ( obj instanceof Iterator )
        {
            return (Iterator) obj;
        }

        if ( obj instanceof List )
        {
            return ((List)obj).iterator();
        }

        return new SingleObjectIterator( obj );
    }

    static public List convertToList(Object obj)
    {
        if ( obj instanceof List )
        {
            return (List) obj;
        }

        List list = new ArrayList( 1 );

        list.add( obj );

        return list;
    }
}
