// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

public interface FunctionCallExpr extends Expr
{
    void addParameter(Expr parameter);
}
