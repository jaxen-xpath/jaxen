// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Navigator;

import org.jaxen.function.StringFunction;
import org.jaxen.function.BooleanFunction;
import org.jaxen.function.NumberFunction;

import java.util.List;

class DefaultNotEqualsExpr extends DefaultEqualityExpr 
{
    public DefaultNotEqualsExpr(Expr lhs,
                                Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "!=";
    }

    public String toString()
    {
        return "[(DefaultNotEqualsExpr): " + getLHS() + ", " + getRHS() + "]";
    }
    
    

    protected Object evaluateSetSet(List lhsSet,
                                    List rhsSet,
                                    Navigator nav)
    {
        int lhsSize = lhsSet.size();
        int rhsSize = rhsSet.size();
        
        String lhsElem = null;
        String rhsElem = null;
        
        for ( int i = 0 ; i < lhsSize ; ++i )
        {
            lhsElem = StringFunction.evaluate( lhsSet.get( i ),
                                               nav );
            
            for ( int j = 0; j < rhsSize ; ++j )
            {
                rhsElem = StringFunction.evaluate( rhsSet.get( j ),
                                                   nav );
                
                if ( ! lhsElem.equals( rhsElem ) )
                {
                    return Boolean.TRUE;
                }
            }
        }
        
        return Boolean.FALSE;
    }
    
    protected Object evaluateEmptySet(Object theOther)
      {
      return theOther == null ? Boolean.TRUE : Boolean.FALSE;
      }    

    protected Object evaluateSetBoolean(List theSet,
                                        Boolean theBool,
                                        Navigator nav)
    {

        int setSize = theSet.size();

        for ( int i = 0 ; i < setSize ; ++i )
        {
            if ( ! theBool.equals( BooleanFunction.evaluate( theSet.get( i ), nav ) ) )
            {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateSetNumber(List theSet,
                                       Number theNum,
                                       Navigator nav)
    {
        int setSize = theSet.size();

        for ( int i = 0 ; i < setSize ; ++i )
        {
            if ( ! theNum.equals( NumberFunction.evaluate( theSet.get( i ),
                                                           nav ) ) )
            {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateSetString(List theSet,
                                       String theStr,
                                       Navigator nav)
    {
        int setSize = theSet.size();

        for ( int i = 0 ; i < setSize ; ++i )
        {            
            if ( ! theStr.equals( StringFunction.evaluate( theSet.get( i ),
                                                           nav ) ) )
            {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateObjectObject(Object lhs,
                                          Object rhs)
    {
        if ( lhs.equals( rhs ) )
        {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
