
package org.jaxen;

/** Supporting context information for resolving
 *  namespace prefixess, functions, and variables.
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class ContextSupport
{
    private NamespaceContext namespaceContext;
    private FunctionContext  functionContext;
    private VariableContext  variableContext;

    private Navigator        navigator;
    
    /** Construct an empty <code>ContextSupport</code>.
     */
    public ContextSupport()
    {
        // intentionally left blank
    }

    /** Construct.
     *
     *  @param namespaceContext The NamespaceContext.
     *  @param functionContext The FunctionContext.
     *  @param variableContext The VariableContext.
     */
    public ContextSupport(NamespaceContext namespaceContext,
                          FunctionContext  functionContext,
                          VariableContext  variableContext,
                          Navigator        navigator)
    {
        setNamespaceContext( namespaceContext );
        setFunctionContext( functionContext );
        setVariableContext( variableContext );
        this.navigator = navigator;
    }

    public void setNamespaceContext(NamespaceContext namespaceContext)
    {
        this.namespaceContext = namespaceContext;
    }

    public void setFunctionContext(FunctionContext functionContext)
    {
        this.functionContext  = functionContext;
    }

    public void setVariableContext(VariableContext variableContext)
    {
        this.variableContext  = variableContext;
    }

    public NamespaceContext getNamespaceContext()
    {
        return this.namespaceContext;
    }

    public FunctionContext getFunctionContext()
    {
        return this.functionContext;
    }

    public VariableContext getVariableContext()
    {
        return this.variableContext;
    }

    public Navigator getNavigator()
    {
        return this.navigator;
    }

    public String translateNamespacePrefixToUri(String prefix)
    {
        NamespaceContext context = getNamespaceContext();

        if ( context != null )
        {
            return context.translateNamespacePrefixToUri( prefix );
        }

        return "";
    }

    public Object getVariableValue(String prefix,
                                   String localName)
    {
        VariableContext context = getVariableContext();

        if ( context != null )
        {
            return context.getVariableValue( prefix,
                                             localName );
        }

        return null;
    }

    public Function getFunction(String prefix,
                                String localName)
    {
        FunctionContext context = getFunctionContext();

        if ( context != null )
        {
            return context.getFunction( prefix,
                                        localName );
        }

        return null;
    }
}
