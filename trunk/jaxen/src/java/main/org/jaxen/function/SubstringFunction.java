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
 */
package org.jaxen.function;

import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
/**
 * <p><b>4.2</b> <code><i>string</i> substring(<i>string</i>,<i>number</i>,<i>number?</i>)</code>
 *
 * @author bob mcwhirter (bob @ werken.com)
 * @see "http://www.w3.org/TR/xpath#function-substring"
 */
public class SubstringFunction implements Function
{
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        final int argc = args.size();
        if (argc < 2 || argc > 3){
            throw new FunctionCallException( "substring() requires two or three arguments." );
        }

        final Navigator nav = context.getNavigator();

        final String str = StringFunction.evaluate(args.get(0), nav );
        // The spec doesn't really address this case
        if (str == null){
            return "";
        }

        final int strlen = str.length();

        if (strlen == 0){
            return "";
        }

        Double d1 = NumberFunction.evaluate(args.get(1), nav);

        if (d1.isNaN()){
            return "";
        }
        // Round the value and subtract 1 as Java strings are zero based
        int start = RoundFunction.evaluate(d1, nav).intValue() - 1;

        int len = strlen;
        if (argc == 3){
            Double d2 = NumberFunction.evaluate(args.get(2), nav);

            if (!d2.isNaN()){
                len = RoundFunction.evaluate(d2, nav ).intValue();
            }
        }

        // negative start is treated as 0
        if ( start < 0){
            start = 0;
        }else if (start > strlen){
            return "";
        }

        // if the length is longer than the rest of the string just
        // take the rest of the string
        int end = start + len;
        if (end > strlen){
            end = strlen;
        }
        return str.substring(start, end);
    }
}
