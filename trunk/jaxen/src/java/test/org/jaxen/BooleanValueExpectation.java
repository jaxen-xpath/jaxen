
package org.jaxen;

import org.jaxen.function.BooleanFunction;

import java.util.Collection;

class BooleanValueExpectation implements SubExpectation
{
    private boolean expected;

    public BooleanValueExpectation(boolean expected)
    {
        this.expected = expected;
    }

    public boolean getExpectedBoolean()
    {
        return this.expected;
    }

    public boolean wasMet(Object result,
                          Navigator nav)
    {
        Boolean actual = (Boolean) getToCompare( result,
                                                 nav );

        return ( this.expected == actual.booleanValue() );
    }

    public String toString()
    {
        return "boolean-value = \"" + getExpectedBoolean() + "\"";
    }

    public Object getToCompare(Object result,
                               Navigator nav)
    {
        Boolean actual  = BooleanFunction.evaluate( result );

        return actual;
    }

        
}
