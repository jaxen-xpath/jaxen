
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;

import java.util.List;

/**
 * <p><b>4.3</b> <code><i>boolean</i> false()</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class FalseFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 0)
        {
            return evaluate();
        }

        throw new FunctionCallException( "false() requires no arguments." );
    }

    public static Boolean evaluate()
    {
        return Boolean.FALSE;
    }
}
