
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;
import java.util.StringTokenizer;

/**
 * <p><b>4.2</b> <code><i>string</i> normalize-space(<i>string</i>)</code> 
 * 
 * @author James Strachan (james@metastuff.com)
 */
public class NormalizeSpaceFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() >= 1)
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }
    
        throw new FunctionCallException( "normalize-space() requires one argument" );
    }

    public static String evaluate(Object strArg,
                                  Navigator nav) 
    {
        String str = StringFunction.evaluate( strArg,
                                              nav );

        if ( str.length() <= 1 )
        {
            return str;
        }

        StringBuffer buffer = new StringBuffer();
        boolean      first = true;

        StringTokenizer tokenizer = new StringTokenizer(str);

        while ( tokenizer.hasMoreTokens() )
        {
            if (first)
            {
                first = false;
            }
            else 
            {
                buffer.append(" ");
            }

            buffer.append(tokenizer.nextToken());
        }

        return buffer.toString();
    }
}
