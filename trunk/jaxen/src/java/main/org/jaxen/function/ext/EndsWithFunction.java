package org.jaxen.function.ext;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

import java.util.List;

/**
 * <p><code><i>boolean</i> ends-with(<i>string</i>,<i>string</i>)</code>
 *
 * @author mark wilson (markw @ wilsoncom.de)
 */
public class EndsWithFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 2)
        {
            return evaluate( args.get(0),
                             args.get(1),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "ends-with() requires two arugments." );
    }

    public static Boolean evaluate(Object strArg,
                                   Object matchArg,
                                   Navigator nav)
    {
        String str   = StringFunction.evaluate( strArg,
                                                nav );

        String match = StringFunction.evaluate( matchArg,
                                                nav );

        return ( str.endsWith(match)
                 ? Boolean.TRUE
                 : Boolean.FALSE
                 );
    }
}
