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
 *  @see org.jaxen.dom4j.Dom4jXPath XPath for dom4j
 *  @see org.jaxen.jdom.JDOMXPath  XPath for JDOM
 *  @see org.jaxen.dom.DOMXPath   XPath for W3C DOM
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class ContextSupport
    implements Serializable
{
    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Function context. */
    private transient FunctionContext functionContext;
    
    /** Namespace context. */
    private NamespaceContext namespaceContext;

    /** Variable context. */
    private VariableContext variableContext;
    
    /** Model navigator. */
    private Navigator navigator;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------
    
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
     *  @param navigator The model navigator.
     */
    public ContextSupport(NamespaceContext namespaceContext,
                          FunctionContext functionContext,
                          VariableContext variableContext,
                          Navigator navigator)
    {
        setNamespaceContext( namespaceContext );
        setFunctionContext( functionContext );
        setVariableContext( variableContext );

        this.navigator = navigator;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the <code>NamespaceContext</code>.
     *
     *  @param namespaceContext The namespace context.
     */
    public void setNamespaceContext(NamespaceContext namespaceContext)
    {
        this.namespaceContext = namespaceContext;
    }

    /** Retrieve the <code>NamespaceContext</code>.
     *
     *  @return The namespace context.
     */
    public NamespaceContext getNamespaceContext()
    {
        return this.namespaceContext;
    }

    /** Set the <code>FunctionContext</code>.
     *
     *  @param functionContext The function context.
     */
    public void setFunctionContext(FunctionContext functionContext)
    {
        this.functionContext  = functionContext;
    }

    /** Retrieve the <code>FunctionContext</code>.
     *
     *  @return The function context.
     */
    public FunctionContext getFunctionContext()
    {
        return this.functionContext;
    }

    /** Set the <code>VariableContext</code>.
     *
     *  @param variableContext The variable context.
     */
    public void setVariableContext(VariableContext variableContext)
    {
        this.variableContext  = variableContext;
    }

    /** Retrieve the <code>VariableContext</code>.
     *
     *  @return The variable context.
     */
    public VariableContext getVariableContext()
    {
        return this.variableContext;
    }

    /** Retrieve the <code>Navigator</code>.
     *
     *  @return The navigator.
     */
    public Navigator getNavigator()
    {
        return this.navigator;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Translate a namespace prefix to its URI.
     *
     *  @param prefix The prefix.
     *
     *  @return The naemspace URI mapped to the prefix.
     */
    public String translateNamespacePrefixToUri(String prefix)
    {
        NamespaceContext context = getNamespaceContext();

        if ( context != null )
        {
            return context.translateNamespacePrefixToUri( prefix );
        }

        return null;
    }

    /** Retrieve a variable value.
     *
     *  @param namespaceURI The function namespace URI.
     *  @param prefix The function prefix.
     *  @param localName The function name.
     *
     *  @return The variable value.
     *
     *  @throws UnresolvableException If unable to locate a bound variable.
     */
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

    /** Retrieve a <code>Function</code>.
     *
     *  @param namespaceURI The function namespace URI.
     *  @param prefix The function prefix.
     *  @param localName The function name.
     *
     *  @return The function object.
     *
     *  @throws UnresolvableException If unable to locate a bound function.
     */
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
