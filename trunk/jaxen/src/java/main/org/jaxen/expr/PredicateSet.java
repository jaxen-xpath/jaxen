// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

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
