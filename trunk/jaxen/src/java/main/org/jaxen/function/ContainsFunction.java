package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;

/**
 *  <p><b>4.2</b> <code><i>boolean</i> contains(<i>string</i>,<i>string</i>)</code> 
 *  
 *   @author bob mcwhirter (bob @ werken.com)
 */
public class ContainsFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 2)
        {
            return evaluate(args.get(0),
                            args.get(1),
                            context.getNavigator() );
        }

        throw new FunctionCallException("contains() requires two arguments.");
    }

    public static Boolean evaluate(Object strArg,
                                   Object matchArg,
                                   Navigator nav) 
    {
        String str   = StringFunction.evaluate( strArg,
                                                nav );

        String match = StringFunction.evaluate( matchArg,
                                                nav );

        return ( ( str.indexOf(match) >= 0)
                 ? Boolean.TRUE
                 : Boolean.FALSE
                 );
    }
}
