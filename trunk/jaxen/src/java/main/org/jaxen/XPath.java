package org.jaxen;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "jaxen" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "jaxen"
    nor may "jaxen" appear in their names without prior written
    permission of The Werken Company. "jaxen" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://jaxen.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import java.util.List;

/** Defines the interface to an object which represents an XPath 1 expression which
 *  can be evaluated against a variety of different XML object models.
 *
 *  <p>
 *  Most of the evaluation methods take a context object. This is typically a
 *  node or node set object (which is typically a List of node objects) or 
 *  a Jaxen Context object.
 *  A null context is allowed, meaning that 
 *  there is no XML nodes on which to evaluate.
 *  </p>
 *
 *  @see org.jaxen.dom4j.Dom4jXPath XPath for dom4j
 *  @see org.jaxen.jdom.JDOMXPath  XPath for JDOM
 *  @see org.jaxen.dom.DOMXPath   XPath for W3C DOM
 *  @see org.jaxen.exml.ElectricXPath  XPath for Electric XML
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public interface XPath
{
    // ----------------------------------------------------------------------
    //     Basic Evaluation
    // ----------------------------------------------------------------------

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
     *  @param context The node, nodeset or Context object for evaluation. This value can be null
     *
     *  @return The result of evaluating the XPath expression
     *          against the supplied context.
     *
     *  @throws JaxenException If an error occurs while attempting
     *          to perform evaluation.
     */
    Object evaluate(Object context) throws JaxenException;
    
    // ----------------------------------------------------------------------
    //     Advanced Evaluation
    // ----------------------------------------------------------------------
    
    /** Retrieve a string-value interpretation of this XPath
     *  expression when evaluated against a given context.
     *
     *  @deprecated As of Jaxen 1.0 RC1 please use 
     *      {@link #stringValueOf(Object) instead}
     *
     *  <p>
     *  The string-value of the expression is determined per
     *  the <code>string(..)</code> core function as defined
     *  in the XPath specification.  This means that an expression
     *  that selects more than one nodes will return the string value
     *  of the first node in the node set..
     *  </p>
     *
     *  @param context The node, nodeset or Context object for evaluation. This value can be null
     *
     *  @return The string-value interpretation of this expression.
     *
     *  @throws JaxenException If an error occurs while attempting
     *          to perform evaluation.
     */
    String valueOf(Object context)
        throws JaxenException;

    /** Retrieve a string-value interpretation of this XPath
     *  expression when evaluated against a given context.
     *
     *  <p>
     *  The string-value of the expression is determined per
     *  the <code>string(..)</code> core function as defined
     *  in the XPath specification.  This means that an expression
     *  that selects more than one nodes will return the string value
     *  of the first node in the node set..
     *  </p>
     *
     *  @param context The node, nodeset or Context object for evaluation. This value can be null
     *
     *  @return The string-value interpretation of this expression.
     *
     *  @throws JaxenException If an error occurs while attempting
     *          to perform evaluation.
     */
    String stringValueOf(Object context)
        throws JaxenException;
    
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
     *  @param context The node, nodeset or Context object for evaluation. This value can be null
     *
     *  @return The boolean-value interpretation of this expression.
     *
     *  @throws JaxenException If an error occurs while attempting
     *          to perform evaluation.
     */
    boolean booleanValueOf(Object context)
        throws JaxenException;
    

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
     *  @param context The node, nodeset or Context object for evaluation. This value can be null
     *
     *  @return The number-value interpretation of this expression.
     *
     *  @throws JaxenException If an error occurs while attempting
     *          to perform evaluation.
     */
    Number numberValueOf(Object context)
        throws JaxenException;

    // ----------------------------------------------------------------------
    //     Selection
    // ----------------------------------------------------------------------

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
     *  @see #selectSingleNode
     *
     *  @param context The node, nodeset or Context object for evaluation. This value can be null
     *
     *  @return The <code>node-set</code> of all items selected
     *          by this XPath expression.
     *
     *  @throws JaxenException If an error occurs while attempting
     *          to perform evaluation.
     */
    List selectNodes(Object context)
        throws JaxenException;

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
     *  @see #selectNodes
     *
     *  @param context The node, nodeset or Context object for evaluation. This value can be null
     *
     *  @return The <code>node-set</code> of all items selected
     *          by this XPath expression.
     *
     *  @throws JaxenException If an error occurs while attempting
     *          to perform evaluation.
     */
    Object selectSingleNode(Object context)
        throws JaxenException;
    
    // ----------------------------------------------------------------------
    //     Helpers
    // ----------------------------------------------------------------------

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
    void addNamespace(String prefix,
                      String uri)
        throws JaxenException;

    // ----------------------------------------------------------------------
    //     Properties
    // ----------------------------------------------------------------------
    
    /** Set a <code>NamespaceContext</code> for use with this
     *  XPath expression.
     *
     *  <p>
     *  A <code>NamespaceContext</code> is responsible for translating
     *  namespace prefixes within the expression into namespace URIs.
     *  </p>
     *
     *  @see NamespaceContext
     *  @see NamespaceContext#translateNamespacePrefixToUri
     *
     *  @param namespaceContext The <code>NamespaceContext</code> to
     *         install for this expression.
     */
    void setNamespaceContext(NamespaceContext namespaceContext);

    /** Set a <code>FunctionContext</code> for use with this XPath
     *  expression.
     *
     *  <p>
     *  A <code>FunctionContext</code> is responsible for resolving
     *  all function calls used within the expression.
     *  </p>
     *
     *  @see FunctionContext
     *  @see FunctionContext#getFunction
     *
     *  @param functionContext The <code>FunctionContext</code> to
     *         install for this expression.
     */
    void setFunctionContext(FunctionContext functionContext);

    /** Set a <code>VariableContext</code> for use with this XPath
     *  expression.
     *
     *  <p>
     *  A <code>VariableContext</code> is responsible for resolving
     *  all variables referenced within the expression.
     *  </p>
     *
     *  @see VariableContext
     *  @see VariableContext#getVariableValue
     *
     *  @param variableContext The <code>VariableContext</code> to
     *         install for this expression.
     */
    void setVariableContext(VariableContext variableContext);

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
     *  @see NamespaceContext
     *
     *  @return The <code>NamespaceContext</code> used by this expression.
     */
    NamespaceContext getNamespaceContext();

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
     *  @see FunctionContext
     *
     *  @return The <code>FunctionContext</code> used by this expression.
     */
    FunctionContext getFunctionContext();

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
     *  @see VariableContext
     *  
     *  @return The <code>VariableContext</code> used by this expression.
     */
    VariableContext getVariableContext();
    

    /** Retrieve the XML object-model-specific {@link Navigator} 
     *  for us in evaluating this XPath expression.
     *
     *  @return The implementation-specific <code>Navigator</code>.
     */
    Navigator getNavigator();
}
