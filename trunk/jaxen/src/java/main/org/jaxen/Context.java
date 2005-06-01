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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Wrapper around implementation-specific objects used
 *  as the context of an expression evaluation.
 *
 *  <p>
 *  <strong>NOTE:</strong> This class is not typically used directly,
 *  but is exposed for writers of implementation-specific
 *  XPath packages.
 *  </p>
 *
 *  <p>
 *  The <code>Context</code> bundles utilities together
 *  for evaluation of the expression.  It wraps the provided
 *  objects for ease-of-passage through the expression AST.
 *  </p>
 *
 *  @see ContextSupport
 *  @see BaseXPath
 *  @see org.jaxen.dom4j.Dom4jXPath XPath for dom4j
 *  @see org.jaxen.jdom.JDOMXPath  XPath for JDOM
 *  @see org.jaxen.dom.DOMXPath   XPath for W3C DOM
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class Context
    implements Serializable
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Context-support */
    private ContextSupport contextSupport;

    /** Context node-set */
    private List nodeSet;

    /** Current context size */
    private int size;

    /** Current context position */
    private int position;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Create a new context.
     *
     *  @param contextSupport the context-support
     */
    public Context(ContextSupport contextSupport)
    {
        this.contextSupport = contextSupport;
        this.nodeSet        = Collections.EMPTY_LIST;
    }
    
    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the context node-set.
     *
     *  @param nodeSet the context node-set
     */
    public void setNodeSet(List nodeSet)
    {
        this.nodeSet = nodeSet;
    }

    /** Retrieve the context node-set.
     *
     *  @return the context node-set
     */
    public List getNodeSet()
    {
        return this.nodeSet;
    }

    /** Set the <code>ContextSupport</code>.
     *
     *  @param contextSupport the context-support
     */
    public void setContextSupport(ContextSupport contextSupport)
    {
        this.contextSupport = contextSupport;
    }

    /** Retrieve the <code>ContextSupport</code>.
     *
     *  @return the context-support
     */
    public ContextSupport getContextSupport()
    {
        return this.contextSupport;
    }

    /** Retrieve the current <code>Navigator</code>.
     *
     *  @return the navigator
     */
    public Navigator getNavigator()
    {
        return getContextSupport().getNavigator();
    }

    /** Translate a namespace prefix to its URI.
     *
     *  @param prefix the prefix
     *
     *  @return the namespace URI mapped to the prefix
     */
    public String translateNamespacePrefixToUri(String prefix)
    {
        return getContextSupport().translateNamespacePrefixToUri( prefix );
    }

    /** Retrieve a variable value.
     *
     *  @param namespaceURI the function namespace URI
     *  @param prefix the function prefix
     *  @param localName the function name
     *
     *  @return the variable value
     *
     *  @throws UnresolvableException if unable to locate a bound variable
     */
    public Object getVariableValue(String namespaceURI,
                                   String prefix,
                                   String localName)
        throws UnresolvableException
    {
        return getContextSupport().getVariableValue( namespaceURI,
                                                     prefix,
                                                     localName );
    }

    /** Retrieve a <code>Function</code>.
     *
     *  @param namespaceURI the function namespace URI
     *  @param prefix the function prefix
     *  @param localName the function name
     *
     *  @return the function object
     *
     *  @throws UnresolvableException if unable to locate a bound function
     */
    public Function getFunction(String namespaceURI,
                                String prefix,
                                String localName)
        throws UnresolvableException
    {
        return getContextSupport().getFunction( namespaceURI,
                                                prefix,
                                                localName );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     Properties
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Set the current size in the context node-set.
     *
     *  @param size the size
     */
    public void setSize(int size)
    {
        this.size = size;
    }

    /** Retrieve the size of the context node-set.
     *
     *  @return the size
     */
    public int getSize()
    {
        return this.size;
    }

    /** Set the current position in the context node-set.
     *
     *  @param position the position
     */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /** Retrieve current position in the context node-set.
     *
     *  @return the current position
     */
    public int getPosition()
    {
        return this.position;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     Helpers
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Create a type-safe shallow copy.
     *
     *  @return the duplicate
     */
    public Context duplicate()
    {
        Context dupe = new Context( getContextSupport() );

        List thisNodeSet = getNodeSet();

        if ( thisNodeSet != null )
        {
            List dupeNodeSet = new ArrayList( thisNodeSet.size() );
            dupeNodeSet.addAll( thisNodeSet );
            dupe.setNodeSet( dupeNodeSet );
        }

        return dupe;
    }
}
