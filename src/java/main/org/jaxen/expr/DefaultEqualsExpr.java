// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.function.NumberFunction;

class DefaultEqualsExpr extends DefaultEqualityExpr 
  {
  public DefaultEqualsExpr( Expr lhs, Expr rhs )
    {
    super( lhs, rhs );
    }

  public String getOperator()
    {
    return "=";
    }

  public String toString()
    {
    return "[(DefaultEqualsExpr): " + getLHS() + ", " + getRHS() + "]";
    }
  
  protected boolean evaluateObjectObject( Object lhs, Object rhs )
    {
    if( eitherIsNumber( lhs, rhs ) )
      {
      if( NumberFunction.isNaN( (Double) lhs ) || NumberFunction.isNaN( (Double) rhs ) )
        {
        return false;
        }
      }
    
    return lhs.equals( rhs );
    }
  }

