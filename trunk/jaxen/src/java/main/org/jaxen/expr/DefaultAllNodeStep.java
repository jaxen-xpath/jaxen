// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.ContextSupport;

import org.jaxen.expr.iter.IterableAxis;

class DefaultAllNodeStep extends DefaultStep
{
    public DefaultAllNodeStep(IterableAxis axis)
    {
        super( axis );
    }

    public String toString()
    {
        return "[(DefaultAllNodeStep): " + getAxisName() + "]";
    }

    public String getText()
    {
        return getAxisName() + "::node()" + super.getText();
    }

    public boolean matches(Object node,
                           ContextSupport contextSupport)
    {
        return true;
    }
}
