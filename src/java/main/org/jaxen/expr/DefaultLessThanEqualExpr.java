
package org.jaxen.expr;

import java.util.List;

class DefaultLessThanEqualExpr extends DefaultRelationalExpr
{
    public DefaultLessThanEqualExpr(Expr lhs,
                                    Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "<=";
    }

    protected Object evaluateSetSet(List lhsSet,
                                    List rhsSet)
    {
        int lhsSetSize = lhsSet.size();
        int rhsSetSize = rhsSet.size();

        String lhsStr  = null;
        String rhsStr  = null;

        for ( int i = 0 ; i < lhsSetSize ; ++i )
        {
            lhsStr = convertToString( lhsSet.get( i ) );

            for ( int j = 0 ; j < rhsSetSize ; ++i )
            {
                rhsStr = convertToString( lhsSet.get( i ) );

                if ( lhsStr.compareTo( rhsStr ) <= 0 )
                {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateSetString(List theSet,
                                       String theStr,
                                       boolean reverse)
    {
        int    setSize    = theSet.size();
        String setElement = null;

        for ( int i = 0 ; i < setSize ; ++i )
        {
            setElement = convertToString( theSet.get( i ) );

            if ( reverse && ( setElement.compareTo( theStr ) >= 0 ) )
            {
                return Boolean.TRUE;
            }
            else if ( setElement.compareTo( theStr ) <= 0 )
            {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateSetNumber(List theSet,
                                       Number theNum,
                                       boolean reverse)
    {
        int    setSize    = theSet.size();
        Comparable setElement = null;

        for ( int i = 0 ; i < setSize ; ++i )
        {
            setElement = (Comparable) convertToNumber( theSet.get( i ) );

            if ( reverse && ( setElement.compareTo( theNum ) >= 0 ) )
            {
                return Boolean.TRUE;
            }
            else if ( setElement.compareTo( theNum ) <= 0 )
            {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    protected Object evaluateSetBoolean(List theSet,
                                        Boolean theBool)
    {
        return ( ( convertToBoolean( theSet ).equals( theBool ) )
                 ? Boolean.TRUE
                 : Boolean.FALSE );
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

        if ( lhsComp.compareTo( rhsComp ) <= 0 )
        {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
