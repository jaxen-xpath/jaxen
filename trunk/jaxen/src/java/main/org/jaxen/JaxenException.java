
package org.jaxen;

import org.saxpath.SAXPathException;

public class JaxenException extends SAXPathException
{
    private Throwable rootCause;

    public JaxenException(String message)
    {
        super( message );
    }

    public JaxenException(Throwable rootCause)
    {
        super( "wrapped exception" );
        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return this.rootCause;
    }
}

