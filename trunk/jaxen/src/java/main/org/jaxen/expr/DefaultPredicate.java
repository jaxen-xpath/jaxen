// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.ContextSupport;

//class DefaultPredicate extends DefaultExpr implements Predicate
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

    public Object evaluate(Context context)
    {
        return getExpr().evaluate( context );
    }

    public boolean matches(Object node,
                           int position,
                           int contextSize,
                           ContextSupport support)
    {
        System.err.println("Predicate.matches(" + node + ")");

        return true;
    }
}
