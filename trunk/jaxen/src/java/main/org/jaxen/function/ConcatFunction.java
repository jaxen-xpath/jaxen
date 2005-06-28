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
 *      Jaxen Project <http://www.jaxen.org/>."
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

import java.util.Iterator;
import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

/**
 * <b>4.2</b> <code><i>string</i> concat(<i>string</i>,<i>string</i>,<i>string*</i>)</code> 
 * <p>
 * Concatenates its arguments and returns the resulting string.
 * </p>
 *  
 *  @author bob mcwhirter (bob@werken.com)
 * 
 * @see <a href="http://www.w3.org/TR/xpath#function-concat">Section 4.2 of the XPath Specification</a>
 */
public class ConcatFunction implements Function
{

    /**
     * Create a new <code>ConcatFunction</code> object.
     */
    public ConcatFunction() {}
    
    /** 
     * Concatenates the arguments and returns the resulting string.
     * Non-string items are first converted to strings as if by the 
     * XPath <code>string()<code> function.
     *
     * @param context the context at the point in the
     *         expression when the function is called
     * @param args the list of strings to be concatenated
     * 
     * @return a <code>String</code> containing the concatenation of the items 
     *     of <code>args</code>
     * 
     * @throws FunctionCallException if <code>args</code> has less than two items
     */
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if ( args.size() >= 2 )
        {
            return evaluate( args,
                             context.getNavigator() );
        }

        throw new FunctionCallException("concat() requires at least two arguments");
    }

    /** 
     * Converts each item in the list to a string and returns the 
     * concatenation of these strings.
     * If necessary, each item is first converted to a <code>String</code>
     * as if by the XPath <code>string()</code> function.
     * 
     * @param list the items to be concatenated
     * @param nav ignored
     * 
     * @return the concatenation of the arguments
     */
   public static String evaluate(List list,
                                  Navigator nav)
    {
        StringBuffer result = new StringBuffer();
        Iterator argIter = list.iterator();
        while ( argIter.hasNext() )
        {
            result.append( StringFunction.evaluate( argIter.next(),
                                                    nav ) );
        }
    
        return result.toString();
    }
}
