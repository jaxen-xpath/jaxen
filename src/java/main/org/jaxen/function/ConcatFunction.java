
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;
import java.util.Iterator;

/**
 *  <p><b>4.2</b> <code><i>string</i> concat(<i>string</i>,<i>string</i>,<i>string*</i>)</code> 
 *  
 *  @author bob mcwhirter (bob@werken.com)
 */
public class ConcatFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if ( args.size() >= 2 )
        {
            return evaluate( args,
                             context.getNavigator() );
        }

        throw new FunctionCallException("concat() requires at least two arguments");
    }

    public static String evaluate(List list,
                                  Navigator nav)
    {
        StringBuffer result = new StringBuffer();

        Iterator argIter = list.iterator();

        while ( argIter.hasNext() )
        {

            result.append( StringFunction.evaluate( argIter.next(),
                                                    nav ) );
        }
    
        return result.toString();
    }
}
