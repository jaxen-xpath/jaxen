
package org.jaxen;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class BaseXPath extends JaXPath
{
    /** the support information and function, namespace and variable contexts */
    private ContextSupport support;
    
    public BaseXPath(String xpathExpr) throws JaxenException
    {
        super( xpathExpr );
    }

    /** Returns a String or Number or Boolean if this XPath expression
     * results in a primitive value being returned or a List of Nodes is
     * returned.
     */     
    public Object evaluate(Object context) throws JaxenException
    {
        List answer = selectNodes(context);
        if ( answer != null && answer.size() == 1) {
            Object first = answer.get(0);
            if ( first instanceof String || first instanceof Number || first instanceof Boolean ) 
            {
                return first;
            }
            // should we return first if the expression results in one node?
        }
        return answer;
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

    public boolean booleanValueOf(Object context) throws JaxenException
    {
        return jaBooleanValueOf( (Context) getContext( context ) );
    }

    public Number numberValueOf(Object context) throws JaxenException
    {
        return jaNumberValueOf( (Context) getContext( context ) );
    }

    // Helpers

    public BaseXPath addNamespace(String prefix,
                                  String uri) throws JaxenException
    {
        NamespaceContext nsContext = getNamespaceContext();

        if ( nsContext instanceof SimpleNamespaceContext )
        {
            ((SimpleNamespaceContext)nsContext).addNamespace( prefix,
                                                              uri );
            
            return this;
        }

        throw new JaxenException("Operation not permitted while using a custom namespace context.");
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
        NamespaceContext answer = getContextSupport().getNamespaceContext();
        if ( answer == null ) {
            answer = createNamespaceContext();
            getContextSupport().setNamespaceContext( answer );
        }
        return answer;
    }

    public FunctionContext getFunctionContext()
    {
        FunctionContext answer = getContextSupport().getFunctionContext();
        if ( answer == null ) {
            answer = createFunctionContext();
            getContextSupport().setFunctionContext( answer );
        }
        return answer;
    }

    public VariableContext getVariableContext()
    {
        VariableContext answer = getContextSupport().getVariableContext();
        if ( answer == null ) {
            answer = createVariableContext();
            getContextSupport().setVariableContext( answer );
        }
        return answer;
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
