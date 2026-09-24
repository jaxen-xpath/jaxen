package org.jaxen.expr;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;
import org.jaxen.expr.iter.IterableAxis;
import org.jaxen.saxpath.Axis;

public class DefaultStepTest extends TestCase {

    public void testEvaluateHandlesNullAxisIterator() throws JaxenException {
        DefaultStep step = new DefaultStep(new NullIteratorAxis(), new PredicateSet()) {
            public boolean matches(Object node, ContextSupport contextSupport) {
                return true;
            }
        };

        Context context = new Context(new ContextSupport());
        context.setNodeSet(Collections.singletonList(new Object()));

        List result = step.evaluate(context);

        assertTrue(result.isEmpty());
    }

    private static final class NullIteratorAxis extends IterableAxis {

        private NullIteratorAxis() {
            super(Axis.CHILD);
        }

        public Iterator iterator(Object contextNode, ContextSupport support) {
            return null;
        }
    }
}
