
package org.jaxen.expr;

import org.jaxen.Context;

class DefaultDivExpr extends DefaultMultiplicativeExpr
{
    public DefaultDivExpr(Expr lhs,
                          Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "div";
    }

    public Object evaluate(Context context)
    {
        Number lhsValue = convertToNumber( getLHS().evaluate( context ) );
        Number rhsValue = convertToNumber( getRHS().evaluate( context ) );

        double result = lhsValue.doubleValue() / rhsValue.doubleValue();

        return new Double( result );
    }
}
