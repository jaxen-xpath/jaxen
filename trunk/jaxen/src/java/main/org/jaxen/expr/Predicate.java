// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import java.io.Serializable;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;

public interface Predicate extends Serializable
{
    Expr getExpr();
    void setExpr(Expr expr);

    void simplify();

    String getText();

    Object evaluate(Context context) throws JaxenException;

}
