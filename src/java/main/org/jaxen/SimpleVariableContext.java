
package org.jaxen;

import java.util.Map;
import java.util.HashMap;

public class SimpleVariableContext implements VariableContext
{
    private Map variables;

    public SimpleVariableContext()
    {
        variables = new HashMap();
    }

    public void setVariableValue( String namespaceURI,
                                  String localName,
                                  Object value )
    {
        this.variables.put( new QualifiedName(namespaceURI, localName),
                            value );
    }

    /** Associates the local name with no namespace URI to the given value */
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
            return this.variables.get( key );
        else
            throw new UnresolvableException( "Variable {" + namespaceURI +
                                             "}" + prefix + ":" + localName );
    }
}
