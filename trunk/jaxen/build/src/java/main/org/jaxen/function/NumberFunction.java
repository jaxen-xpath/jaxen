package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import org.jaxen.expr.DefaultExpr;

import java.util.List;

/**
 * <p><b>4.4</b> <code><i>number</i> number(<i>object</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class NumberFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "number() requires one argument." );
    }

    public static Number evaluate(Object obj,
                                  Navigator nav)
    {
        if (obj instanceof Number)
        {
            return (Number) obj;
        }
        else
        {
            return DefaultExpr.convertToNumber( StringFunction.evaluate( obj,
                                                                         nav ) );
        }
    }
}
