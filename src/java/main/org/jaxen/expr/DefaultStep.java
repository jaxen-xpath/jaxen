// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.expr.iter.IterableAxis;

import org.jaxen.util.LinkedIterator;

import org.saxpath.Axis;

import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.Iterator;

public abstract class DefaultStep implements Step
{
    private IterableAxis axis;
    private PredicateSet predicates;

    public DefaultStep(IterableAxis axis)
    {
        this.axis       = axis;
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
        return Axis.lookup( getAxis() );
    }

    public String getText()
    {
        return this.predicates.getText();
    }

    public String toString()
    {
        return super.toString();
    }

    public void simplify()
    {
        this.predicates.simplify();
    }

    public Iterator axisIterator(Object contextNode,
                                 ContextSupport support) throws UnsupportedAxisException
    {
        return getIterableAxis().iterator( contextNode,
                                           support );
    }
}
