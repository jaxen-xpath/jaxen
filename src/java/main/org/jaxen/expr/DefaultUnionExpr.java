// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.util.LinkedIterator;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

class DefaultUnionExpr extends DefaultBinaryExpr implements UnionExpr
{
    public DefaultUnionExpr(Expr lhs,
                            Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String getOperator()
    {
        return "|";
    }

    public String toString()
    {
        return "[(DefaultUnionExpr): " + getLHS() + ", " + getRHS() + "]";
    }

    public Object evaluate(Context context)
    {
        List results = new ArrayList();

        List lhsResults = convertToList( getLHS().evaluate( context ) );
        List rhsResults = convertToList( getRHS().evaluate( context ) );

        Set unique = new HashSet();

        results.addAll( lhsResults );
        unique.addAll( lhsResults );

        Iterator rhsIter = rhsResults.iterator();
        Object   each    = null;

        while ( rhsIter.hasNext() )
        {
            each = rhsIter.next();

            if ( ! unique.contains( each ) )
            {
                results.add( each );
                unique.add( each );
            }
        }

        return results;
    }
}

