
package org.jaxen;

import org.jaxen.function.NumberFunction;

import java.util.Collection;

class NumberValueExpectation implements SubExpectation
{
    private Number expected;
    private double delta;

    public NumberValueExpectation(Number expected,
                                  double delta)
    {
        this.expected = expected;
        this.delta    = delta;
    }

    public Number getExpectedNumber()
    {
        return this.expected;
    }

    public boolean wasMet(Object result,
                          Navigator nav)
    {
        Number resultNum = (Number) getToCompare( result,
                                                  nav );

        boolean met = false;

        double ed = this.expected.doubleValue();
        double ad = resultNum.doubleValue();

        if ( Math.abs( ed - ad ) > delta )
        {
            met = false;
        }
        else
        {
            met = true;
        }
        return met;
    }

    public Object getToCompare(Object result,
                               Navigator nav)
    {
        Number resultNum = NumberFunction.evaluate( result,
                                                    nav );

        return resultNum;
    }

    public String toString()
    {
        return "number-value = " + getExpectedNumber();
    }
}
