
package org.jaxen;

import org.saxpath.SAXPathException;

import java.util.List;
import java.util.Collections;

public abstract class BaseXPath extends JaXPath
{
    /** Configurable variable context used to evaluate XPath variables */
    private VariableContext variableContext;
    
    public BaseXPath(String xpathExpr) throws SAXPathException
    {
        super( xpathExpr );
    }

    public List selectNodes(Object context) throws JaxenException
    {
        return jaSelectNodes( (Context) getContext( context ) );
    }

    public Object selectSingleNode(Object context) throws JaxenException
    {
        return jaSelectSingleNode( (Context) getContext( context ) );
    }

    public String valueOf(Object context) throws JaxenException
    {
        return jaValueOf( (Context) getContext( context ) );
    }

    public Number numberValueOf(Object context) throws JaxenException
    {
        return jaNumberValueOf( (Context) getContext( context ) );
    }

    protected Context getContext(Object context)
    {
        if ( context instanceof Context )
        {
            return (Context) context;
        }

        Context fullContext = new Context( getContextSupport() );

        if ( context instanceof List )
        {
            fullContext.setNodeSet( (List) context );
        }
        else
        {
            fullContext.setNodeSet( Collections.singletonList( context ) );
        }

        return fullContext;
    }

    protected ContextSupport getContextSupport()
    {
        ContextSupport support = new ContextSupport( new SimpleNamespaceContext(),
                                                     XPathFunctionContext.getInstance(),
                                                     getVariableContext(),
                                                     getNavigator() );

        return support;
    }

    protected abstract Navigator getNavigator();
    
    public VariableContext getVariableContext()
    {
        if ( variableContext == null ) 
        {
            variableContext = new SimpleVariableContext();
        }
        return variableContext;
    }
    
    public void setVariableContext(VariableContext variableContext)
    {
        this.variableContext = variableContext;
    }
}
