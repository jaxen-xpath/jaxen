
package org.jaxen;

import org.saxpath.SAXPathException;

/** Generic Jaxen exception.
 *
 *  <p>
 *  This is the root of all Jaxen exceptions.
 *  It may wrap other exceptions.  See {@link #getRootCause}.
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class JaxenException extends SAXPathException
{
    /** Root cause, if any. */
    private Throwable rootCause;

    /** Construct with a message.
     *
     *  @param message The error message.
     */
    public JaxenException(String message)
    {
        super( message );
    }

    /** Construct with a root cause.
     *
     *  @param rootCause Root cause of the error.
     */
    public JaxenException(Throwable rootCause)
    {
        super( "wrapped exception" );
        this.rootCause = rootCause;
    }

    /** Retrieve the root cause, if any.
     *
     *  @return Root cause of the error.
     */
    public Throwable getRootCause()
    {
        return this.rootCause;
    }
}

