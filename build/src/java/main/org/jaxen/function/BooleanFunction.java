
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.expr.DefaultExpr;

import java.util.List;

/**
 *  <p><b>4.3</b> <code><i>boolean</i> boolean(<i>object</i>)</code> 
 *  
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class BooleanFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if ( args.size() == 1 )
        {
            return evaluate( args.get(0) );
        }

        throw new FunctionCallException("boolean() requires one argument");
    }

    public static Boolean evaluate(Object obj)
    {
        return DefaultExpr.convertToBoolean( obj );
    }
}
