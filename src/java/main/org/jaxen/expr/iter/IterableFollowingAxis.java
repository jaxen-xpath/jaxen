
package org.jaxen.expr.iter;

import org.jaxen.ContextSupport;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.expr.Step;

import java.util.Iterator;

public class IterableFollowingAxis extends IterableAxis
{
    public IterableFollowingAxis(int value)
    {
        super( value );
    }

    public Iterator iterator(Object contextNode,
                             ContextSupport support) throws UnsupportedAxisException
    {
        return support.getNavigator().getFollowingAxisIterator( contextNode );
    }
}
