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
import java.util.Iterator;

import org.saxpath.SAXPathException;

/** Interface for navigating around an arbitrary object
 *  model, using xpath semantics.
 *
 *  <p>
 *  There is a method to obtain a <code>java.util.Iterator</code>,
 *  for each axis specified by XPath.  If the target object model
 *  does not support the semantics of a particular axis, an
 *  {@link UnsupportedAxisException} is to be thrown.
 *  </p>
 *
 *  @author bob mcwhirter (bob@werken.com)
 *  @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public interface Navigator extends Serializable
{

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    //     Axis Iterators
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    /** Retrieve an <code>Iterator</code> matching the <code>child</code>
     *  xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getChildAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>descendant</code>
     *  xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getDescendantAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>parent</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getParentAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>ancestor</code>
     *  xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getAncestorAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the
     *  <code>following-sibling</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getFollowingSiblingAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the
     *  <code>preceding-sibling</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getPrecedingSiblingAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>following</code>
     *  xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getFollowingAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>preceding</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getPrecedingAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>attribute</code>
     *  xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getAttributeAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>namespace</code>
     *  xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getNamespaceAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>self</code> xpath
     *  axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getSelfAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the
     *  <code>descendant-or-self</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getDescendantOrSelfAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the
     *  <code>ancestor-or-self</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getAncestorOrSelfAxisIterator(Object contextNode) throws UnsupportedAxisException;

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    //     Extractors
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    /** Loads a document from the given URI
     *
     * @param uri is the URI of the document to load
     *
     * @throws FunctionCallException if the document could not be loaded
     */
    Object getDocument(String uri) throws FunctionCallException;

    /** Returns the document node that contains the given context node.
     *
     *  @param object The context node.
     *
     *  @return The document of the context node.
     *
     *  @see #isDocument(Object)
     */
    Object getDocumentNode(Object contextNode);
    
    /** Returns the parent of the given context node.
     *
     *  <p>
     *  The parent of any node must either be a document
     *  node or an element node.
     *  </p>
     *
     *  @param object The context node.
     *
     *  @return The parent of the context node.
     *
     *  @see #isDocument
     *  @see #isElement
     */
    Object getParentNode(Object contextNode) throws UnsupportedAxisException;
    
    /** Retrieve the namespace URI of the given element node.
     *
     *  @param element The context element node.
     *
     *  @return The namespace URI of the element node.
     */
    String getElementNamespaceUri(Object element);    

    /** Retrieve the name of the given element node.
     *
     *  @param element The context element node.
     *
     *  @return The name of the element node.
     */
    String getElementName(Object element);    

    /** Retrieve the QName of the given element node.
     *
     *  @param element The context element node.
     *
     *  @return The QName of the element node.
     */
    String getElementQName(Object element);

    /** Retrieve the namespace URI of the given attribute node.
     *
     *  @param element The context attribute node.
     *
     *  @return The namespace URI of the attribute node.
     */
    String getAttributeNamespaceUri(Object attr);    

    /** Retrieve the name of the given attribute node.
     *
     *  @param element The context attribute node.
     *
     *  @return The name of the attribute node.
     */
    String getAttributeName(Object attr);

    /** Retrieve the QName of the given attribute node.
     *
     *  @param element The context attribute node.
     *
     *  @return The QName of the attribute node.
     */
    String getAttributeQName(Object attr);

    /** Retrieve the target of a processing-instruction.
     *
     *  @param pi The context processing-instruction node.
     *
     *  @return The target of the processing-instruction node.
     */
    String getProcessingInstructionTarget(Object pi);

    /** Retrieve the data of a processing-instruction.
     *
     *  @param pi The context processing-instruction node.
     *
     *  @return The data of the processing-instruction node.
     */
    String getProcessingInstructionData(Object pi);

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    //     isXXX testers
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    /** Returns whether the given object is a document node. A document node
     *  is the node that is selected by the xpath expression <code>/</code>.
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if the object is a document node,
     *          else <code>false</code>
     */
    boolean isDocument(Object object);

    /** Returns whether the given object is an element node.
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if the object is an element node,
     *          else <code>false</code>
     */
    boolean isElement(Object object);

    /** Returns whether the given object is an attribute node. 
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if the object is an attribute node,
     *          else <code>false</code>
     */
    boolean isAttribute(Object object);

    /** Returns whether the given object is a namespace node. 
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if the object is a namespace node,
     *          else <code>false</code>
     */
    boolean isNamespace(Object object);

    /** Returns whether the given object is a comment node. 
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if the object is a comment node,
     *          else <code>false</code>
     */
    boolean isComment(Object object);

    /** Returns whether the given object is a text node. 
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if the object is a text node,
     *          else <code>false</code>
     */
    boolean isText(Object object);

    /** Returns whether the given object is a processing-instruction node.
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if the object is a processing-instruction node,
     *          else <code>false</code>
     */
    boolean isProcessingInstruction(Object object);

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    //     String-Value extractors
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    /** Retrieve the string-value of a comment node.
     *
     *  @param comment The comment node.
     *
     *  @return The string-value of the node.
     */
    String getCommentStringValue(Object comment);

    /** Retrieve the string-value of an element node.
     *
     *  @param element The comment node.
     *
     *  @return The string-value of the node.
     */
    String getElementStringValue(Object element);

    /** Retrieve the string-value of an attribute node.
     *
     *  @param attr The attribute node.
     *
     *  @return The string-value of the node.
     */
    String getAttributeStringValue(Object attr);

    /** Retrieve the string-value of a namespace node.
     *
     *  @param attr The namespace node.
     *
     *  @return The string-value of the node.
     */
    String getNamespaceStringValue(Object ns);

    /** Retrieve the string-value of a text node.
     *
     *  @param attr The text node.
     *
     *  @return The string-value of the node.
     */
    String getTextStringValue(Object txt);

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    //     General utilities
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    /** Retrieve the namespace prefix of a namespace node.
     *
     *  @param ns The namespace node.
     *
     *  @return The prefix associated with the node.
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
     *  @param prefix The prefix to translate.
     *  @param element The element to consider during translation.
     *
     *  @return The namespace URI associated with the prefix.
     *
     *  @see NamespaceContext
     */
    String translateNamespacePrefixToUri(String prefix, Object element);

    /** Returns a parsed form of the given xpath string, which will be suitable
     *  for queries on documents that use the same navigator as this one.
     *
     *  @param xpath The xpath expression.
     *
     *  @return A new XPath expression object.
     *
     *  @see XPath
     */
    XPath parseXPath(String xpath) throws SAXPathException;

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
    Object getElementById(Object contextNode, String elementId);

    /** Returns a number that identifies the type of node that the given
     *  object represents in this navigator.
     *
     *  @see org.jaxen.pattern.Pattern
     */
    short getNodeType(Object node);
}
