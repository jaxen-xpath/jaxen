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
 *  <p><b>4.3</b> <code><i>boolean</i> boolean(<i>object</i>)</code> 
 *  
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class BooleanFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if ( args.size() == 1 )
        {
            return evaluate( args.get(0), context.getNavigator() );
        }

        throw new FunctionCallException("boolean() requires one argument");
    }

    public static Boolean evaluate(Object obj, Navigator nav)
    {
        if ( obj instanceof List )
        {
            List list = (List) obj;
            
            // if it's an empty list, then we have a null node-set -> false            
            if (list.size() == 0)
            {
                return Boolean.FALSE;
            }
     
            // otherwise, unwrap the list and check the primitive
            obj = list.get(0);
        }
        
        // now check for primitive types
        // otherwise a non-empty node-set is true

        // if it's a Boolean, let it decide
        if ( obj instanceof Boolean )
        {
            return (Boolean) obj;
        }
        // if it's a Number, != 0 -> true
        else if ( obj instanceof Number )
        {
            double d = ((Number) obj).doubleValue();
            if ( d == 0 || Double.isNaN(d) )
            {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        // if it's a String, "" -> false
        else if ( obj instanceof String )
        {
            return ( ((String)obj).length() > 0
                     ? Boolean.TRUE
                     : Boolean.FALSE );
        }
        else 
        {
            // assume it's a node so that this node-set is non-empty 
            // and so it's true
            return ( obj != null ) ? Boolean.TRUE : Boolean.FALSE;
        }

    }
}
