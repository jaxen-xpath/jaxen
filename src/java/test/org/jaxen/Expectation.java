
package org.jaxen;

import java.util.List;
import java.util.ArrayList;

class Expectation
{
    private String xpath;
    private List   expectations;

    private int    failedIndex;

    public Expectation(String xpath)
    {
        this.xpath        = xpath;
        this.expectations = new ArrayList();
        this.failedIndex  = -1;
    }

    public String getXPath()
    {
        return this.xpath;
    }

    public boolean wasMet(Object result,
                          Navigator nav)
    {
        this.failedIndex = -1;

        boolean met = true;

        for ( int i = 0 ; i < expectations.size() ; ++i )
        {
            met = ((SubExpectation)expectations.get( i )).wasMet( result,
                                                                  nav );
            
            if ( ! met )
            {
                this.failedIndex = i;
                break;
            }
        }

        return met;
    }

    public SubExpectation getFailedExpectation()
    {
        if ( this.failedIndex < 0 )
        {
            return null;
        }

        return (SubExpectation) this.expectations.get( failedIndex );
    }

    public Expectation expectSize(int size)
    {
        this.expectations.add( new SizeExpectation( size ) );

        return this;
    }

    public Expectation expectStringValue(String str)
    {
        this.expectations.add( new StringValueExpectation( str ) );

        return this;
    }

    public Expectation expectNumberValue(int num)
    {
        this.expectations.add( new NumberValueExpectation( new Integer( num ),
                                                           0 ) );

        return this;
    }

    public Expectation expectNumberValue(double num,
                                         double delta)
    {
        this.expectations.add( new NumberValueExpectation( new Double( num ),
                                                           delta ) );

        return this;
    }

    public Expectation expectBooleanValue(boolean bool)
    {
        this.expectations.add( new BooleanValueExpectation( bool ) );
        
        return this;
    }
}
