// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;

class DefaultPredicate implements Predicate
{
    private Expr expr;

    public DefaultPredicate(Expr expr)
    {
        setExpr( expr );
    }

    public Expr getExpr()
    {
        return this.expr;
    }

    public void setExpr(Expr expr)
    {
        this.expr = expr;
    }

    public String getText()
    {
        return "[" + getExpr().getText() + "]";
    }

    public String toString()
    {
        return "[(DefaultPredicate): " + getExpr() + "]";
    }

    public void simplify()
    {
        setExpr( getExpr().simplify() );
    }

    public Object evaluate(Context context) throws JaxenException
    {
        return getExpr().evaluate( context );
    }
}
