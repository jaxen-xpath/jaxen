package org.jaxen.function.ext;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

import java.util.List;
import java.util.Locale;

/**
 * <p><code><i>string</i> upper-case(<i>string</i>)</code>
 *
 * This function can take a second parameter of the 
 * <code>Locale</code> to use for the String conversion.
 * </p>
 *
 * <p>
 * For example
 *
 * <code>upper-case( /foo/bar )</code>
 * <code>upper-case( /foo/@name, $myLocale )</code>
 * </p>
 *
 * @author mark wilson (markw@wilsoncom.de)
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public class UpperFunction extends LowerFunction
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        int size = args.size();
        if (size > 0)
        {
            Object text = args.get(0);
            Locale locale = null;
            if (size > 1)
            {  
                locale = getLocale( args.get(1) );
            }
            return evaluate( text, locale, context.getNavigator() );
        }
        throw new FunctionCallException( "upper-case() requires at least one argument." );
    }

    /**
     * Converts the given string value to upper case using an optional Locale
     * 
     * @param strArg the value which gets converted to a String
     * @param locale the Locale to use for the conversion or null if the
     *          default should be used
     * @param nav the Navigator to use
     */
    public static String evaluate(Object strArg,
                                  Locale locale,
                                  Navigator nav)
    {

        String str   = StringFunction.evaluate( strArg,
                                                nav );
        if (locale != null)
        {
            return str.toUpperCase(locale);
        }
        else 
        {
            return  str.toUpperCase();
        }
    }
}
