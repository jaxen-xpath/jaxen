
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;

/**
 * <p><b>4.4</b> <code><i>number</i> round(<i>number</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class RoundFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "round() requires one argument." );
    }

    public static Number evaluate(Object obj,
                                  Navigator nav)
    {
        Number n = NumberFunction.evaluate( obj,
                                            nav );

        if ( n instanceof Double )
        {
            Double d = (Double) n;

            if (d.isNaN() || d.isInfinite())
            {
                return d;
            }
        }

        double value = n.doubleValue();

        return new Double( Math.round( value ) );
    }
}
