
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;

/**
 * <p><b>4.2</b> <code><i>string</i> substring(<i>string</i>,<i>number</i>,<i>number?</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class SubstringFunction implements Function
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
        else if (args.size() == 3)
        {
            return evaluate( args.get(0),
                             args.get(1),
                             args.get(2),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "substring() requires two or three arguments." );
    }

    public static String evaluate(Object strArg,
                                  Object startArg,
                                  Navigator nav)
    {

        String str   = StringFunction.evaluate( strArg,
                                                nav );

        int    start = RoundFunction.evaluate( NumberFunction.evaluate( startArg,
                                                                        nav),
                                               nav ).intValue();

        start -= 1;

        return str.substring(start);

    }

    public static String evaluate(Object strArg,
                                  Object startArg,
                                  Object lenArg,
                                  Navigator nav)
    {

        String str = StringFunction.evaluate( strArg,
                                              nav );

        int start = RoundFunction.evaluate( NumberFunction.evaluate( startArg,
                                                                     nav ),
                                            nav ).intValue();
    
        int len = RoundFunction.evaluate( NumberFunction.evaluate( lenArg,
                                                                   nav ),
                                          nav ).intValue();

        // Java Strings start at 0 rather than 1
        start -= 1;
    
        int end = start + len;

        return str.substring(start,
                             end);

    }
}
