/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows 
 *    these conditions in the documentation and/or other materials 
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 * 
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project (http://www.jaxen.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id$
 */

package org.jaxen.function.ext;

import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.jaxen.Function;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

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
