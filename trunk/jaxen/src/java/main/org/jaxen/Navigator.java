package org.jaxen;

/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2005 bob mcwhirter & James Strachan.
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

import java.io.Serializable;
import java.util.Iterator;

/** Interface for navigating around an arbitrary object
 *  model, using XPath semantics.
 *
 *  <p>
 *  There is a method to obtain a <code>java.util.Iterator</code>,
 *  for each axis specified by XPath.  If the target object model
 *  does not support the semantics of a particular axis, an
 *  {@link UnsupportedAxisException} is to be thrown. If there are
 *  no nodes on that axis, an empty iterator should be returned.
 *  </p>
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 *
 *  @version $Id$
 */
public interface Navigator extends Serializable
{
    // ----------------------------------------------------------------------
    //     Axis Iterators
    // ----------------------------------------------------------------------

    /** Retrieve an <code>Iterator</code> matching the <code>child</code>
     *  XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the child axis are
     *          not supported by this object model
     */
    Iterator getChildAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>descendant</code>
     *  XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the desscendant axis are
     *          not supported by this object model
     */
    Iterator getDescendantAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>parent</code> XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the parent axis are
     *          not supported by this object model
     */
    Iterator getParentAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>ancestor</code>
     *  XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the ancestor axis are
     *          not supported by this object model
     */
    Iterator getAncestorAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the
     *  <code>following-sibling</code> XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the following-sibling axis are
     *          not supported by this object model
     */
    Iterator getFollowingSiblingAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the
     *  <code>preceding-sibling</code> XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the preceding-sibling axis are
     *          not supported by this object model
     */
    Iterator getPrecedingSiblingAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>following</code>
     *  XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the following axis are
     *          not supported by this object model
     */
    Iterator getFollowingAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>preceding</code> XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the preceding axis are
     *          not supported by this object model
     */
    Iterator getPrecedingAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>attribute</code>
     *  XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the attribute axis are
     *          not supported by this object model
     */
    Iterator getAttributeAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>namespace</code>
     *  XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the namespace axis are
     *          not supported by this object model
     */
    Iterator getNamespaceAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>self</code> xpath
     *  axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the self axis are
     *          not supported by this object model
     */
    Iterator getSelfAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the
     *  <code>descendant-or-self</code> XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the descendant-or-self axis are
     *          not supported by this object model
     */
    Iterator getDescendantOrSelfAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the
     *  <code>ancestor-or-self</code> XPath axis.
     *
     *  @param contextNode the original context node
     *
     *  @return an Iterator capable of traversing the axis, not null
     *
     *  @throws UnsupportedAxisException if the semantics of the ancestor-or-self axis are
     *          not supported by this object model
     */
    Iterator getAncestorOrSelfAxisIterator(Object contextNode)
        throws UnsupportedAxisException;

    // ----------------------------------------------------------------------
    //     Extractors
    // ----------------------------------------------------------------------

    /** Loads a document from the given URI
     *
     *  @param uri the URI of the document to load
     *
     *  @return the document
     *
      * @throws FunctionCallException if the document could not be loaded
     */
    Object getDocument(String uri)
        throws FunctionCallException;

    /** Returns the document node that contains the given context node.
     *
     *  @see #isDocument(Object)
     *
     *  @param contextNode the context node
     *
     *  @return the document of the context node
     */
    Object getDocumentNode(Object contextNode);
    
    /** Returns the parent of the given context node.
     *
     *  <p>
     *  The parent of any node must either be a document
     *  node or an element node.
     *  </p>
     *
     *  @see #isDocument
     *  @see #isElement
     *
     *  @param contextNode the context node
     *
     *  @return the parent of the context node, or null if this is a document node.
     *
     *  @throws UnsupportedAxisException if the parent axis is not
     *          supported by the model
     */
    Object getParentNode(Object contextNode)
        throws UnsupportedAxisException;
    
    /** Retrieve the namespace URI of the given element node.
     *
     *  @param element the context element node
     *
     *  @return the namespace URI of the element node
     */
    String getElementNamespaceUri(Object element);    

    /** Retrieve the name of the given element node.
     *
     *  @param element the context element node
     *
     *  @return the name of the element node
     */
    String getElementName(Object element);    

    /** Retrieve the QName of the given element node.
     *
     *  @param element the context element node
     *
     *  @return the QName of the element node
     */
    String getElementQName(Object element);

    /** Retrieve the namespace URI of the given attribute node.
     *
     *  @param attr the context attribute node
     *
     *  @return the namespace URI of the attribute node
     */
    String getAttributeNamespaceUri(Object attr);    

    /** Retrieve the name of the given attribute node.
     *
     *  @param attr the context attribute node
     *
     *  @return the name of the attribute node
     */
    String getAttributeName(Object attr);

    /** Retrieve the QName of the given attribute node.
     *
     *  @param attr the context attribute node
     *
     *  @return the qualified name of the attribute node
     */
    String getAttributeQName(Object attr);

    /** Retrieve the target of a processing-instruction.
     *
     *  @param pi the context processing-instruction node
     *
     *  @return the target of the processing-instruction node
     */
    String getProcessingInstructionTarget(Object pi);

