// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.ContextSupport;
import org.jaxen.Navigator;

import org.jaxen.expr.iter.IterableAxis;

class DefaultCommentNodeStep extends DefaultStep
{
    public DefaultCommentNodeStep(IterableAxis axis)
    {
        super( axis );
    }

    public String toString()
    {
        return "[(DefaultCommentNodeStep): " + getAxis() + "]";
    }

    public String getText()
    {
        return getAxisName() + "::comment()";
    }

    public boolean matches(Object node,
                           ContextSupport contextSupport)
    {
        Navigator nav = contextSupport.getNavigator();

        return nav.isComment( node );
    }
}
