// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class DefaultPredicated 
{
    private List predicates;

    protected DefaultPredicated()
    {
        this.predicates = new ArrayList();
    }

    public List getPredicates()
    {
        return this.predicates;
    }

    public void addPredicate(Predicate predicate)
    {
        getPredicates().add( predicate );
    }

    public String getText()
    {
        StringBuffer buf      = new StringBuffer();
        Iterator     predIter = getPredicates().iterator();

        while ( predIter.hasNext() )
        {
            buf.append( ((Predicate)predIter.next()).getText() );
        }

        return buf.toString();
    }

    public String toString()
    {
        StringBuffer buf      = new StringBuffer();
        Iterator     predIter = getPredicates().iterator();

        while( predIter.hasNext() )
        {
            buf.append( predIter.next().toString() );

            if ( predIter.hasNext() )
            {
                buf.append(", ");
            }
        }

        return buf.toString();
    }

    public void simplifyAllPredicates()
    {
        Iterator  predIter = getPredicates().iterator();
        Predicate eachPred = null;

        while ( predIter.hasNext() )
        {
            eachPred = (Predicate) predIter.next();

            eachPred.simplify();
        }
    }
}
