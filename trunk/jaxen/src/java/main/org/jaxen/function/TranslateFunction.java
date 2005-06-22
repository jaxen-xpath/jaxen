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


package org.jaxen.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

/**
 * <p>
 * <b>4.2</b>
 * <code><i>string</i> translate(<i>string</i>,<i>string</i>,<i>string</i>)</code>
 * 
 * <blockquote src="http://www.w3.org/TR/xpath#function-translate">
 * <p>
 * The <b><a href="http://www.w3.org/TR/xpath#function-translate">translate</a></b> function
 * returns the first argument string with occurrences of characters in
 * the second argument string replaced by the character at the
 * corresponding position in the third argument string. For example,
 * <code>translate("bar","abc","ABC")</code> returns the string
 * <code>BAr</code>. If there is a character in the second argument
 * string with no character at a corresponding position in the third
 * argument string (because the second argument string is longer than
 * the third argument string), then occurrences of that character in the
 * first argument string are removed. For example,
 * <code>translate("--aaa--","abc-","ABC")</code> returns
 * <code>"AAA"</code>. If a character occurs more than once in the
 * second argument string, then the first occurrence determines the
 * replacement character. If the third argument string is longer than
 * the second argument string, then excess characters are ignored.
 * </p>
 * 
 * <blockquote> <b>NOTE: </b>The <b>translate</b> function is not a
 * sufficient solution for case conversion in all languages. A future
 * version of XPath may provide additional functions for case
 * conversion.</blockquote>
 * 
 * </blockquote>
 * 
 * @author Jan Dvorak ( jan.dvorak @ mathan.cz )
 * 
 * @see <a href="http://www.w3.org/TR/xpath#function-translate"
 *      target="_top">Section 4.2 of the XPath Specification</a>
 */
public class TranslateFunction implements Function
{

     /* The translation is done thru a HashMap. Performance tip (for anyone
      * who needs to improve the performance of this particular function):
      * Cache the HashMaps, once they are constructed. */
    
    
    /** Returns a copy of the first argument in which
     * characters found in the second argument are replaced by
     * correpsonding characters from the third argument.
     *
     * @param context the context at the point in the
     *         expression when the function is called
     * @param args a list that contains exactly three items
     * 
     * @return a <code>String</code> built from <code>args.get(0)</code> 
     *     in which occurrences of characters in<code>args.get(1)</code> 
     *     are replaced by the correpsonding characters in <code>args.get(2)</code> 
     * 
     * @throws FunctionCallException if <code>args</code> does not have exactly three items
     */
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
    if (args.size() == 3)
        {
            return evaluate( args.get(0),
                             args.get(1),
                             args.get(2),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "translate() requires three arguments." );
    }

    /** Returns a copy of <code>strArg</code> in which
     * characters found in <code>fromArg</code> are replaced by
     * corresponding characters from <code>toArg</code>.
     * If necessary each argument is first converted to it string-value
     * as if by the XPath string() function.
     * 
     * @param strArg the base string
     * @param fromArg the characters to be replaced
     * @param toArg the characters they will be replaced by
     * @param nav the <code>Navigator</code> used to calculate the string-values of the arguments.
     * 
     * @return a copy of <code>strArg</code> in which
     *  characters found in <code>fromArg</code> are replaced by
     *  corresponding characters from <code>toArg</code>
     * 
     */    public static String evaluate(Object strArg,
                                  Object fromArg,
                                  Object toArg,
                                  Navigator nav)
    {
        String inStr = StringFunction.evaluate( strArg, nav );
        String fromStr = StringFunction.evaluate( fromArg, nav );
        String toStr = StringFunction.evaluate( toArg, nav );
    
        // Initialize the mapping in a HashMap
        Map charMap = new HashMap();
        int fromLen = fromStr.length();
        int toLen = toStr.length();
        for ( int i = 0; i < fromLen; ++i ) {
            String cFrom = fromStr.substring( i, i+1 ).intern();
            if ( charMap.containsKey( cFrom ) ) {
                // We've seen the character before, ignore
                continue;
            }
            if ( i < toLen ) {
                Character cTo = new Character( toStr.charAt( i ) );
                // Will change
                charMap.put( cFrom, cTo );
            } 
            else {
                // Will delete
                charMap.put( cFrom, null );
            }
        }

        // Process the input string thru the map
        StringBuffer outStr = new StringBuffer( inStr.length() );
        int inLen = inStr.length();
        for ( int i = 0; i < inLen; ++i ) {
            String cIn = inStr.substring( i, i+1 );
            if ( charMap.containsKey( cIn ) ) {
                Character cTo = (Character) charMap.get( cIn );
                if ( cTo != null ) {
                    outStr.append( cTo.charValue() );
                }
            } 
            else {
                outStr.append( cIn );
            }
        }
    
        return new String( outStr );
    }
     
}

