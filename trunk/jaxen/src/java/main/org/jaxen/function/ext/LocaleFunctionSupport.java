package org.jaxen.function.ext;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

/**
 * <p>An abastract base class for Locale-specific extension 
 * functions. This class provides convenience methods that
 * can be inherited, specifically to find a Locale from
 * an XPath function argument value.
 * </p>
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public abstract class LocaleFunctionSupport implements Function
{

    /** 
     * Attempts to convert the given function argument value 
     * into a Locale either via casting, 
     * extracting it from a List
     * or looking up the named Locale using reflection.
     *
     * @param value is either a Locale, a List containing a Locale
     *      or a String containing the name of a Locale
     *      as defined by the Locale static members.
     *
     * @return the Locale for the value or null if one could 
     *      not be deduced
     */
    protected Locale getLocale(Object value, Navigator navigator) 
    {
        if (value instanceof Locale)
        {
            return (Locale) value;
        }
        else if(value instanceof List)
        {
            List list = (List) value;
            if ( ! list.isEmpty() ) 
            {
                return getLocale( list.get(0), navigator );
            }
        }
        else {
            String text = StringFunction.evaluate( value, navigator );
            if (text != null && text.length() > 0) 
            {
                return findLocale( text );
            }
        }
        return null;
    }
    
    /** 
     * Tries to find a static Locale instance by name
     *
     * @parem name is the name of the Locale such as 'FRANCE'
     * @return the Locale for the given name or null if one could not
     *      be found or some wierd reflection error occurs.
     */
    protected Locale findLocale(String name) {
        try 
        {
            Field field = Locale.class.getField(name);
            if (field != null) 
            {
                return (Locale) field.get(null);
            }
        }
        catch (Exception e) 
        {
            // ignore any exceptions
        }
        return null;
    }
}
