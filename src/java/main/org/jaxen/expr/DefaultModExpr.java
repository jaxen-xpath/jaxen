
package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;

import org.jaxen.function.NumberFunction;

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

    public Object evaluate(Context context) throws JaxenException
    {
        Number lhsValue = NumberFunction.evaluate( getLHS().evaluate( context ),
                                                   context.getNavigator() );
        Number rhsValue = NumberFunction.evaluate( getRHS().evaluate( context ),
                                                   context.getNavigator() );

        int result = lhsValue.intValue() % rhsValue.intValue();

        return new Integer( result );
    }
}
