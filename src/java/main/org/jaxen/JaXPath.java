
package org.jaxen;

import org.jaxen.expr.Expr;
import org.jaxen.expr.XPath;
import org.jaxen.function.BooleanFunction;
import org.jaxen.function.StringFunction;
import org.jaxen.function.NumberFunction;

import org.saxpath.XPathReader;
import org.saxpath.SAXPathException;
import org.saxpath.helpers.XPathReaderFactory;

import java.io.Serializable;
import java.util.List;

/** Generic non-abstract XPath expression.
 *
 *  <p>
 *  <b>NOTE:</b> This is <i>not</i> intended for production
 *  use, but simply as an implementation-agnostic hook point
 *  for performing testing.
 *  </p>
 *
 *  <p>
 *  If you are reading this, chances are, you're a core Jaxen developer.
 *  If for some reason, you're not sure how you ended up here, you
 *  should probably take a look at {@link BaseXPath} instead.
 *  </p>
 *
 *  @see BaseXPath
 *  @see org.jaxen.dom4j.XPath XPath for dom4j
 *  @see org.jaxen.jdom.XPath  XPath for JDOM
 *  @see org.jaxen.dom.XPath   XPath for W3C DOM
 *  @see org.jaxen.exml.XPath  XPath for EXML
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
class JaXPath implements Serializable
{
    /** the parsed form of the xpath expression */
    private XPath xpath;

    /** Construct given an XPath expression string.
     *
     *  @param xpathExpr The XPath expression.
     *
     *  @throws JaxenException if there is a syntax error while
     *          parsing the expression.
     */
    public JaXPath(String xpathExpr) throws JaxenException
    {
        try
        {
            XPathReader reader = XPathReaderFactory.createReader();
            
            JaxenHandler handler = new JaxenHandler();
            
            reader.setXPathHandler( handler );
            
            reader.parse( xpathExpr );

            this.xpath = handler.getXPath();
        }
        catch (org.saxpath.XPathSyntaxException e)
        {
            throw new org.jaxen.XPathSyntaxException( e.getXPath(),
                                                      e.getPosition(),
                                                      e.getMessage() );
        }
        catch (SAXPathException e)
        {
            throw new JaxenException( e );
        }
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
     *  @see #jaSelectSingleNode
     */
    protected List jaSelectNodes(Context context) throws JaxenException
    { 
        return this.xpath.asList( context );
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
     *  @see #jaSelectNodes
     */
    protected Object jaSelectSingleNode(Context context) throws JaxenException
    {
        List results = jaSelectNodes( context );

        if ( results.isEmpty() )
        {
            return null;
        }

        return results.get( 0 );
    }

    protected String jaValueOf(Context context) throws JaxenException
    {
        Object result = jaSelectSingleNode( context );

        if ( result == null )
        {
            return "";
        }

        return StringFunction.evaluate( result,
                                        context.getNavigator() );
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
    protected boolean jaBooleanValueOf(Context context) throws JaxenException
    {
        List result = jaSelectNodes( context );

        if ( result == null )
            return false;

        return BooleanFunction.evaluate( result, context.getNavigator() ).booleanValue();
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
    protected Number jaNumberValueOf(Context context) throws JaxenException
    {
        Object result = jaSelectSingleNode( context );

        if ( result == null )
        {
            return null;
        }

        return NumberFunction.evaluate( result,
                                        context.getNavigator() );
    }

    /** Retrieve the root expression of the internal
     *  compiled form of this XPath expression.
     *
     *  <p>
     *  Internally, Jaxen maintains a form of Abstract Syntax
     *  Tree (AST) to represent the structure of the XPath expression.
     *  This is normally not required during normal consumer-grade
     *  usage of Jaxen.  This method is provided for hard-core users
     *  who wish to manipulate or inspect a tree-based version of
     *  the expression.
     *  </p>
     *
     *  @return The root of the AST of this expression.
     */
    public Expr getRootExpr() 
    {
        return xpath.getRootExpr();
    }
    
    /** Return the normalized string of this XPath expression.
     *
     *  <p>
     *  During parsing, the XPath expression is normalized,
     *  removing abbreviations and other convenience notation.
     *  This method returns the fully normalized representation
     *  of the original expression.
     *  </p>
     *
     *  @return The normalized XPath expression string.
     */
    public String toString()
    {
        return this.xpath.getText();
    }

    /** Returns the string version of this xpath.
     *
     *  @return The normalized XPath expression string.
     *
     *  @see #toString
     */
    public String debug()
    {
        return this.xpath.toString();
    }
}
