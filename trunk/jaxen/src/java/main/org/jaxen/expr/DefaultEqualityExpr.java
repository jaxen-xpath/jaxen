// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.Navigator;
import org.jaxen.JaxenException;

import org.jaxen.function.StringFunction;
import org.jaxen.function.BooleanFunction;
import org.jaxen.function.NumberFunction;

import java.util.List;

abstract class DefaultEqualityExpr extends DefaultTruthExpr 
{
    public DefaultEqualityExpr(Expr lhs,
                               Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String toString()
    {
        return "[(DefaultEqualityExpr): " + getLHS() + ", " + getRHS() + "]";
    }
    
    public Object evaluate(Context context) throws JaxenException
    {
        Object lhsValue = getLHS().evaluate( context );
        Object rhsValue = getRHS().evaluate( context );

        //System.out.println( "Comparing: " + lhsValue + " and: " + rhsValue );
        
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
            List   theSet   = null;
            Object theOther = null;

            if ( isSet( lhsValue ) )
            {
                theSet   = (List) lhsValue;
                theOther = rhsValue;
            }
            else
            {
                theSet   = (List) rhsValue;
                theOther = lhsValue;
            }

            if ( isNumber( theOther ) )
            {
                return evaluateSetNumber( theSet,
                                          (Number) theOther,
                                          nav );
            }

            if ( isBoolean( theOther ) )
            {
                return evaluateSetBoolean( theSet,
                                           (Boolean) theOther,
                                           nav );
            }

            if ( isString( theOther ) )
            {
                return evaluateSetString( theSet,
                                          (String) theOther,
                                          nav );
            }
        }

        if ( eitherIsBoolean( lhsValue,
                              rhsValue ) )
        {
            return evaluateObjectObject( BooleanFunction.evaluate( lhsValue ),
                                         BooleanFunction.evaluate( rhsValue ) );
        }

        if ( eitherIsNumber( lhsValue,
                             rhsValue ) )
        {
            return evaluateObjectObject( NumberFunction.evaluate( lhsValue,
                                                                  nav ),
                                         NumberFunction.evaluate( rhsValue,
                                                                  nav ) );
                                              
        }
        
        return evaluateObjectObject( StringFunction.evaluate( lhsValue,
                                                              nav ),
                                     StringFunction.evaluate( rhsValue,
                                                              nav ) );
                                          
    }

    protected abstract Object evaluateSetSet(List lhsSet,
                                             List rhsSet,
                                             Navigator nav);

    protected abstract Object evaluateSetBoolean(List set,
                                                 Boolean bool,
                                                 Navigator nav);
    
    protected abstract Object evaluateSetNumber(List set,
                                                Number num,
                                                Navigator nav);
    
    protected abstract Object evaluateSetString(List set,
                                                String str,
                                                Navigator nav);
    
    protected abstract Object evaluateObjectObject(Object lhs,
                                                   Object rhs);
}
