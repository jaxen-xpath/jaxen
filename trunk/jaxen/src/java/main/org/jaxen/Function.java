
package org.jaxen;

import java.util.List;

/** Interface for the extensible function framework.
 *
 *  <p>
 *  <b>NOTE:</b> This class is not typically used directly,
 *  but is exposed for writers of extended XPath packages.
 *  </p>
 *
 *  <p>
 *  Implementations of <code>Function</code> are functors
 *  which are used to evaluate a function-call within an
 *  XPath expression.  
 *  </p>
 *
 *  @see FunctionContext
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public interface Function
{
    /** Call this function.
     *
     *  @param context The context at the point in the
     *         expression when the function is called.
     *  @param args List of arguments provided during
     *         the call of the function.
     */
    Object call(Context context,
                List args) throws FunctionCallException;
}
