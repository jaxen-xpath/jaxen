// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;
import org.jaxen.util.SingleObjectIterator;

import java.util.Set;
import java.util.Iterator;
import java.util.Collections;

abstract class DefaultArithExpr extends DefaultBinaryExpr 
{
    public DefaultArithExpr(Expr lhs,
                            Expr rhs)
    {
        super( lhs,
               rhs );
    }

    public String toString()
    {
        return "[(DefaultArithExpr): " + getLHS() + ", " + getRHS() + "]";
    }
    
    public void assertInteger( Number number ) throws JaxenException
      {
      if( number.doubleValue() != number.intValue() )
        {
        throw new JaxenException( number + " is not an integer" );
        }
      }
}
