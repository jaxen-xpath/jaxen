package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;

import java.util.List;

/**
 *  <p><b>4.1</b> <code><i>number</i> count(<i>node-set</i>)</code> 
 *  
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class CountFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(0) );
        }

        throw new FunctionCallException( "count() requires one argument." );
    }

    public static Integer evaluate(Object obj)
    {
      if( obj == null )
        {
        return new Integer( 0 );
        }
      
        if (obj instanceof List)
        {
            return new Integer( ((List)obj).size() );
        }
      
        return new Integer( 1 );
    }
}
