// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.util.SingleObjectIterator;

import java.util.Set;
import java.util.Iterator;
import java.util.Collections;

abstract class DefaultArithExpr extends DefaultBinaryExpr 
{
    public DefaultArithExpr(Expr lhs,
                            Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String toString()
    {
        return "[(DefaultArithExpr): " + getLHS() + ", " + getRHS() + "]";
    }
}
