
package org.jaxen;

/** Resolves variable bindings within an XPath expression.
 *
 *  <p>
 *  Variables within an XPath expression are denoted using
 *  notation such as $varName or $nsPrefix:varName, and may
 *  refer to primitive types (Boolean, Number or String),
 *  <code>node-sets</code> or individual XML nodes.
 *  </p>
 *
 *  <p>
 *  When a variable is bound to a <code>node-set</code>, the
 *  actual Java object returned should be a <code>java.util.List</code>
 *  containing XML nodes from the object-model (dom4j, JDOM, DOM, EXML)
 *  being used with the XPath.
 *  </p>
 *
 *  <p>
 *  A variable may validly be assigned the <code>null</code> value,
 *  but an unbound variable (one that this context does not know about)
 *  should cause an {@link UnresolvableException} to be thrown.
 *  </p>
 *
 *  @see SimpleVariableContext
 *  @see NamespaceContext
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public interface VariableContext
{
    /** An implementation should return the value of an xpath variable
     *  based on the namespace uri and local name of the variable-reference
     *  expression.
     *
     *  <p>
     *  It must not use the prefix parameter to select a variable,
     *  because a prefix could be bound to any namespace; the prefix parameter
     *  could be used in debugging output or other generated information.
     *  The prefix may otherwise be ignored.
     *  </p>
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
     *  @return  the variable's value (which can be <code>null</code>)
     *  @throws UnresolvableException  when the variable cannot be resolved.
     */
    public Object getVariableValue( String namespaceURI,
                                    String prefix,
                                    String localName )
        throws UnresolvableException;
}
