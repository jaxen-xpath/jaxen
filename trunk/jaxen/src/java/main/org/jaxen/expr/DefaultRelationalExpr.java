// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import java.util.List;
import java.util.Iterator;
import org.jaxen.Context;
import org.jaxen.Navigator;
import org.jaxen.JaxenException;
import org.jaxen.function.NumberFunction;

abstract class DefaultRelationalExpr extends DefaultTruthExpr 
  {
  public DefaultRelationalExpr( Expr lhs, Expr rhs )
    {
    super( lhs, rhs );
    }
  
  public String toString()
    {
    return "[(DefaultRelationalExpr): " + getLHS() + ", " + getRHS() + "]";
    }

  public Object evaluate( Context context ) throws JaxenException
    {
    Object lhsValue = getLHS().evaluate( context );
    Object rhsValue = getRHS().evaluate( context );
    Navigator nav = context.getNavigator();

    if( bothAreSets( lhsValue, rhsValue ) )
      {
      return evaluateSetSet( (List) lhsValue, (List) rhsValue, nav );
      }

    if( eitherIsSet( lhsValue, rhsValue ) )
      {
      if( isSet( lhsValue ) )
        {        
        return evaluateSetSet( (List) lhsValue, convertToList( rhsValue ), nav );              
        }
      else
        {
        return evaluateSetSet( convertToList( lhsValue ), (List) rhsValue, nav );              
        }
      }
    
    return evaluateObjectObject( lhsValue, rhsValue, nav ) ? Boolean.TRUE : Boolean.FALSE;
    }

  private Object evaluateSetSet( List lhsSet, List rhsSet, Navigator nav )
    {
    if( setIsEmpty( lhsSet ) || setIsEmpty( rhsSet ) ) // return false if either is null or empty
      {
      return Boolean.FALSE;
      }    
    
    for( Iterator lhsIterator = lhsSet.iterator(); lhsIterator.hasNext(); )
      {
      Object lhs = lhsIterator.next();        
      
      for( Iterator rhsIterator = rhsSet.iterator(); rhsIterator.hasNext(); )
        {
        Object rhs = rhsIterator.next();
        
        if( evaluateObjectObject( lhs, rhs, nav ) )
          {
          return Boolean.TRUE;
          }
        }
      }      
    
    return Boolean.FALSE;
    }
  
  private boolean evaluateObjectObject( Object lhs, Object rhs, Navigator nav )
    {
    if( lhs == null || rhs == null )
      {
      return false;
      }
    
    Double lhsNum = NumberFunction.evaluate( lhs, nav );
    Double rhsNum = NumberFunction.evaluate( rhs, nav );      
    
    if( NumberFunction.isNaN( lhsNum ) || NumberFunction.isNaN( rhsNum ) )
      {
      return false;
      }
    
    return evaluateDoubleDouble( lhsNum, rhsNum );
    }
  
  protected abstract boolean evaluateDoubleDouble( Double lhs, Double rhs );    
  }

