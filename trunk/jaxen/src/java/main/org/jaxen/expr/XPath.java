// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;

import java.util.List;

public interface XPath 
{
    Expr getRootExpr();
    void setRootExpr(Expr rootExpr);
    String getText();
    void simplify();

    List asList(Context context) throws JaxenException;

}
