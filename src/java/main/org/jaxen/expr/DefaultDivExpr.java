
package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;

import org.jaxen.function.NumberFunction;

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

    public Object evaluate(Context context) throws JaxenException
    {
        Number lhsValue = NumberFunction.evaluate( getLHS().evaluate( context ),
                                                   context.getNavigator() );
        Number rhsValue = NumberFunction.evaluate( getRHS().evaluate( context ),
                                                   context.getNavigator() );

        double result = lhsValue.doubleValue() / rhsValue.doubleValue();

        return new Double( result );
    }
}
