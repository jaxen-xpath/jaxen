
package org.jaxen;

import org.jaxen.function.*;
import org.jaxen.function.ext.EvaluateFunction;
import org.jaxen.function.ext.MatrixConcatFunction;

import java.util.Map;
import java.util.HashMap;

/** A <code>FunctionContext</code> implementing the core XPath
 *  function library, with extensions.
 *
 *  <p>
 *  The core XPath function library is provided through this
 *  implementation of <code>FunctionContext</code>.  Additionally,
 *  extension functions have been provided, as enumerated below.
 *  </p>
 *
 *  <p>
 *  This class implements a <i>Singleton</i> pattern (see {@link #getInstance}),
 *  as it is perfectly re-entrant and thread-safe.  If using the
 *  singleton, it is inadvisable to call {@link #registerFunction}
 *  as that will extend the global function context, affecting other
 *  users of the singleton.  But that's your call, really, now isn't
 *  it?  That may be what you really want to do.
 *  </p>
 *
 *  <p>
 *  Extension functions:
 *
 *  <ul>
 *     <li>matrix-concat(..)</li>
 *     <li>evaluate(..)</li>
 *  </ul>
 *
 *  @see FunctionContext
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class XPathFunctionContext extends SimpleFunctionContext
{
   /** Singleton implementation.
    */
    private static class Singleton
    {
        /** Singleton instance.
         */
        private static XPathFunctionContext instance = new XPathFunctionContext();
    }

    /** Retrieve the singleton instance.
     *
     *  @return The singleton instance.
     */
    public static FunctionContext getInstance()
    {
        return Singleton.instance;
    }

    /** Construct.
     *
     *  <p>
     *  Construct with all core XPath functions registered.
     *  </p>
     */
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
