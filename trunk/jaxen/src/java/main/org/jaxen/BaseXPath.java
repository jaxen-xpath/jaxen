
package org.jaxen;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/** Base functionality for all concrete, implementation-specific XPaths.
 *
 *  <p>
 *  This class provides generic functionalty for further-defined
 *  implementation-specific XPaths.
 *  </p>
 *
 *  @see org.jaxen.dom4j.XPath XPath for dom4j
 *  @see org.jaxen.jdom.XPath  XPath for JDOM
 *  @see org.jaxen.dom.XPath   XPath for W3C DOM
 *  @see org.jaxen.exml.XPath  XPath for EXML
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public abstract class BaseXPath extends JaXPath
{
    /** the support information and function, namespace and variable contexts */
    private ContextSupport support;
    
    /** Construct given an XPath expression string.
     *
     *  @param xpathExpr The XPath expression.
     *
     *  @throws JaxenException if there is a syntax error while
     *          parsing the expression.
     */
    public BaseXPath(String xpathExpr) throws JaxenException
    {
        super( xpathExpr );
    }

    /** Evaluate this XPath against a given context.
     *
     *  <p>
     *  The context of evaluation my be a <i>document</i>,
     *  an <i>element</i>, or a set of <i>elements</i>.
     *  </p>
     *
     *  <p>
     *  If the expression evaluates to a single primitive
     *  (String, Number or Boolean) type, it is returned
     *  directly.  Otherwise, the returned value is a
     *  List (a <code>node-set</code>, in the terms of the
     *  specification) of values.
     *  </p>
     *
     *  <p>
     *  When using this method, one must be careful to
     *  test the class of the returned objects, and of 
     *  each of the composite members if a <code>List</code>
     *  is returned.  If the returned members are XML entities,
     *  they will be the actual <code>Document</code>,
     *  <code>Element</code> or <code>Attribute</code> objects
     *  as defined by the concrete XML object-model implementation,
     *  directly from the context document.  This <b>does not
     *  return <i>copies</i> of anything</b>, but merely returns
     *  references to entities within the source document.
     *  </p>
     *  
     *  @param context The context for evaluation.
     *
     *  @return The result of evaluating the XPath expression
     *          against the supplied context.
     */
    public Object evaluate(Object context) throws JaxenException
    {
        List answer = selectNodes(context);

        if ( answer != null
             &&
             answer.size() == 1 )
        {
            Object first = answer.get(0);

            if ( first instanceof String
                 ||
                 first instanceof Number
                 ||
                 first instanceof Boolean ) 
            {
                return first;
            }
        }
        return answer;
    }
    
    /** Select all nodes that are selectable by this XPath
     *  expression. If multiple nodes match, multiple nodes
     *  will be returned.
     *
     *  <p>
     *  <b>NOTE:</b> In most cases, nodes will be returned
     *  in document-order, as defined by the XML Canonicalization
     *  specification.  The exception occurs when using XPath
     *  expressions involving the <code>union</code> operator
     *  (denoted with the pipe '|' character).
     *  </p>
     *
     *  @param context The context for evaluation.
     *
     *  @return The <code>node-set</code> of all items selected
     *          by this XPath expression.
     *
     *  @see #selectSingleNode
     */
    public List selectNodes(Object context) throws JaxenException
    {
        return jaSelectNodes( (Context) getContext( context ) );
    }

    /** Select only the first node that is selectable by this XPath
     *  expression.  If multiple nodes match, only one node will be
     *  returned.
     *
     *  <b>NOTE:</b> In most cases, the selected node will be the first
     *  selectable node in document-order, as defined by the XML Canonicalization
     *  specification.  The exception occurs when using XPath
     *  expressions involving the <code>union</code> operator
     *  (denoted with the pipe '|' character).
     *  </p>
     *
     *  @param context The context for evaluation.
     *
     *  @return The <code>node-set</code> of all items selected
     *          by this XPath expression.
     *
     *  @see #selectNodes
     */
    public Object selectSingleNode(Object context) throws JaxenException
    {
        return jaSelectSingleNode( (Context) getContext( context ) );
    }

    public String valueOf(Object context) throws JaxenException
    {
        return jaValueOf( (Context) getContext( context ) );
    }

    /** Retrieve a boolean-value interpretation of this XPath
     *  expression when evaluated against a given context.
     *
     *  <p>
     *  The boolean-value of the expression is determined per
     *  the <code>boolean(..)</code> core function as defined
     *  in the XPath specification.  This means that an expression
     *  that selects zero nodes will return <code>false</code>,
     *  while an expression that selects one-or-more nodes will
     *  return <code>true</code>.
     *  </p>
     *
     *  @param context The context for evaluation.
     *
     *  @return The boolean-value interpretation of this expression.
     */
    public boolean booleanValueOf(Object context) throws JaxenException
    {
        return jaBooleanValueOf( (Context) getContext( context ) );
    }

    /** Retrieve a number-value interpretation of this XPath
     *  expression when evaluated against a given context.
     *
     *  <p>
     *  The number-value of the expression is determined per
     *  the <code>number(..)</code> core function as defined
     *  in the XPath specification. This means that if this
     *  expression selects multiple nodes, the number-value
     *  of the first node is returned.
     *  </p>
     *
     *  @param context The context for evaluation.
     *
     *  @return The number-value interpretation of this expression.
     */
    public Number numberValueOf(Object context) throws JaxenException
    {
        return jaNumberValueOf( (Context) getContext( context ) );
    }

    // Helpers

    /** Add a namespace prefix-to-URI mapping for this XPath
     *  expression.
     *
     *  <p>
     *  Namespace prefix-to-URI mappings in an XPath are independant
     *  of those used within any document.  Only the mapping explicitly
     *  added to this XPath will be available for resolving the
     *  XPath expression.
     *  </p>
     *
     *  <p>
     *  This is a convenience method for adding mappings to the
     *  default {@link NamespaceContext} in place for this XPath.
     *  If you have installed a specific custom <code>NamespaceContext</code>,
     *  then this method will throw a <code>JaxenException</code>.
     *  </p>
     *
     *  @param prefix The namespace prefix.
     *  @param uri The namespace URI.
     *
     *  @throws JaxenException If a <code>NamespaceContext</code>
     *          used by this XPath has been explicitly installed.
     */
    public void addNamespace(String prefix,
                             String uri) throws JaxenException
    {
        NamespaceContext nsContext = getNamespaceContext();

        if ( nsContext instanceof SimpleNamespaceContext )
        {
            ((SimpleNamespaceContext)nsContext).addNamespace( prefix,
                                                              uri );

            return;
        }

        throw new JaxenException("Operation not permitted while using a custom namespace context.");
    }


    // ------------------------------------------------------------
    // ------------------------------------------------------------
    //     Properties
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    
    /** Set a <code>NamespaceContext</code> for use with this
     *  XPath expression.
     *
     *  <p>
     *  A <code>NamespaceContext</code> is responsible for translating
     *  namespace prefixes within the expression into namespace URIs.
     *  </p>
     *
     *  @param namespaceContext The <code>NamespaceContext</code> to
     *         install for this expression.
     *
     *  @see NamespaceContext
     *  @see NamespaceContext#translateNamespacePrefixToUri
     */
    public void setNamespaceContext(NamespaceContext namespaceContext)
    {
        getContextSupport().setNamespaceContext(namespaceContext);
    }

    /** Set a <code>FunctionContext</code> for use with this XPath
     *  expression.
     *
     *  <p>
     *  A <code>FunctionContext</code> is responsible for resolving
     *  all function calls used within the expression.
     *  </p>
     *
     *  @param functionContext The <code>FunctionContext</code> to
     *         install for this expression.
     *
     *  @see FunctionContext
     *  @see FunctionContext#getFunction
     */
    public void setFunctionContext(FunctionContext functionContext)
    {
        getContextSupport().setFunctionContext(functionContext);
    }

    /** Set a <code>VariableContext</code> for use with this XPath
     *  expression.
     *
     *  <p>
     *  A <code>VariableContext</code> is responsible for resolving
     *  all variables referenced within the expression.
     *  </p>
     *
     *  @param variableContext The <code>VariableContext</code> to
     *         install for this expression.
     *
     *  @see VariableContext
     *  @see VariableContext#getVariableValue
     */
    public void setVariableContext(VariableContext variableContext)
    {
        getContextSupport().setVariableContext(variableContext);
    }

    /** Retrieve the <code>NamespaceContext</code> used by this XPath
     *  expression.
     *
     *  <p>
     *  A <code>FunctionContext</code> is responsible for resolving
     *  all function calls used within the expression.
     *  </p>
     *
     *  <p>
     *  If this XPath expression has not previously had a <code>NamespaceContext</code>
     *  installed, a new default <code>NamespaceContext</code> will be created,
     *  installed and returned.
     *  </p>
     *
     *  @return The <code>NamespaceContext</code> used by this expression.
     *
     *  @see NamespaceContext
     */
    public NamespaceContext getNamespaceContext()
    {
        NamespaceContext answer = getContextSupport().getNamespaceContext();
        if ( answer == null ) {
            answer = createNamespaceContext();
            getContextSupport().setNamespaceContext( answer );
        }
        return answer;
    }

    /** Retrieve the <code>FunctionContext</code> used by this XPath
     *  expression.
     *
     *  <p>
     *  A <code>FunctionContext</code> is responsible for resolving
     *  all function calls used within the expression.
     *  </p>
     *
     *  <p>
     *  If this XPath expression has not previously had a <code>FunctionContext</code>
     *  installed, a new default <code>FunctionContext</code> will be created,
     *  installed and returned.
     *  </p>
     *
     *  @return The <code>FunctionContext</code> used by this expression.
     *
     *  @see FunctionContext
     */
    public FunctionContext getFunctionContext()
    {
        FunctionContext answer = getContextSupport().getFunctionContext();
        if ( answer == null ) {
            answer = createFunctionContext();
            getContextSupport().setFunctionContext( answer );
        }
        return answer;
    }

    /** Retrieve the <code>VariableContext</code> used by this XPath
     *  expression.
     *
     *  <p>
     *  A <code>VariableContext</code> is responsible for resolving
     *  all variables referenced within the expression.
     *  </p>
     *
     *  <p>
     *  If this XPath expression has not previously had a <code>VariableContext</code>
     *  installed, a new default <code>VariableContext</code> will be created,
     *  installed and returned.
     *  </p>
     *  
     *  @return The <code>VariableContext</code> used by this expression.
     *
     *  @see VariableContext
     */
    public VariableContext getVariableContext()
    {
        VariableContext answer = getContextSupport().getVariableContext();
        if ( answer == null ) {
            answer = createVariableContext();
            getContextSupport().setVariableContext( answer );
        }
        return answer;
    }
    
    
    // ------------------------------------------------------------
    // ------------------------------------------------------------
    //     Implementation methods
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    
    /** Create a {@link Context} wrapper for the provided
     *  implementation-specific object.
     *
     *  @param context The implementation-specific object 
     *         to be used as the context.
     *
     *  @return A <code>Context</code> wrapper around the object.
     */
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

    /** Retrieve the {@link ContextSupport} aggregation of
     *  <code>NamespaceContext</code>, <code>FunctionContext</code>,
     *  <code>VariableContext</code>, and {@link Navigator}.
     *
     *  @return Aggregate <code>ContextSupport</code> for this
     *          XPath expression.
     */
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

    /** Retrieve the XML object-model-specific {@link Navigator} 
     *  for us in evaluating this XPath expression.
     *
     *  @return The implementation-specific <code>Navigator</code>.
     */
    public abstract Navigator getNavigator();
    
    

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    //     Factory methods for default contexts
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    /** Create a default <code>FunctionContext</code>.
     *
     *  @return The default <code>FunctionContext</code>.
     */
    protected FunctionContext createFunctionContext()
    {
        return XPathFunctionContext.getInstance();
    }
    
    /** Create a default <code>NamespaceContext</code>.
     *
     *  @return A default <code>NamespaceContext</code> instance.
     */
    protected NamespaceContext createNamespaceContext()
    {
        return new SimpleNamespaceContext();
    }
    
    /** Create a default <code>VariableContext</code>.
     *
     *  @return A default <code>VariableContext</code> instance.
     */
    protected VariableContext createVariableContext()
    {
        return new SimpleVariableContext();
    }
    
}
