// Copyright 2001 bob mcwhirter & James Strachan.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;

import org.jaxen.function.BooleanFunction;

class DefaultAndExpr extends DefaultLogicalExpr 
{
    public DefaultAndExpr(Expr lhs,
                          Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "and";
    }

    public String toString()
    {
        return "[(DefaultAndExpr): " + getLHS() + ", " + getRHS() + "]";
    }

    public Object evaluate(Context context) throws JaxenException
    {
        Boolean lhsValue = BooleanFunction.evaluate( getLHS().evaluate( context ) );

        if ( lhsValue == Boolean.FALSE )
        {
            return Boolean.FALSE;
        }

        Boolean rhsValue = BooleanFunction.evaluate( getRHS().evaluate( context ) );

        if ( rhsValue == Boolean.FALSE )
        {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
