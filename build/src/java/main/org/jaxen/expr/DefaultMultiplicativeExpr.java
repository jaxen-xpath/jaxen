// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

abstract class DefaultMultiplicativeExpr extends DefaultArithExpr 
{
    public DefaultMultiplicativeExpr(Expr lhs,
                                     Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String toString()
    {
        return "[(DefaultMultiplicativeExpr): " + getLHS() + ", " + getRHS() + "]";
    }
}
