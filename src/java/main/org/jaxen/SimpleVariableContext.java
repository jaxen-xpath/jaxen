
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

    public void setVariableValue(String prefix,
                                 String localName,
                                 Object value)
    {
        this.variables.put( prefix + ":" + localName,
                            value );
    }

    public Object getVariableValue(String prefix,
                                   String localName)
    {
        return this.variables.get( prefix + ":" + localName );
    }
}
