
package org.jaxen;

/** Used when a function-call or variable-reference, or any other lookup
 *  based on namespace and local name, couldn't be resolved.
 *
 *  @author Erwin Bolwidt (ejb @ klomp.org)
 */
public class UnresolvableException extends JaxenException
{
    public UnresolvableException(String message)
    {
        super( message );
    }
}

