
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.Navigator;
import org.jaxen.FunctionCallException;

import java.util.List;

public class DocumentFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            Navigator nav = context.getNavigator();

            String    url = StringFunction.evaluate( args.get( 0 ),
                                                     nav );

            return evaluate( url,
                             nav );
        }

        throw new FunctionCallException( "false() requires no arguments." );
    }

    public static Object evaluate(String url,
                                  Navigator nav)
    {
        return nav.getDocument( url );
    }
}
