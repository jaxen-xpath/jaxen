
package org.jaxen;

/** Implemented by classes that know how to resolve xpath function names and
 *  namespaces to implementations of these functions.
 */
public interface FunctionContext
{
    /** An implementation should return a Function [implementation] object
     *  based on the namespace uri and local name of the function-call
     *  expression.
     *  It must not use the prefix parameter to select an implementation,
     *  because a prefix could be bound to any namespace; the prefix parameter
     *  could be used in debugging output or other generated information.
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
