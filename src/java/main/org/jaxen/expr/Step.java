// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.ContextSupport;
import org.jaxen.UnsupportedAxisException;

import java.util.Iterator;

//public interface Step extends Predicated, Expr
public interface Step extends Predicated
{
    boolean matches(Object node,
                    ContextSupport contextSupport);

    String getText();
    void simplify();

    /*
    Iterator asIterator(Iterator contextIterator,
                        ContextSupport support);
    */

    Iterator axisIterator(Object contextNode,
                          ContextSupport support) throws UnsupportedAxisException;
}
