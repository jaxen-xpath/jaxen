// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;

import java.util.Iterator;
import java.util.Set;
import java.util.List;

class DefaultFilterExpr extends DefaultExpr implements FilterExpr, Predicated
{
    private Expr expr;
    private PredicateSet predicates;

    public DefaultFilterExpr(Expr expr)
    {
        this.expr       = expr;
        this.predicates = new PredicateSet();
    }

    public void addPredicate(Predicate predicate)
    {
        this.predicates.addPredicate( predicate );
    }

    public List getPredicates()
    {
        return this.predicates.getPredicates();
    }

    public PredicateSet getPredicateSet()
    {
        return this.predicates;
    }

    public Expr getExpr()
    {
        return this.expr;
    }

    public String toString()
    {
        return "[(DefaultFilterExpr): " + getExpr() + "]";
    }

    public String getText()
    {
        return getExpr().getText();
    }

    public Expr simplify()
    {
        this.predicates.simplify();

        this.expr = getExpr().simplify();

        if ( this.predicates.getPredicates().size() == 0 )
        {
            return getExpr();
        }

        return this;
    }

    public Object evaluate(Context context) throws JaxenException
    {
        Object results = getExpr().evaluate( context );
        
        if ( results instanceof List )
        {
            getPredicateSet().evaluatePredicates( (List) results,
                                                  context.getContextSupport() );
        }

        return results;
    }
}
