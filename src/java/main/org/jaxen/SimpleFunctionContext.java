
package org.jaxen;

import java.util.Map;
import java.util.HashMap;

/** Simple default implementation of <code>FunctionContext</code>.
 *
 *  <p>
 *  This is a simple table-based key-lookup implementation
 *  for <code>FunctionContext</code> which can be programmatically
 *  extended by registering additional functions.
 *  </p>
 *
 *  @see XPathFunctionContext
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class SimpleFunctionContext implements FunctionContext
{
    /** Table of functions. */
    private HashMap functions;

    /** Construct.
     *
     *  <p>
     *  Construct with an empty function lookup table.
     *  </p>
     */
    public SimpleFunctionContext()
    {
        this.functions = new HashMap();
    }

    /** Register a new function.
     *
     *  <p>
     *  By registering a new function, any XPath expression
     *  that utilizes this <code>FunctionContext</code> may
     *  refer-to and use the new function.
     *  </p>
     *
     *  <p>
     *  Functions may exist either in an namespace or not.
     *  Namespace prefix-to-URI resolution is the responsibility
     *  of a {@link NamespaceContext}.  Within this <code>FunctionContext</code>
     *  functions are only referenced using the URI, <b>not</b>
     *  the prefix.
     *  </p>
     *
     *  <p>
     *  The namespace URI of a function may be <code>null</code>
     *  to indicate that it exists without a namespace.
     *  </p>
     *
     *  @param namespaceURI The namespace URI of the function to
     *         be registered with this context.
     *  @param localName The non-prefixed local portion of the
     *         function to be registered with this context.
     *  @param function A {@link Function} implementation object
     *         to be used when evaluating the function.
     */
    public void registerFunction(String namespaceURI,
                                 String localName,
                                 Function function )
    {
        this.functions.put( new QualifiedName(namespaceURI, localName),
                            function );
    }

    public Function getFunction(String namespaceURI,
                                String prefix,
                                String localName )
        throws UnresolvableException
    {
        Object key = new QualifiedName( namespaceURI, localName );

        if ( this.functions.containsKey(key) )
            return (Function) this.functions.get( key );
        else
            throw new UnresolvableException( "Function " +
                                             prefix + ":" + localName );
    }
}
