// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.util.SingleObjectIterator;

import java.util.List;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Collections;

public abstract class DefaultExpr implements Expr
{
    public Expr simplify()
    {
        return this;
    }

    static public Iterator convertToIterator(Object obj)
    {
        if ( obj instanceof Iterator )
        {
            return (Iterator) obj;
        }

        if ( obj instanceof List )
        {
            return ((List)obj).iterator();
        }

        return new SingleObjectIterator( obj );
    }

    static public List convertToList(Object obj)
    {
        if ( obj instanceof List )
        {
            return (List) obj;
        }

        return Collections.singletonList( obj );
    }

    /*
    static public String convertToString(Object obj)
    {
        if ( obj instanceof String )
        {
            return (String) obj;
        }
        else if ( obj instanceof Boolean )
        {
            return obj.toString();
        }
        else if ( obj instanceof Integer )
        {
            return obj.toString();
        }
        else if ( obj instanceof Double )
        {
            Double num = (Double) obj;

            if ( num.isNaN() )
            {
                return "NaN";
            }
            else if ( num.isInfinite() )
            {
                if ( num.intValue() < 0 )
                {
                    return "-Infinity";
                }
                else
                {
                    return "Infinity";
                }
            }
        }
        else if ( obj instanceof List )
        {
            Iterator iter = ((List)obj).iterator();

            return convertToString( iter );
        }
        else if ( obj instanceof Iterator )
        {
            if ( ! ((Iterator)obj).hasNext() )
            {
                return "";
            }

            Object first = ((Iterator)obj).next();

            return convertToString( first );
        }

        return "";
    }

    static public Number convertToNumber(Object obj)
    {
        if ( obj instanceof Number )
        {
            return (Number) obj;
        }
        else if ( obj instanceof Boolean )
        {
            if ( obj == Boolean.TRUE )
            {
                return new Integer( 1 );
            }
            else
            {
                return new Integer( 0 );
            }
        }
        else if ( obj instanceof String )
        {
            try
            {
                Double doubleValue = new Double( (String) obj );

                return doubleValue;
            }
            catch (NumberFormatException e)
            {
                return new Double( Double.NaN );
            }
        }
        else if ( obj instanceof List
                  ||
                  obj instanceof Iterator )
        {
            return convertToNumber( convertToString( obj ) );
        }
        
        return new Double( Double.NaN );
    }

    static public Boolean convertToBoolean(Object obj)
    {
        if ( obj instanceof Boolean )
        {
            return (Boolean) obj;
        }
        else if ( obj instanceof Number )
        {
            if ( ((Number)obj).doubleValue() == Double.NaN
                 ||
                 ((Number)obj).doubleValue() == 0 )
            {
                return Boolean.FALSE;
            }

            return Boolean.TRUE;
        }
        else if ( obj instanceof String )
        {
            return ( ((String)obj).length() > 0
                     ? Boolean.TRUE
                     : Boolean.FALSE );
        }
        else if ( obj instanceof List )
        {
            return ( ((List)obj).isEmpty()
                     ? Boolean.FALSE
                     : Boolean.TRUE );
        }
        else if ( obj instanceof Iterator )
        {
            return ( ((Iterator)obj).hasNext()
                     ? Boolean.TRUE
                     : Boolean.FALSE );
        }

        return Boolean.FALSE;
    }
    */
}
