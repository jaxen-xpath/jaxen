
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;
import java.util.Iterator;

/**
 * <p><b>4.4</b> <code><i>number</i> sum(<i>node-set</i>)</code> 
 * 
 * @author bob mcwhirter (bob @ werken.com)
 */
public class SumFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {

        if (args.size() == 1)
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "sum() requires one argument." );
    }

    public static Double evaluate(Object obj,
                                  Navigator nav)
    {
        double sum = 0;

        if (obj instanceof List)
        {
            Iterator nodeIter = ((List)obj).iterator();

            while ( nodeIter.hasNext() )
            {
                sum += NumberFunction.evaluate( nodeIter.next(),
                                                nav ).doubleValue();
            }
        }
        else
        {
            sum += NumberFunction.evaluate( obj,
                                            nav ).doubleValue();
        }

        return new Double(sum);
    }
}
