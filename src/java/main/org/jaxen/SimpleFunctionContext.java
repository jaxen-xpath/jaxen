
package org.jaxen;

import java.util.Map;
import java.util.HashMap;

public class SimpleFunctionContext implements FunctionContext
{
    private HashMap functions;

    public SimpleFunctionContext()
    {
        this.functions = new HashMap();
    }

    public void registerFunction(String prefix,
                                 String localName,
                                 Function function)
    {
        this.functions.put( prefix + ":" + localName,
                            function );
    }

    public Function getFunction(String prefix,
                                String localName)
    {
        return (Function) this.functions.get( prefix + ":" + localName );
    }
}
