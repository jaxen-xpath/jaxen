
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;

/**
 * <p><b>4.2</b> <code><i>string</i> substring-after(<i>string</i>,<i>string</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class SubstringAfterFunction implements Function
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

        throw new FunctionCallException( "substring-after() requires two arguments." );
    }

    public static String evaluate(Object strArg,
                                  Object matchArg,
                                  Navigator nav)
    {
        String str   = StringFunction.evaluate( strArg,
                                                nav );

        String match = StringFunction.evaluate( matchArg,
                                                nav );
    
        int loc = str.indexOf(match);

        if ( loc < 0 )
        {
            return "";
        }

        return str.substring(loc+match.length());
    }
}
