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

import java.util.Iterator;

import org.jaxen.pattern.Pattern;
import org.jaxen.util.AncestorAxisIterator;
import org.jaxen.util.AncestorOrSelfAxisIterator;
import org.jaxen.util.DescendantAxisIterator;
import org.jaxen.util.DescendantOrSelfAxisIterator;
import org.jaxen.util.FollowingAxisIterator;
import org.jaxen.util.FollowingSiblingAxisIterator;
import org.jaxen.util.PrecedingAxisIterator;
import org.jaxen.util.PrecedingSiblingAxisIterator;
import org.jaxen.util.SelfAxisIterator;

/** Default implementation of {@link Navigator}.
 *
 *  <p>
 *  This implementation is an abstract class, since
 *  some required operations cannot be implemented without
 *  additional knowledge of the object model.
 *  </p>
 *
 *  <p>
 *  When possible, default method implementations build
 *  upon each other, to reduce the number of methods required
 *  to be implemented for each object model.  All methods,
 *  of course, may be overridden, to provide more-efficient
 *  implementations.
 *  </p>
 *
 *  @author bob mcwhirter (bob@werken.com)
 *  @author Erwin Bolwidt (ejb@klomp.org)
 */
public abstract class DefaultNavigator implements Navigator
{
    /** Throws <code>UnsupportedAxisException</code>
     */
    public Iterator getChildAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        throw new UnsupportedAxisException("child");
    }

    public Iterator getDescendantAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new DescendantAxisIterator( contextNode,
                                           this );
    }

    /** Throws <code>UnsupportedAxisException</code>
     */
    public Iterator getParentAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        throw new UnsupportedAxisException("parent");
    }

    public Iterator getAncestorAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new AncestorAxisIterator( contextNode,
                                         this );
    }

    /** Throws <code>UnsupportedAxisException</code>
     */
    public Iterator getFollowingSiblingAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new FollowingSiblingAxisIterator( contextNode,
                                                 this );
    }

    /** Throws <code>UnsupportedAxisException</code>
     */
    public Iterator getPrecedingSiblingAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new PrecedingSiblingAxisIterator( contextNode,
                                                 this );
    }

    /** Throws <code>UnsupportedAxisException</code>
     */
    public Iterator getFollowingAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new FollowingAxisIterator( contextNode,
                                          this );

        // throw new UnsupportedAxisException("following");
    }

    /** Throws <code>UnsupportedAxisException</code>
     */
    public Iterator getPrecedingAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new PrecedingAxisIterator( contextNode,
                                         this );

        // throw new UnsupportedAxisException("preceding");
    }

    /** Throws <code>UnsupportedAxisException</code>
     */
    public Iterator getAttributeAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        throw new UnsupportedAxisException("attribute");
    }

    /** Throws <code>UnsupportedAxisException</code>
     */
    public Iterator getNamespaceAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        throw new UnsupportedAxisException("namespace");
    }

    public Iterator getSelfAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new SelfAxisIterator( contextNode );
    }

    public Iterator getDescendantOrSelfAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new DescendantOrSelfAxisIterator( contextNode,
                                                 this );
    }

    public Iterator getAncestorOrSelfAxisIterator(Object contextNode) throws UnsupportedAxisException
    {
        return new AncestorOrSelfAxisIterator( contextNode,
                                               this );
    }

    public Object getDocumentNode(Object contextNode)
    {
        return null;
    }
    
    public String translateNamespacePrefixToUri(String prefix, Object element)
    {
        return null;
    }

    public String getProcessingInstructionTarget(Object obj)
    {
        return null;
    }

    public String getProcessingInstructionData(Object obj)
    {
        return null;
    }

    public short getNodeType(Object node)
    {
        if ( isElement(node) ) 
        {
            return Pattern.ELEMENT_NODE;
        }
        else if ( isAttribute(node) ) 
        {
            return Pattern.ATTRIBUTE_NODE;
        }
        else if ( isText(node) ) 
        {
            return Pattern.TEXT_NODE;
        }
        else if ( isComment(node) ) 
        {
            return Pattern.COMMENT_NODE;
        }
        else if ( isDocument(node) ) 
        {
            return Pattern.DOCUMENT_NODE;
        }
        else if ( isProcessingInstruction(node) ) 
        {
            return Pattern.PROCESSING_INSTRUCTION_NODE;
        }
        else {
            return Pattern.UNKNOWN_NODE;
        }
    }
    
    public Object getParentNode(Object contextNode) throws UnsupportedAxisException
    {
        Iterator iter = getParentAxisIterator( contextNode );
        if ( iter != null && iter.hasNext() )
        {
            return iter.next();
        }
        return null;
    }

    public Object getDocument(String url) throws FunctionCallException
    {
        return null;
    }

    /**
     *  Default implementation that can not find elements. Override in subclass
     *  if subclass does know about attribute types.
     *
     *  @param contextNode   a node from the document in which to look for the
     *                       id
     *  @param elementId   id to look for
     *
     *  @return   null
     */
    public Object getElementById(Object object, String elementId)
    {
        return null;
    }
}
