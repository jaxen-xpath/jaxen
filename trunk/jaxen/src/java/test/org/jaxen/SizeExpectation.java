
package org.jaxen;

import java.util.Collection;

class SizeExpectation implements SubExpectation
{
    private int expectedSize;

    public SizeExpectation(int expectedSize)
    {
        this.expectedSize = expectedSize;
    }

    public int getExpectedSize()
    {
        return this.expectedSize;
    }

    public boolean wasMet(Object result,
                          Navigator nav)
    {
        Integer actual = (Integer) getToCompare( result,
                                                 nav );
        
        if ( getExpectedSize() == actual.intValue() )
        {
            return true;
        }

        return false;
    }

    public Object getToCompare(Object result,
                               Navigator nav)
    {
        if ( result instanceof Collection )
        {
            return new Integer( ((Collection)result).size() );
        }

        return new Integer( 1 );
    }

    public String toString()
    {
        return "size = " + getExpectedSize();
    }
}
