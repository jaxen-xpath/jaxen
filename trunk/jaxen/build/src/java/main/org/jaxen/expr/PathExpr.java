// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

public interface PathExpr extends Expr
{
    Expr getFilterExpr();
    void setFilterExpr(Expr filterExpr);

    LocationPath getLocationPath();
}
