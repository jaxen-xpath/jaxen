// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.util.SingleObjectIterator;

import java.util.Iterator;
import java.util.Set;
import java.util.Collections;


class DefaultNumberExpr extends DefaultExpr implements NumberExpr
{
    private Number number;

    public DefaultNumberExpr(Number number)
    {
        this.number = number;
    }

    public Number getNumber()
    {
        return this.number;
    }

    public String toString()
    {
        return "[(DefaultNumberExpr): " + getNumber() + "]";
    }

    public String getText()
    {
        return getNumber().toString();
    }

    public Object evaluate(Context context)
    {
        return getNumber();
    }
}
