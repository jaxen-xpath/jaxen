package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;

/**
 * <p><b>4.3</b> <code><i>boolean</i> not(<i>boolean</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class NotFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(0), context.getNavigator() );
        }

        throw new FunctionCallException( "not() requires one argument." );
    }

    public static Boolean evaluate(Object obj, Navigator nav)
    {
        return ( ( BooleanFunction.evaluate( obj, nav ).booleanValue() )
                 ? Boolean.FALSE
                 : Boolean.TRUE
                 );
    }
}
