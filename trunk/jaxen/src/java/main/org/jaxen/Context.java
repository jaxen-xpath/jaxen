/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows 
 *    these conditions in the documentation and/or other materials 
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 * 
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project (http://www.jaxen.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id$
 */


package org.jaxen;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** Wrapper around implementation-specific objects used
 *  as the context of an expression evaluation.
 *
 *  <p>
 *  <b>NOTE:</b> This class is not typically used directly,
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
 *  @see org.jaxen.exml.ElectricXPath  XPath for Electric XML
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class Context implements Serializable
{
    private ContextSupport contextSupport;

    private List           nodeSet;

    private int size;
    private int position;

    public Context(ContextSupport contextSupport)
    {
        this.contextSupport = contextSupport;
        this.nodeSet        = Collections.EMPTY_LIST;
    }

    public List getNodeSet()
    {
        return this.nodeSet;
    }

    public void setNodeSet(List nodeSet)
    {
        this.nodeSet = nodeSet;
    }

    public ContextSupport getContextSupport()
    {
        return this.contextSupport;
    }

    public void setContextSupport(ContextSupport contextSupport)
    {
        this.contextSupport = contextSupport;
    }

    public Navigator getNavigator()
    {
        return getContextSupport().getNavigator();
    }

    public String translateNamespacePrefixToUri(String prefix)
    {
        return getContextSupport().translateNamespacePrefixToUri( prefix );
    }

    public Object getVariableValue( String namespaceURI,
                                    String prefix,
                                    String localName )
        throws UnresolvableException
    {
        return getContextSupport().getVariableValue( namespaceURI,
                                                     prefix,
                                                     localName );
    }

    public Function getFunction( String namespaceURI,
                                 String prefix,
                                 String localName )
        throws UnresolvableException
    {
        return getContextSupport().getFunction( namespaceURI,
                                                prefix,
                                                localName );
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public int getSize()
    {
        return this.size;
    }

    public int getPosition()
    {
        return this.position;
    }

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
