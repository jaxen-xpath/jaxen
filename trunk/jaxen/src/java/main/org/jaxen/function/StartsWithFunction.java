
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;

/**
 * <p><b>4.2</b> <code><i>boolean</i> starts-with(<i>string</i>,<i>string</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class StartsWithFunction implements Function
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

        throw new FunctionCallException( "starts-with() requires two arugments." );
    }

    public static Boolean evaluate(Object strArg,
                                   Object matchArg,
                                   Navigator nav)
    {
        String str   = StringFunction.evaluate( strArg,
                                                nav );

        String match = StringFunction.evaluate( matchArg,
                                                nav );

        return ( str.startsWith(match)
                 ? Boolean.TRUE
                 : Boolean.FALSE
                 );
    }
}
