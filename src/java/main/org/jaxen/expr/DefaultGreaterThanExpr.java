
package org.jaxen.expr;

import org.jaxen.Navigator;

import org.jaxen.function.StringFunction;
import org.jaxen.function.NumberFunction;

import java.util.List;

class DefaultGreaterThanExpr extends DefaultRelationalExpr
{
    public DefaultGreaterThanExpr(Expr lhs,
                                  Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return ">";
    }

    protected Object evaluateSetSet(List lhsSet,
                                    List rhsSet,
                                    Navigator nav)
    {
        int lhsSetSize = lhsSet.size();
        int rhsSetSize = rhsSet.size();
        
        String lhsStr  = null;
        String rhsStr  = null;
        
        for ( int i = 0 ; i < lhsSetSize ; ++i )
        {
            lhsStr = StringFunction.evaluate( lhsSet.get(i),
                                              nav );
            
            for ( int j = 0 ; j < rhsSetSize ; ++j )
            {
                rhsStr = StringFunction.evaluate( rhsSet.get( j ),
                                                  nav );
                
                if ( lhsStr.compareTo( rhsStr ) > 0 )
                {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateSetString(List theSet,
                                       String theStr,
                                       boolean reverse,
                                       Navigator nav)
    {
        int    setSize    = theSet.size();
        String setElement = null;

        for ( int i = 0 ; i < setSize ; ++i )
        {
            setElement = StringFunction.evaluate( theSet.get( i ),
                                                  nav );

            if ( reverse ) {
                if ( setElement.compareTo( theStr ) < 0 )
                    return Boolean.TRUE;
            } else {
                if ( setElement.compareTo( theStr ) > 0 )
                    return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateSetNumber(List theSet,
                                       Number theNum,
                                       boolean reverse,
                                       Navigator nav)
    {
        int    setSize    = theSet.size();
        Comparable setElement = null;

        for ( int i = 0 ; i < setSize ; ++i )
        {
            setElement = (Comparable) NumberFunction.evaluate( theSet.get( i ),
                                                               nav );


            if ( reverse ) { 
                if ( setElement.compareTo( theNum ) < 0 )
                    return Boolean.TRUE;
            } else {
                if ( setElement.compareTo( theNum ) > 0 )
                    return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateSetBoolean(List theSet,
                                        Boolean theBool,
                                        Navigator nav )
    {
        return Boolean.FALSE;
    }

    protected Object evaluateObjectObject(Object lhs,
                                          Object rhs)
    {
        if ( bothAreBoolean( lhs,
                             rhs ) )
        {
            return ( ( lhs.equals( rhs ) )
                     ? Boolean.TRUE
                     : Boolean.FALSE );
        }

        Comparable lhsComp = (Comparable) lhs;
        Comparable rhsComp = (Comparable) rhs;

        if ( lhsComp.compareTo( rhsComp ) > 0 )
        {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
