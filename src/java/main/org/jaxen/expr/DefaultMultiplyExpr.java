
package org.jaxen.expr;

import org.jaxen.Context;

import org.jaxen.function.NumberFunction;

class DefaultMultiplyExpr extends DefaultMultiplicativeExpr
{
    public DefaultMultiplyExpr(Expr lhs,
                               Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "*";
    }

    public Object evaluate(Context context)
    {
        Number lhsValue = NumberFunction.evaluate( getLHS().evaluate( context ),
                                                   context.getNavigator() );
        Number rhsValue = NumberFunction.evaluate( getRHS().evaluate( context ),
                                                   context.getNavigator() );

        if ( lhsValue instanceof Double
             ||
             rhsValue instanceof Double )
        {
            double result = lhsValue.doubleValue() * rhsValue.doubleValue();

            return new  Double( result );
        }

        int result = lhsValue.intValue() * rhsValue.intValue();

        return new Integer( result );
    }
}
