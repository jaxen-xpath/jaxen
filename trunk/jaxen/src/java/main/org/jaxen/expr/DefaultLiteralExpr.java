// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.util.SingleObjectIterator;

import java.util.Set;
import java.util.Iterator;
import java.util.Collections;

class DefaultLiteralExpr extends DefaultExpr implements LiteralExpr
{
    private String literal;

    public DefaultLiteralExpr(String literal)
    {
        this.literal = literal;
    }

    public String getLiteral()
    {
        return this.literal;
    }

    public String toString()
    {
        return "[(DefaultLiteralExpr): " + getLiteral() + "]";
    }

    public String getText()
    {
        return "\"" + getLiteral() + "\"";
    }

    public Object evaluate(Context context)
    {
        return getLiteral();
    }
}
