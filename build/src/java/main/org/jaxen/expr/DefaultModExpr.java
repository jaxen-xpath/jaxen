
package org.jaxen.expr;

import org.jaxen.Context;

class DefaultModExpr extends DefaultMultiplicativeExpr
{
    public DefaultModExpr(Expr lhs,
                          Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "mod";
    }

    public Object evaluate(Context context)
    {
        Number lhsValue = convertToNumber( getLHS().evaluate( context ) );
        Number rhsValue = convertToNumber( getRHS().evaluate( context ) );

        int result = lhsValue.intValue() % rhsValue.intValue();

        return new Integer( result );
    }
}
