// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;

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

        if ( bothAreSets( lhsValue,
                          rhsValue ) )
        {
            return evaluateSetSet( (List) lhsValue,
                                   (List) rhsValue );
        }

        if ( eitherIsSet( lhsValue,
                          rhsValue ) )
        {
            if ( isSet( lhsValue ) )
            {
                if ( isString( rhsValue ) )
                {
                    return evaluateSetString( (List) lhsValue,
                                              (String) rhsValue );
                }
                else if ( isNumber( rhsValue ) )
                {
                    return evaluateSetNumber( (List) lhsValue,
                                              (Number) rhsValue );
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
                                              (List) rhsValue );
                }
                else if ( isNumber( lhsValue )  )
                {
                    return evaluateNumberSet( (Number) lhsValue,
                                              (List) rhsValue );
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
                                             List rhsSet);
    
    protected abstract Object evaluateSetBoolean(List theSet,
                                                 Boolean theBool);

    protected abstract Object evaluateSetString(List theSet,
                                                String theStr,
                                                boolean reverse);

    protected abstract Object evaluateSetNumber(List theSet,
                                                Number theNum,
                                                boolean reverse);

    protected abstract Object evaluateObjectObject(Object lhs,
                                                   Object rhs);
    
    protected Object evaluateStringSet(String theStr,
                                       List theSet)
    {
        return evaluateSetString( theSet,
                                  theStr,
                                  true );
    }

    protected Object evaluateSetString(List theSet,
                                       String theStr)
    {
        return evaluateSetString( theSet,
                                  theStr,
                                  false );
    }

    protected Object evaluateNumberSet(Number theNum,
                                       List theSet)
    {
        return evaluateSetNumber( theSet,
                                  theNum,
                                  true );
    }

    protected Object evaluateSetNumber(List theSet,
                                       Number theNum)
    {
        return evaluateSetNumber( theSet,
                                  theNum,
                                  false );
    }
}

