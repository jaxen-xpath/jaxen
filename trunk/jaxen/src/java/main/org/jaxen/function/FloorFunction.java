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

import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

/**
 * <p><b>4.4</b> <code><i>number</i> floor(<i>number</i>)</code></p> 
 * 
 * 
 * <blockquote src="http://www.w3.org/TR/xpath">
 * The floor function returns the largest (closest to positive infinity) 
 * number that is not greater than the argument and that is an integer....
 * If the argument is NaN, then NaN is returned. 
 * If the argument is positive infinity, then positive infinity is returned. 
 * If the argument is negative infinity, then negative infinity is returned. 
 * If the argument is positive zero, then positive zero is returned. 
 * If the argument is negative zero, then negative zero is returned. 
 * If the argument is greater than zero, but less than 1, then positive zero is returned.
 * </blockquote>
 * 
 * @author bob mcwhirter (bob @ werken.com)
 * 
 * @see <a href="http://www.w3.org/TR/xpath#function-floor" target="_top">Section 4.4 of the XPath Specification</a>
 * @see <a href="http://www.w3.org/1999/11/REC-xpath-19991116-errata/"  target="_top">XPath Errata</a>
 */
public class FloorFunction implements Function
{

    /**
     * Create a new <code>FloorFunction</code> object.
     */
    public FloorFunction() {}
    
    /** Returns the largest integer less than or equal to a number.
     *
     * @param context the context at the point in the
     *         expression when the function is called
     * @param args a list with exactly one item which will be converted to a 
     *     <code>Double</code> as if by the XPath <code>number()</code> function
     * 
     * @return a <code>Double</code> containing the largest integer less than or equal
     *     to <code>args.get(0)</code>
     * 
     * @throws FunctionCallException if <code>args</code> has more or less than one item
     */
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "floor() requires one argument." );
    }

    /** Returns the largest integer less than or equal to the argument.
     * If necessary, the argument is first converted to a <code>Double</code>
     * as if by the XPath <code>number()</code> function.
     * 
     * @param obj the object whose floor is returned
     * @param nav ignored
     * 
     * @return a <code>Double</code> containing the largest integer less 
     *     than or equal to <code>obj</code>
     */
    public static Double evaluate(Object obj,
                                  Navigator nav)
    {
        Double value = NumberFunction.evaluate( obj,
                                                nav );

        return new Double( Math.floor( value.doubleValue() ) );
    }
}

