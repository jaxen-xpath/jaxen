// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;

import org.jaxen.function.BooleanFunction;

class DefaultOrExpr extends DefaultLogicalExpr 
{
    public DefaultOrExpr(Expr lhs,
                         Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "or";
    }

    public String toString()
    {
        return "[(DefaultOrExpr): " + getLHS() + ", " + getRHS() + "]";
    }

    public Object evaluate(Context context)
    {
        Boolean lhsValue = BooleanFunction.evaluate( getLHS().evaluate( context ) );

        if ( lhsValue == Boolean.TRUE )
        {
            return Boolean.TRUE;
        }

        Boolean rhsValue = BooleanFunction.evaluate( getRHS().evaluate( context ) );

        if ( rhsValue == Boolean.TRUE )
        {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
