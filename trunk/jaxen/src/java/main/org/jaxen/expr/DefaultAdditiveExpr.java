// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

abstract class DefaultAdditiveExpr extends DefaultArithExpr 
{
    public DefaultAdditiveExpr(Expr lhs,
                               Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String toString()
    {
        return "[(DefaultAdditiveExpr): " + getLHS() + ", " + getRHS() + "]";
    }
}
