
package org.jaxen.expr;

import org.jaxen.Context;

class DefaultPlusExpr extends DefaultAdditiveExpr
{
    public DefaultPlusExpr(Expr lhs,
                           Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "+";
    }

    public Object evaluate(Context context)
    {
        Number lhsValue = convertToNumber( getLHS().evaluate( context ) );
        Number rhsValue = convertToNumber( getRHS().evaluate( context ) );

        if ( lhsValue instanceof Double
             ||
             rhsValue instanceof Double )
        {
            double result = lhsValue.doubleValue() + rhsValue.doubleValue();

            return new Double( result );
        }

        int result = lhsValue.intValue() + rhsValue.intValue();

        return new Integer( result );
    }
}
