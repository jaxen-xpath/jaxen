
package org.jaxen;

import java.io.Serializable;

/** Supporting context information for resolving
 *  namespace prefixess, functions, and variables.
 * 
 *  <p>
 *  <b>NOTE:</b> This class is not typically used directly,
 *  but is exposed for writers of implementation-specific
 *  XPath packages.
 *  </p>
 *
 *  @see org.jaxen.dom4j.XPath XPath for dom4j
 *  @see org.jaxen.jdom.XPath  XPath for JDOM
 *  @see org.jaxen.dom.XPath   XPath for W3C DOM
 *  @see org.jaxen.exml.XPath  XPath for EXML
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class ContextSupport implements Serializable
{
    private transient FunctionContext  functionContext;
    
    private NamespaceContext namespaceContext;
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

        return null;
    }

    public Object getVariableValue( String namespaceURI,
                                    String prefix,
                                    String localName )
        throws UnresolvableException
    {
        VariableContext context = getVariableContext();

        if ( context != null )
        {
            return context.getVariableValue( namespaceURI, prefix, localName );
        }
        else
        {
            throw new UnresolvableException( "No variable context installed" );
        }
    }

    public Function getFunction( String namespaceURI,
                                 String prefix,
                                 String localName )
        throws UnresolvableException
    {
        FunctionContext context = getFunctionContext();

        if ( context != null )
        {
            return context.getFunction( namespaceURI, prefix, localName );
        }
        else
        {
            throw new UnresolvableException( "No function context installed" );
        }
    }
}
