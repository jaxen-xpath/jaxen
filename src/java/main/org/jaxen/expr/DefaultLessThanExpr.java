package org.jaxen.expr;

class DefaultLessThanExpr extends DefaultRelationalExpr
  {
  public DefaultLessThanExpr( Expr lhs, Expr rhs )
    {
    super( lhs, rhs );
    }

  public String getOperator()
    {
    return "<";
    }

  protected boolean evaluateDoubleDouble( Double lhs, Double rhs )
    {
    return lhs.compareTo( rhs ) < 0;
    }    
  }
