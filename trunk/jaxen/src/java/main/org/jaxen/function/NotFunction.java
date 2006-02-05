/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 * <p><b>4.3</b> <code><i>boolean</i> not(<i>boolean</i>)</code> </p>
 * 
 * 
 * <blockquote src="http://www.w3.org/TR/xpath#function-not">
 * The <b>not</b> function returns true if its argument is false, and false otherwise.
 * </blockquote>
 * 
 * @author bob mcwhirter (bob @ werken.com)
 * @see <a href="http://www.w3.org/TR/xpath#function-not" target="_top">Section 4.3 of the XPath Specification</a>
 */
public class NotFunction implements Function
{

    /**
     * Create a new <code>NotFunction</code> object.
     */
    public NotFunction() {}
    
    /**
     * Returns <code>Boolean.TRUE</code> if the boolean value of 
     * <code>args.get(0)</code> is false, and <code>Boolean.FALSE</code> otherwise.
     * The boolean value is calculated as if by the XPath <code>boolean</code>
     * function. 
     * 
     * @param context the context at the point in the
     *         expression where the function is called
     * @param args a single element list
     * 
     * @return <code>Boolean.TRUE</code> if the boolean value of 
     * <code>obj</code> is false, and <code>Boolean.FALSE</code> otherwise
     * 
     * @throws FunctionCallException if <code>args</code> does not have exactly one argument
     */
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            return evaluate( args.get(0), context.getNavigator() );
        }

        throw new FunctionCallException( "not() requires one argument." );
    }

    /**
     * Returns <code>Boolean.TRUE</code> if the boolean value of 
     * <code>obj</code> is false, and <code>Boolean.FALSE</code> otherwise.
     * The boolean value is calculated as if by the XPath <code>boolean</code>
     * function. 
     * 
     * @param obj the object whose boolean value is inverted
     * @param nav the <code>Navigator</code> used to calculate the boolean value of <code>obj</code>
     * 
     * @return <code>Boolean.TRUE</code> if the boolean value of 
     * <code>obj</code> is false, and <code>Boolean.FALSE</code> otherwise
     */
    public static Boolean evaluate(Object obj, Navigator nav)
    {
        return ( ( BooleanFunction.evaluate( obj, nav ).booleanValue() )
                 ? Boolean.FALSE
                 : Boolean.TRUE
                 );
    }
    
}