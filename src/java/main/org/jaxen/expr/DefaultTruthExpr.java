// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.util.SingleObjectIterator;

import java.util.List;
import java.util.Iterator;
import java.util.Collections;

abstract class DefaultTruthExpr extends DefaultBinaryExpr 
{
    public DefaultTruthExpr(Expr lhs,
                            Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String toString()
    {
        return "[(DefaultTruthExpr): " + getLHS() + ", " + getRHS() + "]";
    }

    protected boolean bothAreSets(Object lhs,
                                  Object rhs)
    {
        return ( lhs instanceof List
                 &&
                 rhs instanceof List );
    }

    protected boolean eitherIsSet(Object lhs,
                                  Object rhs)
    {
        return ( lhs instanceof List
                 ||
                 rhs instanceof List );
    }

    protected boolean isSet(Object obj)
    {
        return ( obj instanceof List );
    }

    protected boolean eitherIsBoolean(Object lhs,
                                      Object rhs)
    {
        return ( lhs instanceof Boolean
                 ||
                 rhs instanceof Boolean );
    }

    protected boolean bothAreBoolean(Object lhs,
                                     Object rhs)
    {
        return ( lhs instanceof Boolean
                 &&
                 rhs instanceof Boolean );
    }

    protected boolean eitherIsNumber(Object lhs,
                                     Object rhs)
    {
        return ( lhs instanceof Number
                 ||
                 rhs instanceof Number );
    }

    protected boolean isNumber(Object obj)
    {
        return ( obj instanceof Number );
    }

    protected boolean isString(Object obj)
    {
        return ( obj instanceof String );
    }

    protected boolean isBoolean(Object obj)
    {
        return ( obj instanceof Boolean );
    }
}

