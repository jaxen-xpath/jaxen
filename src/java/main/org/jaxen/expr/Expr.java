// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;

import java.util.List;
import java.util.Iterator;

public interface Expr
{
    String getText();
    Expr simplify();

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    /*
    List     asList(Context context);
    Iterator asIterator(Context context);
    String   asString(Context context);
    Boolean  asBoolean(Context context);
    Number   asNumber(Context context);
    */

    Object   evaluate(Context context);
}
