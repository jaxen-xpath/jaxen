// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

abstract class DefaultLogicalExpr extends DefaultTruthExpr 
{
    public DefaultLogicalExpr(Expr lhs,
                              Expr rhs)
    {
        super( lhs,
               rhs );
    }
}
