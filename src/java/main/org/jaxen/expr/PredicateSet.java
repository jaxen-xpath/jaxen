// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;

import org.jaxen.function.BooleanFunction;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

class PredicateSet
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
        evaluatePredicates( contextNodeSet, support );
        return ! contextNodeSet.isEmpty();
    }
    
    protected void evaluatePredicates(List contextNodeSet,
                                      ContextSupport support) throws JaxenException
    {
        List      newNodeSet  = new ArrayList();
        List      predicates  = getPredicates();
        Iterator  predIter    = predicates.iterator();
        
        Predicate eachPred    = null;
        Object    contextNode = null;

        int    contextSize = 0;
        Object predResult  = null;
        Context predContext = new Context( support );

        while ( predIter.hasNext() )
        {
            eachPred = (Predicate) predIter.next();

            contextSize = contextNodeSet.size();

            for ( int i = 0 ; i < contextSize ; ++i )
            {
                contextNode = contextNodeSet.get( i );
                predContext.setNodeSet( Collections.singletonList( contextNode ) );
                predContext.setPosition( i + 1 );
                predContext.setSize( contextSize );

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
                    Boolean includes = BooleanFunction.evaluate( predResult );

                    if ( includes.booleanValue() )
                    {
                        newNodeSet.add( contextNode );
                    }
                }
            }

            contextNodeSet.clear();
            contextNodeSet.addAll( newNodeSet );
            newNodeSet.clear();
        }
    }
}