    /** Retrieve the data of a processing-instruction.
     *
     *  @param pi the context processing-instruction node
     *
     *  @return the data of the processing-instruction node
     */
    String getProcessingInstructionData(Object pi);

    // ----------------------------------------------------------------------
    //     isXXX testers
    // ----------------------------------------------------------------------

    /** Returns whether the given object is a document node. A document node
     *  is the node that is selected by the xpath expression <code>/</code>.
     *
     *  @param object the object to test
     *
     *  @return <code>true</code> if the object is a document node,
     *          else <code>false</code>
     */
    boolean isDocument(Object object);

    /** Returns whether the given object is an element node.
     *
     *  @param object the object to test
     *
     *  @return <code>true</code> if the object is an element node,
     *          else <code>false</code>
     */
    boolean isElement(Object object);

    /** Returns whether the given object is an attribute node. 
     *
     *  @param object the object to test
     *
     *  @return <code>true</code> if the object is an attribute node,
     *          else <code>false</code>
     */
    boolean isAttribute(Object object);

    /** Returns whether the given object is a namespace node. 
     *
     *  @param object the object to test
     *
     *  @return <code>true</code> if the object is a namespace node,
     *          else <code>false</code>
     */
    boolean isNamespace(Object object);

    /** Returns whether the given object is a comment node. 
     *
     *  @param object the object to test
     *
     *  @return <code>true</code> if the object is a comment node,
     *          else <code>false</code>
     */
    boolean isComment(Object object);

    /** Returns whether the given object is a text node. 
     *
     *  @param object the object to test
     *
     *  @return <code>true</code> if the object is a text node,
     *          else <code>false</code>
     */
    boolean isText(Object object);

    /** Returns whether the given object is a processing-instruction node.
     *
     *  @param object the object to test
     *
     *  @return <code>true</code> if the object is a processing-instruction node,
     *          else <code>false</code>
     */
    boolean isProcessingInstruction(Object object);

    // ----------------------------------------------------------------------
    //     String-Value extractors
    // ----------------------------------------------------------------------

    /** Retrieve the string-value of a comment node.
     *
     *  @param comment the comment node
     *
     *  @return the string-value of the node
     */
    String getCommentStringValue(Object comment);

    /** Retrieve the string-value of an element node.
     *
     *  @param element the comment node.
     *
     *  @return the string-value of the node.
     */
    String getElementStringValue(Object element);

    /** Retrieve the string-value of an attribute node.
     *
     *  @param attr the attribute node
     *
     *  @return the string-value of the node
     */
    String getAttributeStringValue(Object attr);

    /** Retrieve the string-value of a namespace node.
     *
     *  @param ns the namespace node
     *
     *  @return the string-value of the node
     */
    String getNamespaceStringValue(Object ns);

    /** Retrieve the string-value of a text node.
     *
     *  @param txt the text node
     *
     *  @return the string-value of the node
     */
    String getTextStringValue(Object txt);

    // ----------------------------------------------------------------------
    //     General utilities
    // ----------------------------------------------------------------------

    /** Retrieve the namespace prefix of a namespace node.
     *
     *  @param ns the namespace node
     *
     *  @return the prefix associated with the node
     */
    String getNamespacePrefix(Object ns);

    
    /** Translate a namespace prefix to a namespace URI, <b>possibly</b>
     *  considering a particular element node.
     *
     *  <p>
     *  Strictly speaking, prefix-to-URI translation should occur
     *  irrespective of any element in the document.  This method
     *  is provided to allow a non-conforming ease-of-use enhancement.
     *  </p>
     *
     *  @see NamespaceContext
     *
     *  @param prefix the prefix to translate
     *  @param element the element to consider during translation
     *
     *  @return the namespace URI associated with the prefix
     */
    String translateNamespacePrefixToUri(String prefix,
                                         Object element);

    /** Returns a parsed form of the given xpath string, which will be suitable
     *  for queries on documents that use the same navigator as this one.
     *
     *  @see XPath
     *
     *  @param xpath the XPath expression
     *
     *  @return a new XPath expression object
     *
     *  @throws org.jaxen.saxpath.SAXPathException if an error occurs while parsing the
     *          XPath expression
     */
    XPath parseXPath(String xpath)
        throws org.jaxen.saxpath.SAXPathException;

    /**
     *  Returns the element whose ID is given by elementId.
     *  If no such element exists, returns null.
     *  Attributes with the name "ID" are not of type ID unless so defined.
     *  Implementations that do not know whether attributes are of type ID or
     *  not are expected to return null.
     *
     *  @param contextNode   a node from the document in which to look for the
     *                       id
     *  @param elementId   id to look for
     *
     *  @return   element whose ID is given by elementId, or null if no such
     *            element exists in the document or if the implementation
     *            does not know about attribute types
     */
    Object getElementById(Object contextNode,
                          String elementId);

    /** Returns a number that identifies the type of node that the given
     *  object represents in this navigator.
     * 
     * @param node ????
     * @return ????
     *
     *  @see org.jaxen.pattern.Pattern
     */
    short getNodeType(Object node);
}
