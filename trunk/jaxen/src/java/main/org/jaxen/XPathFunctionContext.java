
package org.jaxen;

import org.jaxen.function.*;
import org.jaxen.function.ext.EvaluateFunction;
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
        registerFunction( null,  // namespace URI
                          "boolean",
                          new BooleanFunction() );

        registerFunction( null,  // namespace URI
                          "ceiling",
                          new CeilingFunction() );

        registerFunction( null,  // namespace URI
                          "concat",
                          new ConcatFunction() );

        registerFunction( null,  // namespace URI
                          "contains",
                          new ContainsFunction() );
        
        registerFunction( null,  // namespace URI
                          "count",
                          new CountFunction() );

        registerFunction( null,  // namespace URI
                          "document",
                          new DocumentFunction() );

        registerFunction( null,  // namespace URI
                          "false",
                          new FalseFunction() );

        registerFunction( null,  // namespace URI
                          "floor",
                          new FloorFunction() );

        registerFunction( null,  // namespace URI
                          "id",
                          new IdFunction() );

        registerFunction( null,  // namespace URI
                          "last",
                          new LastFunction() );

        registerFunction( null,  // namespace URI
                          "local-name",
                          new LocalNameFunction() );

        registerFunction( null,  // namespace URI
                          "name",
                          new NameFunction() );

        registerFunction( null,  // namespace URI
                          "namespace-uri",
                          new NamespaceUriFunction() );

        registerFunction( null,  // namespace URI
                          "normalize-space",
                          new NormalizeSpaceFunction() );

        registerFunction( null,  // namespace URI
                          "not",
                          new NotFunction() );

        registerFunction( null,  // namespace URI
                          "number",
                          new NumberFunction() );

        registerFunction( null,  // namespace URI
                          "position",
                          new PositionFunction() );

        registerFunction( null,  // namespace URI
                          "round",
                          new RoundFunction() );

        registerFunction( null,  // namespace URI
                          "starts-with",
                          new StartsWithFunction() );

        registerFunction( null,  // namespace URI
                          "string",
                          new StringFunction() );

        registerFunction( null,  // namespace URI
                          "string-length",
                          new StringLengthFunction() );

        registerFunction( null,  // namespace URI
                          "substring-after",
                          new SubstringAfterFunction() );

        registerFunction( null,  // namespace URI
                          "substring-before",
                          new SubstringBeforeFunction() );

        registerFunction( null,  // namespace URI
                          "substring",
                          new SubstringFunction() );

        registerFunction( null,  // namespace URI
                          "sum",
                          new SumFunction() );

        registerFunction( null,  // namespace URI
                          "true",
                          new TrueFunction() );
        

        // register extension functions
        // extension functions should go into a namespace, but which one?
        // for now, keep them in default namespace to not to break any code

        registerFunction( null,  // namespace URI
                          "matrix-concat",
                          new MatrixConcatFunction() );

        registerFunction( null,  // namespace URI
                          "evaluate",
                          new EvaluateFunction() );
        
    }
}
