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

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;

import org.jaxen.function.BooleanFunction;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

class PredicateSet implements Serializable
{
    private List predicates;

    public PredicateSet()
    {
        this.predicates = Collections.EMPTY_LIST;
    }

    public void addPredicate(Predicate predicate)
    {
        if ( this.predicates == Collections.EMPTY_LIST )
        {
            this.predicates = new ArrayList();
        }

        this.predicates.add( predicate );
    }

    public List getPredicates()
    {
        return this.predicates;
    }

    public void simplify()
    {
        Iterator  predIter = this.predicates.iterator();
        Predicate eachPred = null;

        while ( predIter.hasNext() )
        {
            eachPred = (Predicate) predIter.next();
            eachPred.simplify();
        }
    }

    public String getText()
    {
        StringBuffer buf = new StringBuffer();

        Iterator  predIter = this.predicates.iterator();
        Predicate eachPred = null;

        while ( predIter.hasNext() )
        {
            eachPred = (Predicate) predIter.next();
            buf.append( eachPred.getText() );
        }

        return buf.toString();
    }

    // XXXX: Note - this could be *MUCH* more efficient
    // currently this creates many redundant collections and should halt 
    // evaluation on the first matching item.            
    protected boolean evaluateAsBoolean(List contextNodeSet,
                                      ContextSupport support) throws JaxenException
    {
        List result = evaluatePredicates( contextNodeSet, support );
        
        return ! result.isEmpty();
    }
    
    protected List evaluatePredicates(List contextNodeSet,
                                      ContextSupport support) throws JaxenException
    {
	// Easy way out (necessary)

	if (predicates.size()==0) return contextNodeSet;

        List      newNodeSet  = new ArrayList();
	List      filterSet   = contextNodeSet;

        List      predicates  = getPredicates();
        Iterator  predIter    = predicates.iterator();
        
        Predicate eachPred    = null;
        Object    contextNode = null;

        Object predResult  = null;
        Context predContext = new Context( support );

	// Filter the 'filterSet' against all predicates. This has been
	// tuned for performance, so there are two separate loops

	// In the first run, all nodes matching the first predicate will
	// be copied from contextNodeSet to newNodeSet

	// In the second loop, newNodeSet is filtered progressively against
	// the remaining predicates, eliminating any non-matching nodes

	if ( predIter.hasNext() )
        {
            eachPred = (Predicate) predIter.next();

            int filterSize = filterSet.size();

            for ( int i = 0 ; i < filterSize ; ++i )
            {
                contextNode = filterSet.get( i );

                List list = new ArrayList( 1 );

                list.add( contextNode );

                predContext.setNodeSet( list );

                predContext.setPosition( i + 1 );
                predContext.setSize( filterSize );

                predResult = eachPred.evaluate( predContext );

                if ( predResult instanceof Number )
                {
                    int proximity = ((Number)predResult).intValue();

                    if ( proximity == ( i + 1 ) )
                    {
                        newNodeSet.add( contextNode );
                    }
                }
                else
                {
                    Boolean includes = BooleanFunction.evaluate( predResult, predContext.getNavigator() );

                    if ( includes.booleanValue() )
                    {
                        newNodeSet.add( contextNode );
                    }
                }
            }
        }


	// This will be true if any filtering takes place from here on

	boolean nodesFiltered = false;

	// Second loop: Filter filterSet until no more predicates are left

	filterSet = newNodeSet;

	while ( predIter.hasNext() )
        {
            eachPred = (Predicate) predIter.next();


	    int filterSize = filterSet.size();

            for ( int i = 0 ; i < filterSize ; ++i )
            {
                contextNode = filterSet.get(i);

		// Check if a node has been eliminated already

		if (contextNode == null) continue;

                List list = new ArrayList( 1 );

                list.add( contextNode );

                predContext.setNodeSet( list );

                predContext.setPosition( i + 1 );
                predContext.setSize( filterSize );

                predResult = eachPred.evaluate( predContext );

                if ( predResult instanceof Number )
                {
                    int proximity = ((Number)predResult).intValue();

                    if ( proximity != ( i + 1 ) )
                    {
                        filterSet.set(i,null);
			nodesFiltered = true;
                    }
                }
                else
                {
                    Boolean includes = BooleanFunction.evaluate( predResult, predContext.getNavigator() );

                    if ( !includes.booleanValue() )
                    {
                        filterSet.set(i,null);
			nodesFiltered = true;
                    }
                }
            }
        }

	// Go through the filtered set and delete any null nodes (if necessary)

	if (nodesFiltered) 
	{
	    // System.err.println("FILTERING");

	    Iterator iter=filterSet.iterator();

	    while (iter.hasNext()) {
		Object obj=iter.next();

		if (obj==null) 
		    iter.remove();
	    }
	}

	return filterSet;
    }
}
