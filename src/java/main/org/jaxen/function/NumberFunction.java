package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;
import java.util.Iterator;

/**
 * <p><b>4.4</b> <code><i>number</i> number(<i>object</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class NumberFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "number() requires one argument." );
    }

    public static Number evaluate(Object obj,
                                  Navigator nav)
    {
        if (obj instanceof Number)
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
            return evaluate( StringFunction.evaluate( obj,
                                                      nav ),
                             nav );
        }
        
        return new Double( Double.NaN );
    }
}
