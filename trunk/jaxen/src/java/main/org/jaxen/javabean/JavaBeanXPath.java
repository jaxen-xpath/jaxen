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

package org.jaxen.javabean;

import org.jaxen.Context;
import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** An XPath implementation for JavaBeans.
 *
 * <p>This is the main entry point for matching an XPath against a JavaBean
 * tree.  You create a compiled XPath object, then match it against
 * one or more context nodes using the {@link #selectNodes(Object)}
 * method, as in the following example:</p>
 *
 * <pre>
 * Node node = ...;
 * XPath path = new JavaBeanXPath("a/b/c");
 * List results = path.selectNodes(node);
 * </pre>
 *
 * @see BaseXPath
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *
 * @version $Revision$
 */
public class JavaBeanXPath extends BaseXPath
{
    /** Construct given an XPath expression string.
     *
     *  @param xpathExpr The XPath expression.
     *
     *  @throws JaxenException if there is a syntax error while
     *          parsing the expression
     */
    public JavaBeanXPath(String xpathExpr) throws JaxenException
    {
        super( xpathExpr, DocumentNavigator.getInstance() );
    }

    protected Context getContext(Object node)
    {
        if ( node instanceof Context )
        {
            return (Context) node;
        }

        if ( node instanceof Element )
        {
            return super.getContext( node );
        }

        if ( node instanceof List )
        {
            List newList = new ArrayList();

            for ( Iterator listIter = ((List)node).iterator();
                  listIter.hasNext(); )
            {
                newList.add( new Element( null, "root", listIter.next() ) );
            }

            return super.getContext( newList );
        }

        return super.getContext( new Element( null, "root", node ) );
    }

    public Object evaluate(Object node)
        throws JaxenException
    {
        Object result = super.evaluate( node );

        if ( result instanceof Element )
        {
            return ((Element)result).getObject();
        }
        else if ( result instanceof Collection )
        {
            List newList = new ArrayList();

            for ( Iterator listIter = ((Collection)result).iterator();
                  listIter.hasNext(); )
            {
                Object member = listIter.next();

                if ( member instanceof Element )
                {
                    newList.add( ((Element)member).getObject() );
                }
                else
                {
                    newList.add( member );
                }
            }

            return newList;
        }

        return result;
    }
} 
