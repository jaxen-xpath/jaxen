
package org.jaxen;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/** Simple default implementation for <code>VariableContext</code>.
 *
 *  <p>
 *  This is a simple table-based key-lookup implementation
 *  for <code>VariableContext</code> which can be programmatically
 *  extended by setting additional variables.
 *  </p>
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class SimpleVariableContext implements VariableContext, Serializable
{
    /** Table of variable bindings. */
    private Map variables;

    /** Construct.
     *
     *  <p>
     *  Construct with an empty variable lookup table.
     *  </p>
     */
    public SimpleVariableContext()
    {
        variables = new HashMap();
    }

    /** Set the value associated with a variable.
     *
     *  <p>
     *  This method sets a variable that is 
     *  associated with any particular namespace.
     *  These variables appear such as <code>$prefix:foo</code>
     *  in an XPath expression.  Prefix to URI resolution
     *  is the responsibility of a <code>NamespaceContext</code>.
     *  Variables within a <code>VariableContext</code> are
     *  refered to purely based upon their namespace URI,
     *  if any.
     *  </p>
     *
     *  @param namespaceURI The namespace URI of the variable.
     *  @param localName The local name of the variable
     *  @param value The value to be bound to the variable.
     */
    public void setVariableValue( String namespaceURI,
                                  String localName,
                                  Object value )
    {
        this.variables.put( new QualifiedName(namespaceURI, localName),
                            value );
    }

    /** Set the value associated with a variable.
     *
     *  <p>
     *  This method sets a variable that is <b>not</b>
     *  associated with any particular namespace.
     *  These variables appear such as <code>$foo</code>
     *  in an XPath expression.
     *  </p>
     *
     *  @param localName The local name of the variable
     *  @param value The value to be bound to the variable.
     */
    public void setVariableValue( String localName,
                                  Object value )
    {
        this.variables.put( new QualifiedName(null, localName), value );
    }

    public Object getVariableValue( String namespaceURI,
                                    String prefix,
                                    String localName )
        throws UnresolvableException
    {
        Object key = new QualifiedName( namespaceURI, localName );

        if ( this.variables.containsKey(key) )
        {
            return this.variables.get( key );
        }
        else
        {
            throw new UnresolvableException( "Variable {" + namespaceURI +
                                             "}" + prefix + ":" + localName );
        }
    }
}
