// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;
import org.jaxen.Navigator;
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

    public Object evaluate(Context context) throws JaxenException
    {
        Navigator nav = context.getNavigator();
        Boolean lhsValue = BooleanFunction.evaluate( getLHS().evaluate( context ), nav );

        if ( lhsValue == Boolean.TRUE )
        {
            return Boolean.TRUE;
        }

        Boolean rhsValue = BooleanFunction.evaluate( getRHS().evaluate( context ), nav );

        if ( rhsValue == Boolean.TRUE )
        {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
