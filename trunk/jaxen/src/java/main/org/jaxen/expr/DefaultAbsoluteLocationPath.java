// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.Navigator;
import org.jaxen.JaxenException;

import java.util.List;
import java.util.Collections;

public class DefaultAbsoluteLocationPath extends DefaultLocationPath 
{
    public DefaultAbsoluteLocationPath()
    {
    }

    public String toString()
    {
        return "[(DefaultAbsoluteLocationPath): " + super.toString() + "]";
    }

    public String getText()
    {
        return "/" + super.getText();
    }

    public Object evaluate(Context context) throws JaxenException
    {
        ContextSupport support = context.getContextSupport();
        Navigator      nav     = support.getNavigator();

        Context absContext = new Context( support );

        
        List contextNodes = context.getNodeSet();

        if ( contextNodes.isEmpty() )
        {
            return Collections.EMPTY_LIST;
        }

        Object firstNode = contextNodes.get( 0 );

        Object docNode   = nav.getDocumentNode( firstNode );

        if ( docNode == null )
        {
            return Collections.EMPTY_LIST;
        }

        context.setNodeSet( Collections.singletonList( docNode ) );

        return super.evaluate( context );
    }
}
