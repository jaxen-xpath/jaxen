// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;

import org.jaxen.function.NumberFunction;

class DefaultUnaryExpr extends DefaultExpr implements UnaryExpr
{
    private Expr                   expr;

    public DefaultUnaryExpr(Expr expr)
    {
        this.expr     = expr;
    }

    public Expr getExpr()
    {
        return this.expr;
    }

    public String toString()
    {
        return "[(DefaultUnaryExpr): " + getExpr() + "]";
    }

    public String getText()
    {
        return "-(" + getExpr().getText() + ")";
    }

    public Object evaluate(Context context)
    {
        Number number = NumberFunction.evaluate( getExpr().evaluate( context ),
                                                 context.getNavigator() );

        if ( number instanceof Integer )
        {
            return new Integer( number.intValue() * -1 );
        }

        return new Double( number.doubleValue() * -1 );
    }
}
