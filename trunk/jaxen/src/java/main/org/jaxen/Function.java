
package org.jaxen;

import java.util.List;

public interface Function
{
    Object call(Context context,
                List args) throws FunctionCallException;
}
