
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;

/**
 * <p><b>4.4</b> <code><i>number</i> floor(<i>number</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class FloorFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(1),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "floor() requires one argument." );
    }

    public static Double evaluate(Object obj,
                                  Navigator nav)
    {
        Number value = NumberFunction.evaluate( obj,
                                                nav );

        return new Double( Math.floor( value.doubleValue() ) );
    }
}

