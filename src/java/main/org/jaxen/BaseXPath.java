
package org.jaxen;

import org.saxpath.SAXPathException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class BaseXPath extends JaXPath
{
    /** the support information and function, namespace and variable contexts */
    private ContextSupport support;
    
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


    // Properties
    
    public void setNamespaceContext(NamespaceContext namespaceContext)
    {
        getContextSupport().setNamespaceContext(namespaceContext);
    }

    public void setFunctionContext(FunctionContext functionContext)
    {
        getContextSupport().setFunctionContext(functionContext);
    }

    public void setVariableContext(VariableContext variableContext)
    {
        getContextSupport().setVariableContext(variableContext);
    }

    public NamespaceContext getNamespaceContext()
    {
        return getContextSupport().getNamespaceContext();
    }

    public FunctionContext getFunctionContext()
    {
        return getContextSupport().getFunctionContext();
    }

    public VariableContext getVariableContext()
    {
        return getContextSupport().getVariableContext();
    }
    
    
    // Implementation methods
    
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
            List list = new ArrayList( 1 );

            list.add( context );

            fullContext.setNodeSet( list );
        }

        return fullContext;
    }

    protected ContextSupport getContextSupport()
    {
        if ( support == null )
        {
            support = new ContextSupport( 
                createNamespaceContext(),
                createFunctionContext(),
                createVariableContext(),
                getNavigator() 
            );
        }

        return support;
    }

    public abstract Navigator getNavigator();
    
    

    // Factory methods for default contexts
    
    protected FunctionContext createFunctionContext()
    {
        return XPathFunctionContext.getInstance();
    }
    
    protected NamespaceContext createNamespaceContext()
    {
        return new SimpleNamespaceContext();
    }
    
    protected VariableContext createVariableContext()
    {
        return new SimpleVariableContext();
    }
    
}
