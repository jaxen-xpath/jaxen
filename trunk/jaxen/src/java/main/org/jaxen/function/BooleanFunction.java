
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
        if ( obj instanceof List )
        {
            List list = (List) obj;
            
            // if it's an empty list, then we have a null node-set -> false            
            if (list.size() == 0)
            {
                return Boolean.FALSE;
            }
            
            // otherwise, unwrap the list and check the primitive
            obj = list.get(0);
        }
        
        // now check for primitive types
        // otherwise a non-empty nodeset is true

        // if it's a Boolean, let it decide
        if ( obj instanceof Boolean )
        {
            return (Boolean) obj;
        }
        // if it's a Number, != 0 -> true
        else if ( obj instanceof Number )
        {
            double d = ((Number) obj).doubleValue();
            if ( d == 0 || d == Double.NaN )
            {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        // if it's a String, "" -> false
        else if ( obj instanceof String )
        {
            return ( ((String)obj).length() > 0
                     ? Boolean.TRUE
                     : Boolean.FALSE );
        }
        else 
        {
            // assume its a node so that this nodeset is non-empty 
            // and so its true
            return ( obj != null ) ? Boolean.TRUE : Boolean.FALSE;
        }

/*
        This is the old way to test nodes
        - don't think this is correct and its certainly less efficient
 
        else {
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
            else if ( obj instanceof String )
            {
                return ( ((String)obj).length() > 0
                         ? Boolean.TRUE
                         : Boolean.FALSE );
            }
            return Boolean.FALSE;
        }
*/
    }
}
