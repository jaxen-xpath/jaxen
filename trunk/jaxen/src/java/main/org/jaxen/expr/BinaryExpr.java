// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

public interface BinaryExpr extends Expr
{
    Expr getLHS();
    Expr getRHS();
}
