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
 * <p>
 * <b>4.1</b>
 * <code><i>string</i> namespace-uri(<i>node-set?</i>)</code>
 * </p>
 * 
 * <blockquote src="http://www.w3.org/TR/xpath">
 * <p>
 * The <b>namespace-uri</b>
 * function returns the namespace URI of the <a
 * href="http://www.w3.org/TR/xpath#dt-expanded-name" target="_top">expanded-name</a> of the node in the
 * argument node-set that is first in <a
 * href="http://www.w3.org/TR/xpath#dt-document-order" target="_top">document order</a>. If the argument
 * node-set is empty, the first node has no <a
 * href="http://www.w3.org/TR/xpath#dt-expanded-name" target="_top">expanded-name</a>, or the namespace URI of
 * the <a href="http://www.w3.org/TR/xpath#dt-expanded-name" target="_top">expanded-name</a> is null, an empty
 * string is returned. If the argument is omitted, it defaults to a
 * node-set with the context node as its only member.
 * </p>
 * 
 * <blockquote> <b>NOTE: </b>The string returned by the <b>namespace-uri</b> function will
 * be empty except for element nodes and attribute nodes.</blockquote>
 * 
 * </blockquote>
 * 
 * @author bob mcwhirter (bob @ werken.com)
 * @see <a href="http://www.w3.org/TR/xpath#function-namespace-uri"
 *      target="_top">Section 4.1 of the XPath Specification</a>
 */
public class NamespaceUriFunction implements Function
{

    /**
     * Returns the namespace URI of the specified node or the namespace URI of the context node if 
     * no arguments are provided.
     * 
     * @param context the context at the point in the
     *         expression where the function is called
     * @param args a <code>List</code> containing zero or one items
     * 
     * @return a <code>String</code> containing the namespace URI
     * 
     * @throws FunctionCallException if <code>args</code> has more than one item
     */
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 0)
        {
            return evaluate( context.getNodeSet(),
                             context.getNavigator() );
        }

        if ( args.size() == 1 )
        {
            return evaluate( args,
                             context.getNavigator() );
        }

        throw new FunctionCallException( "namespace-uri() requires zero or one argument." );
    }

    /**
     * Returns the namespace URI of <code>list.get(0)</code>
     * 
     * @param list a list of nodes
     * @param nav the <code>Navigator</code> used to retrieve the namespace
     * 
     * @return the namespace URI of <code>list.get(0)</code>
     * 
     * @throws FunctionCallException if <code>list.get(0)</code> is not a node
     */
    public static String evaluate(List list,
                                  Navigator nav) throws FunctionCallException
    {
        if ( ! list.isEmpty() )
        {
            Object first = list.get(0);

            if ( first instanceof List )
            {
                return evaluate( (List) first,
                                 nav );
            }
            else if ( nav.isElement( first ) )
            {
                return nav.getElementNamespaceUri( first );
            }
            else if ( nav.isAttribute( first ) )
            {
                String uri = nav.getAttributeNamespaceUri( first );
                if (uri == null) return "";
                return uri;
            }
            else if ( nav.isProcessingInstruction( first ) )
            {
                return "";
            }
            else if ( nav.isNamespace( first ) )
            {
                return "";
            }
            else if ( nav.isDocument( first ) )
            {
                return "";
            }
            else if ( nav.isComment( first ) )
            {
                return "";
            }
            else if ( nav.isText( first ) )
            {
                return "";
            }
            else {
                throw new FunctionCallException(
                  "The argument to the namespace-uri function must be a node-set");   
            }

        }

        return "";

    }
}

