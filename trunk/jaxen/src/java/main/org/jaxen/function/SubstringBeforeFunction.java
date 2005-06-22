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

import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

/**
 * <p><b>4.2</b> <code><i>string</i> substring-before(<i>string</i>,<i>string</i>)</code> 
 * 
 * 
 * <blockquote src="http://www.w3.org/TR/xpath#function-substring-before">
 * The substring-before function returns the substring of the first argument 
 * string that precedes the first occurrence of the second argument string 
 * in the first argument string, or the empty string if the 
 * first argument string does not contain the second argument string. 
 * For example, substring-before("1999/04/01","/") returns 1999.
 * </blockquote>
 * 
 * @author bob mcwhirter (bob @ werken.com)
 * 
 * @see <a href="http://www.w3.org/TR/xpath#function-substring-before" target="_top">XPath Specification</a>
 */
public class SubstringBeforeFunction implements Function
{

    /** 
     * Returns the part of the string-value of the first item in <code>args</code>
     * that comes before the string-value of the second item in <code>args</code>;
     * or the empty string if the second string is not a substring of the first string.
     *
     * @param context the context at the point in the
     *         expression when the function is called
     * @param args a list that contains two items
     * 
     * @return a <code>String</code> containing that 
     *     part of the string-value of the first item in <code>args</code>
     *     that comes before the string-value of the second item in <code>args</code>
     * 
     * @throws FunctionCallException if <code>args</code> does not have length two
     */
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 2)
        {
            return evaluate( args.get(0),
                             args.get(1),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "substring-before() requires two arguments." );
    }

    /** 
     * Returns the part of <code>strArg</code> that precedes the first occurence 
     * of <code>matchArg</code>; or the empty string if the 
     * <code>strArg</code> does not contain <code>matchArg</code>
     * 
     * @param strArg the string from which the substring is extracted
     * @param matchArg the string that marks the boundary of the substring
     * @param nav the <code>Navigator</code> used to calculate the string-values of
     *     the first two arguments
     * 
     * @return a <code>String</code> the part of <code>strArg</code> that precedes the first occurence 
     * of <code>matchArg</code>
     * 
     */
    public static String evaluate(Object strArg,
                                  Object matchArg,
                                  Navigator nav)
    {
        String str   = StringFunction.evaluate( strArg,
                                                nav );

        String match = StringFunction.evaluate( matchArg,
                                                nav );

        int loc = str.indexOf(match);

        if ( loc < 0 )
        {
            return "";
        }

        return str.substring(0, loc);

    }
}
