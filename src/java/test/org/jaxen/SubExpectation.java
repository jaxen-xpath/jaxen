
package org.jaxen;

public interface SubExpectation
{
    boolean wasMet(Object result,
                   Navigator nav);

    Object getToCompare(Object result,
                        Navigator nav);
}
