
package org.jaxen;

import org.jaxen.function.*;
import org.jaxen.function.ext.MatrixConcatFunction;

import java.util.Map;
import java.util.HashMap;

public class XPathFunctionContext extends SimpleFunctionContext
{
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
        registerFunction( "",
                          "boolean",
                          new BooleanFunction() );

        registerFunction( "",
                          "ceiling",
                          new CeilingFunction() );

        registerFunction( "",
                          "concat",
                          new ConcatFunction() );

        registerFunction( "",
                          "contains",
                          new ContainsFunction() );
        
        registerFunction( "",
                          "count",
                          new CountFunction() );

        registerFunction( "",
                          "false",
                          new FalseFunction() );

        registerFunction( "",
                          "floor",
                          new FloorFunction() );

        registerFunction( "",
                          "id",
                          new IdFunction() );

        registerFunction( "",
                          "last",
                          new LastFunction() );

        registerFunction( "",
                          "local-name",
                          new LocalNameFunction() );

        registerFunction( "",
                          "name",
                          new NameFunction() );

        registerFunction( "",
                          "namespace-uri",
                          new NamespaceUriFunction() );

        registerFunction( "",
                          "normalize-space",
                          new NormalizeSpaceFunction() );

        registerFunction( "",
                          "not",
                          new NotFunction() );

        registerFunction( "",
                          "number",
                          new NumberFunction() );

        registerFunction( "",
                          "position",
                          new PositionFunction() );

        registerFunction( "",
                          "round",
                          new RoundFunction() );

        registerFunction( "",
                          "starts-with",
                          new StartsWithFunction() );

        registerFunction( "",
                          "string",
                          new StringFunction() );

        registerFunction( "",
                          "string-length",
                          new StringLengthFunction() );

        registerFunction( "",
                          "substring-after",
                          new SubstringAfterFunction() );

        registerFunction( "",
                          "substring-before",
                          new SubstringBeforeFunction() );

        registerFunction( "",
                          "substring",
                          new SubstringFunction() );

        registerFunction( "",
                          "sum",
                          new SumFunction() );

        registerFunction( "",
                          "true",
                          new TrueFunction() );
        
        
        // register extension functions
        registerFunction( "",
                          "matrix-concat",
                          new MatrixConcatFunction() );
        
        registerFunction( "",
                          "document",
                          new DocumentFunction() );
    }
}
