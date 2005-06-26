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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;

/**
 * <p><b>4.1</b> <code><i>node-set</i> id(<i>object</i>)</code> </p>
 *  
 * <p>The <b>id</b> function returns a <code>List</code>
 * of all the elements in the context document that have an ID
 * matching one of a specified list of IDs. How an attribute is determined
 * to be of type ID depends on the navigator, but it normally requires that
 * the attribute be declared to have type ID in the DTD. 
 * </p>
 * 
 * @author Erwin Bolwidt (ejb @ klomp.org)
 * @author J\u00e9r\u00f4me N\u00e8gre (jerome.negre @ e-xmlmedia.fr)
 * 
 * @see <a href="http://www.w3.org/TR/xpath#function-id" target="_top">Section 4.1 of the XPath Specification</a>
 */
public class IdFunction implements Function
{

    /** 
     * Returns the node with the specified ID.
     *
     * @param context the context at the point in the
     *         expression when the function is called
     * @param args a list with exactly one item which is either a string
     *     a node-set
     * 
     * @return a <code>List</code> containing the node with the specified ID; or 
     *     an empty list if there is no such node
     * 
     * @throws FunctionCallException if <code>args</code> has more or less than one item
     */
    public Object call(Context context, List args) throws FunctionCallException
    {
        if ( args.size() == 1 ) {
            return evaluate( context.getNodeSet(),
                             args.get(0), context.getNavigator() );
        }

        throw new FunctionCallException( "id() requires one argument" );
    }

    /** 
     * Returns the node with the specified ID.
     * @param contextNodes the context node-set. The first item in this list
     *     determines the document in which the search is performed.
     * @param arg the ID or IDs to search for
     * @param nav the navigator used to calculate string-values and search
     *     by ID
     * 
     * @return a <code>List</code> containing the node with the specified ID; or 
     *     an empty list if there is no such node
     * 
     */
    public static List evaluate(List contextNodes, Object arg, Navigator nav)
    {
        if (contextNodes.size() == 0) return Collections.EMPTY_LIST;
      
        List nodes = new ArrayList();

        Object contextNode = contextNodes.get(0);

        if (arg instanceof List) {
            Iterator iter = ((List)arg).iterator();
            while (iter.hasNext()) {
                String id = StringFunction.evaluate(iter.next(), nav);
                nodes.addAll( evaluate( contextNodes, id, nav ) );
            }
        } 
        else {
            String ids = StringFunction.evaluate(arg, nav);
            StringTokenizer tok = new StringTokenizer(ids, " \t\n\r");
            while (tok.hasMoreTokens()) {
                String id = tok.nextToken();
                Object node = nav.getElementById(contextNode, id);
                if (node != null) {
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }
    
}

