// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import java.util.List;
import java.util.Iterator;
import org.jaxen.Context;
import org.jaxen.Navigator;
import org.jaxen.JaxenException;
import org.jaxen.function.StringFunction;
import org.jaxen.function.BooleanFunction;
import org.jaxen.function.NumberFunction;

abstract class DefaultEqualityExpr extends DefaultTruthExpr 
  {
  public DefaultEqualityExpr( Expr lhs, Expr rhs )
    {
    super( lhs, rhs );
    }

  public String toString()
    {
    return "[(DefaultEqualityExpr): " + getLHS() + ", " + getRHS() + "]";
    }
  
  public Object evaluate( Context context ) throws JaxenException
    {
    Object lhsValue = getLHS().evaluate( context );
    Object rhsValue = getRHS().evaluate( context );
    
    if( lhsValue == null || rhsValue == null )
      {
      return Boolean.FALSE;
      }
    
    Navigator nav = context.getNavigator();

    if( bothAreSets( lhsValue, rhsValue ) )
      {
      return evaluateSetSet( (List) lhsValue, (List) rhsValue, nav );
      }
    else if ( eitherIsSet( lhsValue, rhsValue ) )
      {
      if ( isSet( lhsValue ) )
        {
        return evaluateSetSet( (List) lhsValue, convertToList( rhsValue ), nav );                
        }
      else
        {
        return evaluateSetSet( convertToList( lhsValue ), (List) rhsValue, nav );                                
        }
      }  
    else
      {
      return evaluateObjectObject( lhsValue, rhsValue, nav ) ? Boolean.TRUE : Boolean.FALSE;
      }    
    }
  
  private Boolean evaluateSetSet( List lhsSet, List rhsSet, Navigator nav )
    {
    if( setIsEmpty( lhsSet ) || setIsEmpty( rhsSet ) )
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
    if( eitherIsBoolean( lhs, rhs ) )
      {            
      return evaluateObjectObject( BooleanFunction.evaluate( lhs, nav ),
                                   BooleanFunction.evaluate( rhs, nav ) );
      }
    else if( eitherIsNumber( lhs, rhs ) )
      {
      return evaluateObjectObject( NumberFunction.evaluate( lhs,
                                                            nav ),
                                   NumberFunction.evaluate( rhs,
                                                            nav ) );                                              
      }
    else
      {
      return evaluateObjectObject( StringFunction.evaluate( lhs,
                                                            nav ),
                                   StringFunction.evaluate( rhs,
                                                            nav ) );
      }
    }
  
  protected abstract boolean evaluateObjectObject( Object lhs, Object rhs );
  }
