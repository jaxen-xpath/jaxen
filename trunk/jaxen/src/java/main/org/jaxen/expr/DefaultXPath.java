// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;

import java.util.List;

public class DefaultXPath implements XPath
{
    private Expr rootExpr;

    public DefaultXPath(Expr rootExpr)
    {
        this.rootExpr = rootExpr;
    }

    public Expr getRootExpr()
    {
        return this.rootExpr;
    }

    public void setRootExpr(Expr rootExpr)
    {
        this.rootExpr = rootExpr;
    }

    public String toString()
    {
        return "[(DefaultXPath): " + getRootExpr() + "]";
    }

    public String getText()
    {
        return getRootExpr().getText();
    }

    public void simplify()
    {
        setRootExpr( getRootExpr().simplify() );
    }

    public List asList(Context context) throws JaxenException
    {
        return DefaultExpr.convertToList( getRootExpr().evaluate( context ) );
    }
}
