// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

abstract class DefaultBinaryExpr extends DefaultExpr implements BinaryExpr
{
    private Expr lhs;
    private Expr rhs;

    public DefaultBinaryExpr(Expr lhs,
                             Expr rhs)
    {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Expr getLHS()
    {
        return this.lhs;
    }

    public Expr getRHS()
    {
        return this.rhs;
    }

    public void setLHS(Expr lhs)
    {
        this.lhs = lhs;
    }

    public void setRHS(Expr rhs)
    {
        this.rhs = rhs;
    }

    public abstract String getOperator();

    public String getText()
    {
        Expr lhs = getLHS();
        Expr rhs = getRHS();

        return "(" + getLHS().getText() + " " + getOperator() + " " + getRHS().getText() + ")";
    }

    public String toString()
    {
        return "[(" + getClass().getName() + "): " + getLHS() + ", " + getRHS() + "]";
    }

    public Expr simplify()
    {
        setLHS( getLHS().simplify() );
        setRHS( getRHS().simplify() );

        return this;
    }
}
