// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.Navigator;

import java.util.List;

abstract class DefaultRelationalExpr extends DefaultTruthExpr 
{
    public DefaultRelationalExpr(Expr lhs,
                                 Expr rhs)
    {
        super( lhs,
               rhs );
    }
    
    public String toString()
    {
        return "[(DefaultRelationalExpr): " + getLHS() + ", " + getRHS() + "]";
    }

    public Object evaluate(Context context)
    {
        Object lhsValue = getLHS().evaluate( context );
        Object rhsValue = getLHS().evaluate( context );

        Navigator nav = context.getNavigator();

        if ( bothAreSets( lhsValue,
                          rhsValue ) )
        {
            return evaluateSetSet( (List) lhsValue,
                                   (List) rhsValue,
                                   nav );
        }

        if ( eitherIsSet( lhsValue,
                          rhsValue ) )
        {
            if ( isSet( lhsValue ) )
            {
                if ( isString( rhsValue ) )
                {
                    return evaluateSetString( (List) lhsValue,
                                              (String) rhsValue,
                                              nav );
                }
                else if ( isNumber( rhsValue ) )
                {
                    return evaluateSetNumber( (List) lhsValue,
                                              (Number) rhsValue,
                                              nav );
                }
                else if ( isBoolean( rhsValue ) )
                {
                    return evaluateSetBoolean( (List) lhsValue,
                                               (Boolean) rhsValue );
                }
            }
            else
            {
                if ( isString( lhsValue ) )
                {
                    return evaluateStringSet( (String) lhsValue,
                                              (List) rhsValue,
                                              nav );
                }
                else if ( isNumber( lhsValue )  )
                {
                    return evaluateNumberSet( (Number) lhsValue,
                                              (List) rhsValue,
                                              nav );
                }
                else if ( isBoolean( lhsValue ) )
                {
                    return evaluateSetBoolean( (List) rhsValue,
                                               (Boolean) rhsValue );
                }
            }
        }

        return evaluateObjectObject( lhsValue,
                                     rhsValue );
    }

    protected abstract Object evaluateSetSet(List lhsSet,
                                             List rhsSet,
                                             Navigator nav);
    
    protected abstract Object evaluateSetBoolean(List theSet,
                                                 Boolean theBool);

    protected abstract Object evaluateSetString(List theSet,
                                                String theStr,
                                                boolean reverse,
                                                Navigator nav);

    protected abstract Object evaluateSetNumber(List theSet,
                                                Number theNum,
                                                boolean reverse,
                                                Navigator nav);

    protected abstract Object evaluateObjectObject(Object lhs,
                                                   Object rhs);
    
    protected Object evaluateStringSet(String theStr,
                                       List theSet,
                                       Navigator nav)
    {
        return evaluateSetString( theSet,
                                  theStr,
                                  true,
                                  nav );
    }

    protected Object evaluateSetString(List theSet,
                                       String theStr,
                                       Navigator nav)
    {
        return evaluateSetString( theSet,
                                  theStr,
                                  false,
                                  nav );
    }

    protected Object evaluateNumberSet(Number theNum,
                                       List theSet,
                                       Navigator nav)
    {
        return evaluateSetNumber( theSet,
                                  theNum,
                                  true,
                                  nav );
    }

    protected Object evaluateSetNumber(List theSet,
                                       Number theNum,
                                       Navigator nav)
    {
        return evaluateSetNumber( theSet,
                                  theNum,
                                  false,
                                  nav );
    }
}

