package org.jaxen.function.ext;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

import java.util.List;

/**
 * <p><code><i>string</i> lower-case(<i>string</i>)</code>
 *
 * @author mark wilson (markw@wilsoncom.de)
 */
public class LowerFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }
        throw new FunctionCallException( "lower-case() requires one argument." );
    }

    public static String evaluate(Object strArg,
                                  Navigator nav)
    {

        String str   = StringFunction.evaluate( strArg,
                                                nav );
        return str.toLowerCase();
    }
}
