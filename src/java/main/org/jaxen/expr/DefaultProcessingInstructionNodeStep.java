// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.ContextSupport;
import org.jaxen.Navigator;

import org.jaxen.expr.iter.IterableAxis;

class DefaultProcessingInstructionNodeStep extends DefaultStep
{
    private String name;

    public DefaultProcessingInstructionNodeStep(IterableAxis axis,
                                                String name)
    {
        super( axis );

        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean matches(Object node,
                           ContextSupport support)
    {
        Navigator nav = support.getNavigator();

        return nav.isProcessingInstruction( node );
    }
}
