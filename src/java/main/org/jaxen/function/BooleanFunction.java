
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;
import java.util.Iterator;

/**
 *  <p><b>4.3</b> <code><i>boolean</i> boolean(<i>object</i>)</code> 
 *  
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class BooleanFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if ( args.size() == 1 )
        {
            return evaluate( args.get(0), context.getNavigator() );
        }

        throw new FunctionCallException("boolean() requires one argument");
    }

    public static Boolean evaluate(Object obj, Navigator nav)
    {
      
      // convert to String if it's a special object type
        if ( nav.isElement( obj ) )
        {
            obj = nav.getElementStringValue( obj );
        }
        else if ( nav.isAttribute( obj ) )
        {
            obj = nav.getAttributeStringValue( obj );
        }
        else if ( nav.isText( obj ) )
        {
            obj = nav.getTextStringValue( obj );
        }
        
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
}
