
package org.jaxen;

import org.jaxen.function.*;

import java.util.Map;
import java.util.HashMap;

public class XPathFunctionContext implements FunctionContext
{
    private Map functions;

    private static class Singleton
    {
        private static XPathFunctionContext instance = new XPathFunctionContext();
    }

    public static FunctionContext getInstance()
    {
        return Singleton.instance;
    }

    public XPathFunctionContext()
    {
        this.functions = new HashMap();

        registerFunction( "",
                          "count",
                          new CountFunction() );

        registerFunction( "",
                          "false",
                          new FalseFunction() );

        registerFunction( "",
                          "true",
                          new TrueFunction() );
    }

    protected void registerFunction(String prefix,
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
