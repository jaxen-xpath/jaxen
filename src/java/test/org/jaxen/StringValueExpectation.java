
package org.jaxen;

import org.jaxen.function.StringFunction;

import java.util.Collection;

class StringValueExpectation implements SubExpectation
{
    private String expected;

    public StringValueExpectation(String expected)
    {
        this.expected = expected;
    }

    public String getExpectedString()
    {
        return this.expected;
    }

    public boolean wasMet(Object result,
                          Navigator nav)
    {
        String resultStr = (String) getToCompare( result,
                                                  nav );

        return this.expected.equals( resultStr );
    }

    public String toString()
    {
        return "string-value = \"" + getExpectedString() + "\"";
    }

    public Object getToCompare(Object result,
                               Navigator nav)
    {
        String compare = StringFunction.evaluate( result,
                                                  nav );

        return compare;
    }

        
}
