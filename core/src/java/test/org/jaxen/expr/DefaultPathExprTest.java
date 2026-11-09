package org.jaxen.expr;

import junit.framework.TestCase;

public class DefaultPathExprTest extends TestCase {

    public void testToStringOmitsNullFilterExprWhenLocationPathIsNull() {
        DefaultPathExpr expr = new DefaultPathExpr(null, null);
        assertEquals("[(DefaultPathExpr)]", expr.toString());
    }
}
