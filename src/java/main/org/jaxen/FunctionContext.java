
package org.jaxen;

/** Implemented by classes that know how to resolve xpath function names and
 *  namespaces to implementations of these functions.
 *
 *  <p>
 *  By using a custom <code>FunctionContext</code>, new or different
 *  functions may be installed and available to XPath expression writers.
 *  </p>
 *
 *  @see XPathFunctionContext
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public interface FunctionContext
{
    /** An implementation should return a <code>Function</code> implementation object
     *  based on the namespace uri and local name of the function-call
     *  expression.
     *
     *  <p>
     *  It must not use the prefix parameter to select an implementation,
     *  because a prefix could be bound to any namespace; the prefix parameter
     *  could be used in debugging output or other generated information.
     *  The prefix may otherwise be completely ignored.
     *  </p>
     *
     *  @param namespaceURI  the namespace uri to which the prefix parameter
     *                       is bound in the xpath expression. If the function
     *                       call expression had no prefix, the namespace uri
     *                       is <code>null</code>.
     *  @param prefix        the prefix that was used in the function call
     *                       expression.
     *  @param localName     the local name of the function-call expression;
     *                       if there is no prefix, then this is the whole
     *                       name of the function.
     *
     *  @return  a Function implementation object.
     *  @throws UnresolvableException  when the function cannot be resolved.
     */
    Function getFunction( String namespaceURI,
                          String prefix,
                          String localName ) throws UnresolvableException;
}
