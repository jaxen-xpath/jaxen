
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;

/**
 * <p><b>4.2</b> <code><i>number</i> string-length(<i>string</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class StringLengthFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 0)
        {
            return evaluate( context.getNodeSet(),
                             context.getNavigator() );
        } 
        else if (args.size() == 1)
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "string-length() requires one argument." );
    }

    public static Number evaluate(Object obj, Navigator nav )
    {
        String str = StringFunction.evaluate( obj, nav );

        return new Double(str.length());
    }
}
