// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.JaxenException;

import org.jaxen.util.IdentityHashMap;

import org.jaxen.util.SingleObjectIterator;
import org.jaxen.util.LinkedIterator;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;

abstract class DefaultLocationPath extends DefaultExpr implements LocationPath
{
    private List steps;

    private final static Object PRESENT = new Object();

    public DefaultLocationPath()
    {
        this.steps = new LinkedList();
    }

    public void addStep(Step step)
    {
        getSteps().add( step );
    }

    public List getSteps()
    {
        return this.steps;
    }

    public Expr simplify()
    {
        Iterator stepIter = getSteps().iterator();
        Step     eachStep = null;

        while ( stepIter.hasNext() )
        {
            eachStep = (Step) stepIter.next();

            eachStep.simplify();
        }
        return this;
    }

    public String getText()
    {
        StringBuffer buf = new StringBuffer();
        Iterator stepIter = getSteps().iterator();

        while ( stepIter.hasNext() )
        {
            buf.append( ((Step)stepIter.next()).getText() );

            if ( stepIter.hasNext() )
            {
                buf.append( "/" );
            }
        }

        return buf.toString();
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();

        Iterator stepIter = getSteps().iterator();

        while( stepIter.hasNext() )
        {
            buf.append( stepIter.next().toString() );

            if ( stepIter.hasNext() )
            {
                buf.append("/");
            }
        }

        return buf.toString();
    }
    
    public boolean isAbsolute() 
    {
        return false;
    }

    public Object evaluate(Context context) throws JaxenException
    {
        List     contextNodeSet  = new ArrayList();
        Map      unique          = new IdentityHashMap();

        contextNodeSet.addAll( context.getNodeSet() );

        Object   eachContextNode = null;

        Iterator stepIter        = getSteps().iterator();
        Step     eachStep        = null;

        List     newNodeSet      = new ArrayList();

        int      contextSize     = 0;

      OUTTER:
        while ( stepIter.hasNext() )
        {
            eachStep = (Step) stepIter.next();

            contextSize = contextNodeSet.size();

          INNER:
            for ( int i = 0 ; i < contextSize ; ++i )
            {
                eachContextNode = contextNodeSet.get( i );
                
                Iterator axisNodeIter = eachStep.axisIterator( eachContextNode,
                                                               context.getContextSupport() );
                
                if ( axisNodeIter == null )
                {
                    continue INNER;
                }
                
                Object   eachAxisNode = null;

                List interimSet=new ArrayList();

                while ( axisNodeIter.hasNext() )
                {
                    eachAxisNode = axisNodeIter.next();

                    // System.err.println( "----> " + eachAxisNode + " // " + eachStep.matches( eachAxisNode, context.getContextSupport()  ) );

                    if ( eachStep.matches( eachAxisNode,
                                           context.getContextSupport() ) )
                    {
                        if ( ! unique.containsKey( eachAxisNode ) )
                        {
                            unique.put( eachAxisNode,
                                        PRESENT );
                            interimSet.add( eachAxisNode );
                        }
                    }
                }

		List filtered = eachStep.getPredicateSet().evaluatePredicates(interimSet,context.getContextSupport() );

		newNodeSet.addAll(filtered);
            }
            
            contextNodeSet.clear();
            contextNodeSet.addAll( newNodeSet );
            newNodeSet.clear();
            unique.clear();
        }
        
        return contextNodeSet;
    }
}
