
package org.jaxen;

public interface VariableContext
{
    /** An implementation should return the value of an xpath variable
     *  based on the namespace uri and local name of the variable-reference
     *  expression.
     *  It must not use the prefix parameter to select a variable,
     *  because a prefix could be bound to any namespace; the prefix parameter
     *  could be used in debugging output or other generated information.
     *
     *  @param namespaceURI  the namespace uri to which the prefix parameter
     *                       is bound in the xpath expression. If the variable
     *                       reference expression had no prefix, the namespace
     *                       uri is <code>null</code>.
     *  @param prefix        the prefix that was used in the variable reference
     *                       expression.
     *  @param localName     the local name of the variable-reference
     *                       expression; if there is no prefix, then this is
     *                       the whole name of the variable.
     *
     *  @return  the variables' value (which can be <code>null</code>)
     *  @throws UnresolvableException  when the variable cannot be resolved.
     */
    public Object getVariableValue( String namespaceURI,
                                    String prefix,
                                    String localName )
        throws UnresolvableException;
}
