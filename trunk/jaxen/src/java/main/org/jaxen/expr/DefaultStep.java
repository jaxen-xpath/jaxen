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
package org.jaxen.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.expr.iter.IterableAxis;
import org.jaxen.saxpath.Axis;
import org.jaxen.util.IdentityHashMap;

public abstract class DefaultStep implements Step
{
    private IterableAxis axis;
    private PredicateSet predicates;

    private final static Object PRESENT = new Object();

    public DefaultStep(IterableAxis axis, PredicateSet predicates)
    {
        this.axis = axis;
        this.predicates = predicates;
    }

    public void addPredicate(Predicate predicate)
    {
        this.predicates.addPredicate(predicate);
    }

    public List getPredicates()
    {
        return this.predicates.getPredicates();
    }

    public PredicateSet getPredicateSet()
    {
        return this.predicates;
    }

    public int getAxis()
    {
        return this.axis.value();
    }

    public IterableAxis getIterableAxis()
    {
        return this.axis;
    }

    public String getAxisName()
    {
        return Axis.lookup(getAxis());
    }

    public String getText()
    {
        return this.predicates.getText();
    }

    public String toString()
    {
        return getIterableAxis() + " " + super.toString();
    }

    public void simplify()
    {
        this.predicates.simplify();
    }

    public Iterator axisIterator(Object contextNode, ContextSupport support)
        throws UnsupportedAxisException
    {
        return getIterableAxis().iterator(contextNode, support);
    }

    public List evaluate(Context context) throws JaxenException
    {
        List contextNodeSet  = context.getNodeSet();
        Map unique = new IdentityHashMap();
        int contextSize = contextNodeSet.size();

        List interimSet = new ArrayList();
        List newNodeSet = new ArrayList();

        Object eachContextNode = null;

        INNER:
        for ( int i = 0 ; i < contextSize ; ++i )
        {
            eachContextNode = contextNodeSet.get( i );

            Iterator axisNodeIter = axisIterator( eachContextNode,
                                                  context.getContextSupport() );
            if ( axisNodeIter == null )
            {
                continue INNER;
            }

            Object eachAxisNode = null;
            while ( axisNodeIter.hasNext() )
            {
                eachAxisNode = axisNodeIter.next();

                if ( matches( eachAxisNode,
                              context.getContextSupport() ) )
                {
                    if ( ! unique.containsKey( eachAxisNode ) )
                    {
                        unique.put( eachAxisNode, PRESENT );
                        interimSet.add( eachAxisNode );
                    }
                }
            }
            newNodeSet.addAll(getPredicateSet().evaluatePredicates(
                              interimSet,context.getContextSupport() ));
            interimSet.clear();
        }
        return newNodeSet;
    }

}
