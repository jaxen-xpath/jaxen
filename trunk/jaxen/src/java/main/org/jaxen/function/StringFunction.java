
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

import java.util.List;
import java.util.Iterator;

/**
 * <p><b>4.2</b> <code><i>string</i> string(<i>object</i>)</code>
 *
 * @author bob mcwhirter (bob @ werken.com)
 */
public class StringFunction implements Function
{
    
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        int size = args.size();

        if ( size == 0 )
        {
            return evaluate( context.getNodeSet(),
                             context.getNavigator() );
        }
        else if ( size == 1 )
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "string() requires one argument." );
    }
    
    public static String evaluate(Object obj,
                                  Navigator nav)
    {
        if ( obj == null )
        {
            return "";
        }
        if ( obj instanceof String)
        {
            return (String) obj;
        }
        else if ( nav.isElement( obj ) )
        {
            return nav.getElementStringValue( obj );
        }
        else if ( nav.isAttribute( obj ) )
        {
            return nav.getAttributeStringValue( obj );
        }
        else if ( nav.isText( obj ) )
        {
            return nav.getTextStringValue( obj );
        }
        else if ( nav.isProcessingInstruction( obj ) )
        {
            return nav.getProcessingInstructionData( obj );
        }
        else if ( nav.isComment( obj ) )
        {
            return nav.getCommentStringValue( obj );
        }
        else if ( nav.isNamespace( obj ) )
        {
            return nav.getNamespaceStringValue( obj );
        }
        else if (obj instanceof List)
        {
            List list = (List) obj;
            int  size = list.size();

            if ( size > 0 ) {
                // the XPath string() function only returns the
                // string value of the first node in a nodeset

                return evaluate( list.get(0),
                                 nav );
            }
        }
        else if ( obj instanceof Boolean )
        {
            return obj.toString();
        }
        else if ( obj instanceof Integer )
        {
            return obj.toString();
        }
        else if ( obj instanceof Double )
        {
            Double num = (Double) obj;
            
            if ( num.isNaN() )
            {
                return "NaN";
            }
            else if ( num.isInfinite() )
            {
                if ( num.intValue() < 0 )
                {
                    return "-Infinity";
                }
                else
                {
                    return "Infinity";
                }
            }
            else if( num.floatValue() == num.intValue() ) // strip .0
              {
              return Integer.toString( num.intValue() );
              }
            else
            {
                return num.toString();
            }
        }

        return "";
    }
}
