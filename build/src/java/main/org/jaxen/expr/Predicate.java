// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.ContextSupport;

public interface Predicate 
{
    Expr getExpr();
    void setExpr(Expr expr);

    void simplify();

    String getText();

    Object evaluate(Context context);

    boolean matches(Object node,
                    int position,
                    int contextSize,
                    ContextSupport support);
}
