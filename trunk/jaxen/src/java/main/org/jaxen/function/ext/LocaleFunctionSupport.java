package org.jaxen.function.ext;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

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
     * Tries to find a Locale instance by name using xml:lang style encodings
     * like 'en', 'en-US', 'en-US-Brooklyn'.
     *
     * @param localeText is the xml:lang encoding of a Locale
     * @return the Locale for the given text or null if one could not
     *      be found 
     */
    protected Locale findLocale(String localeText) {
        StringTokenizer enum = new StringTokenizer( localeText, "-" );
        if (enum.hasMoreTokens()) 
        {
            String language = enum.nextToken();
            if (! enum.hasMoreTokens()) 
            {                
                return findLocaleForLanguage(language);
            }
            else 
            {
                String country = enum.nextToken();
                if (! enum.hasMoreTokens()) 
                {
                    return new Locale(language, country);
                }
                else 
                {
                    String variant = enum.nextToken();
                    return new Locale(language, country, variant);
                }
            }
        }
        return null;
    }
    
    /** 
     * Finds the locale with the given language name with no country
     * or variant, such as Locale.ENGLISH or Locale.FRENCH
     *
     * @param language is the language code to look for
     * @return the Locale for the given language or null if one could not
     *      be found 
     */
    protected Locale findLocaleForLanguage(String language) {
        Locale[] locales = Locale.getAvailableLocales();
        for ( int i = 0, size = locales.length; i < size; i++ ) 
        {
            Locale locale = locales[i];
            if ( language.equals( locale.getLanguage() ) ) 
            {
                String country = locale.getCountry();
                if ( country == null || country.length() == 0 ) 
                {
                    String variant = locale.getVariant();
                    if ( variant == null || variant.length() == 0 ) 
                    {
                        return locale;
                    }
                }
            }
        }    
        return null;
    }
}
