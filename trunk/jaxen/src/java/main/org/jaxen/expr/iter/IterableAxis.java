
package org.jaxen.expr.iter;

import org.jaxen.ContextSupport;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.expr.Step;

import java.io.Serializable;
import java.util.Iterator;

public abstract class IterableAxis implements Serializable
{
    private int value;

    public IterableAxis(int axisValue)
    {
        this.value = axisValue;
    }

    public int value()
    {
        return this.value;
    }

    public abstract Iterator iterator(Object contextNode,
                                      ContextSupport support) throws UnsupportedAxisException;
}
