// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.ContextSupport;
import org.jaxen.Navigator;

import org.jaxen.expr.iter.IterableAxis;

class DefaultTextNodeStep extends DefaultStep
{
    public DefaultTextNodeStep(IterableAxis axis)
    {
        super( axis );
    }

    public boolean matches(Object node,
                           ContextSupport support)
    {
        Navigator nav = support.getNavigator();

        return nav.isText( node );
    }

    public String getText()
    {
        return getAxisName() + "::text()" + super.getText();
    }
}
