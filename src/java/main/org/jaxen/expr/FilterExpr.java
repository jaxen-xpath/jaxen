// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;

public interface FilterExpr extends Expr, Predicated
{

    /** Evaluates the filter expression on the current context
     * and returns true if at least one node matches.
     */
    public boolean asBoolean(Context context) throws JaxenException;
}
