// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import java.util.List;

public interface Predicated
{
    void addPredicate(Predicate predicate);
    List getPredicates();
    PredicateSet getPredicateSet();
}
