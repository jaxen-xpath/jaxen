// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;

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
        Number number = convertToNumber( getExpr().evaluate( context ) );

        if ( number instanceof Integer )
        {
            return new Integer( number.intValue() * -1 );
        }

        return new Double( number.doubleValue() * -1 );
    }
}
